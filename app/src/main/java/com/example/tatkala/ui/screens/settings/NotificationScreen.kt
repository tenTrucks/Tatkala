package com.example.tatkala.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tatkala.data.repository.NotificationSettings
import com.example.tatkala.data.repository.SettingsRepository

@Composable
fun NotificationScreen(onBack: () -> Unit = {}) {
    val settings by SettingsRepository.notifications.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Notification", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column {
                NotificationItem("Master Notification", "Allow Tatakala reminder preferences.", settings.master) {
                    SettingsRepository.setNotifications(settings.copy(master = it))
                }
                NotificationItem("Task Reminder", "Reminder preference for upcoming tasks.", settings.taskReminder) {
                    SettingsRepository.setNotifications(settings.copy(taskReminder = it))
                }
                NotificationItem("Habit Reminder", "Reminder preference for habits.", settings.habitReminder) {
                    SettingsRepository.setNotifications(settings.copy(habitReminder = it))
                }
                NotificationItem("Collaboration Reminder", "Reminder preference for collaborative tasks.", settings.collaborationReminder) {
                    SettingsRepository.setNotifications(settings.copy(collaborationReminder = it))
                }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            "This prototype persists reminder preferences locally. Full scheduled notifications can be enabled later without changing your data.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NotificationItem(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
