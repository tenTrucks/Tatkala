package com.example.tatkala.ui.screens.settings.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tatkala.data.repository.UserProfile
import com.example.tatkala.data.repository.UserRepository

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val profile by UserRepository.profile.collectAsState()
    var fullName by remember(profile.displayName) { mutableStateOf(profile.displayName) }
    var username by remember(profile.username) { mutableStateOf(profile.username.removePrefix("@")) }
    var email by remember(profile.email) { mutableStateOf(profile.email) }
    var phone by remember(profile.phoneNumber) { mutableStateOf(profile.phoneNumber) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Edit Profile",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it; error = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Display name") },
            singleLine = true
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it; error = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Username") },
            singleLine = true
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; error = "" },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Phone") },
            singleLine = true
        )
        if (error.isNotBlank()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = {
                when {
                    fullName.isBlank() -> error = "Display name is required."
                    username.isBlank() -> error = "Username is required."
                    email.isBlank() -> error = "Email is required."
                    else -> {
                        UserRepository.updateProfile(
                            UserProfile(
                                displayName = fullName.trim(),
                                username = username.trim(),
                                email = email.trim(),
                                phoneNumber = phone.trim(),
                                avatarUri = profile.avatarUri
                            )
                        )
                        onSave()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Save Changes")
        }
    }
}
