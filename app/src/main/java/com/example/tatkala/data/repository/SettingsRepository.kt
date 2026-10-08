package com.example.tatkala.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class NotificationSettings(
    val master: Boolean = true,
    val taskReminder: Boolean = true,
    val habitReminder: Boolean = true,
    val collaborationReminder: Boolean = true
)

object SettingsRepository {
    private const val PREF_NAME = "tatakala_settings"
    private lateinit var context: Context
    private var initialized = false

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _language = MutableStateFlow("id")
    val language: StateFlow<String> = _language.asStateFlow()

    private val _notifications = MutableStateFlow(NotificationSettings())
    val notifications: StateFlow<NotificationSettings> = _notifications.asStateFlow()

    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    fun init(appContext: Context) {
        if (initialized) return
        context = appContext.applicationContext
        initialized = true
        val prefs = prefs()
        _themeMode.value = runCatching {
            ThemeMode.valueOf(prefs.getString("theme", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name)
        }.getOrDefault(ThemeMode.SYSTEM)
        _language.value = prefs.getString("language", "id") ?: "id"
        _notifications.value = NotificationSettings(
            master = prefs.getBoolean("notification_master", true),
            taskReminder = prefs.getBoolean("notification_task", true),
            habitReminder = prefs.getBoolean("notification_habit", true),
            collaborationReminder = prefs.getBoolean("notification_collaboration", true)
        )
        _rating.value = prefs.getInt("rating", 0)
        _onboardingCompleted.value = prefs.getBoolean("onboarding_completed", false)
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        prefs().edit().putString("theme", mode.name).apply()
    }

    fun setLanguage(language: String) {
        _language.value = language
        prefs().edit().putString("language", language).apply()
    }

    fun setNotifications(settings: NotificationSettings) {
        _notifications.value = settings
        prefs().edit()
            .putBoolean("notification_master", settings.master)
            .putBoolean("notification_task", settings.taskReminder)
            .putBoolean("notification_habit", settings.habitReminder)
            .putBoolean("notification_collaboration", settings.collaborationReminder)
            .apply()
    }

    fun setRating(value: Int) {
        _rating.value = value.coerceIn(1, 5)
        prefs().edit().putInt("rating", _rating.value).apply()
    }

    fun completeOnboarding() {
        _onboardingCompleted.value = true
        prefs().edit().putBoolean("onboarding_completed", true).apply()
    }

    fun resetSettings() {
        prefs().edit().clear().apply()
        _themeMode.value = ThemeMode.SYSTEM
        _language.value = "id"
        _notifications.value = NotificationSettings()
        _rating.value = 0
        _onboardingCompleted.value = false
    }

    private fun prefs() = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}
