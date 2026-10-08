package com.example.tatkala.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest

data class UserProfile(
    val displayName: String = "Tatakala User",
    val username: String = "tatakala",
    val email: String = "user@tatakala.local",
    val phoneNumber: String = "",
    val avatarUri: String = ""
)

object UserRepository {
    private const val PREF_NAME = "tatakala_user"
    private lateinit var context: Context
    private var initialized = false

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    private val _loggedIn = MutableStateFlow(false)
    val loggedIn: StateFlow<Boolean> = _loggedIn.asStateFlow()

    fun init(appContext: Context) {
        if (initialized) return
        context = appContext.applicationContext
        initialized = true
        load()
    }

    fun register(displayName: String, username: String, email: String, password: String) {
        val cleanUsername = username.ifBlank { email.substringBefore("@") }
        val newProfile = UserProfile(
            displayName = displayName,
            username = cleanUsername,
            email = email,
            phoneNumber = _profile.value.phoneNumber,
            avatarUri = _profile.value.avatarUri
        )
        _profile.value = newProfile
        _loggedIn.value = true
        prefs().edit()
            .putString("display_name", newProfile.displayName)
            .putString("username", newProfile.username)
            .putString("email", newProfile.email)
            .putString("password_hash", hash(password))
            .putBoolean("logged_in", true)
            .apply()
    }

    fun login(email: String, password: String): Boolean {
        val prefs = prefs()
        val storedEmail = prefs.getString("email", null)
        val storedHash = prefs.getString("password_hash", null)
        val valid = if (storedEmail == null || storedHash == null) {
            email.isNotBlank() && password.isNotBlank()
        } else {
            email.equals(storedEmail, ignoreCase = true) && hash(password) == storedHash
        }
        if (valid) {
            _loggedIn.value = true
            prefs.edit().putBoolean("logged_in", true).apply()
        }
        return valid
    }

    fun updateProfile(profile: UserProfile) {
        _profile.value = profile
        prefs().edit()
            .putString("display_name", profile.displayName)
            .putString("username", profile.username)
            .putString("email", profile.email)
            .putString("phone", profile.phoneNumber)
            .putString("avatar_uri", profile.avatarUri)
            .apply()
    }

    fun logout() {
        _loggedIn.value = false
        prefs().edit().putBoolean("logged_in", false).apply()
    }

    fun reset() {
        prefs().edit().clear().apply()
        _profile.value = UserProfile()
        _loggedIn.value = false
    }

    private fun load() {
        val prefs = prefs()
        _profile.value = UserProfile(
            displayName = prefs.getString("display_name", "Tatakala User") ?: "Tatakala User",
            username = prefs.getString("username", "tatakala") ?: "tatakala",
            email = prefs.getString("email", "user@tatakala.local") ?: "user@tatakala.local",
            phoneNumber = prefs.getString("phone", "") ?: "",
            avatarUri = prefs.getString("avatar_uri", "") ?: ""
        )
        _loggedIn.value = prefs.getBoolean("logged_in", false)
    }

    private fun prefs() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private fun hash(raw: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
