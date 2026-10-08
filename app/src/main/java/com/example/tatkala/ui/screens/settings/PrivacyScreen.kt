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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tatkala.data.repository.HabitRepository
import com.example.tatkala.data.repository.SettingsRepository
import com.example.tatkala.data.repository.TaskRepository
import com.example.tatkala.data.repository.UserRepository
import kotlinx.coroutines.launch

@Composable
fun PrivacyScreen(
    onBackClick: () -> Unit = {},
    onFullReset: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var confirmActivityClear by remember { mutableStateOf(false) }
    var confirmFullReset by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Privacy / Local Data", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Local-first prototype", fontWeight = FontWeight.SemiBold)
                Text(
                    "Tatakala stores schedules, habits, profile, session, and preferences locally on this device. No online sync or backend is active in this prototype.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { confirmActivityClear = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Text("Clear activity data")
        }
        Button(
            onClick = { confirmFullReset = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text("Reset application data")
        }
    }

    if (confirmActivityClear) {
        AlertDialog(
            onDismissRequest = { confirmActivityClear = false },
            title = { Text("Clear activity data?") },
            text = { Text("This deletes local tasks, habits, and habit logs. Profile and settings stay intact.") },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        TaskRepository.clear()
                        HabitRepository.clear()
                        confirmActivityClear = false
                    }
                }) { Text("Clear") }
            },
            dismissButton = { TextButton(onClick = { confirmActivityClear = false }) { Text("Cancel") } }
        )
    }

    if (confirmFullReset) {
        AlertDialog(
            onDismissRequest = { confirmFullReset = false },
            title = { Text("Reset app data?") },
            text = { Text("This clears tasks, habits, profile/session, settings, and returns to Login.") },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        TaskRepository.clear()
                        HabitRepository.clear()
                        UserRepository.reset()
                        SettingsRepository.resetSettings()
                        confirmFullReset = false
                        onFullReset()
                    }
                }) { Text("Reset") }
            },
            dismissButton = { TextButton(onClick = { confirmFullReset = false }) { Text("Cancel") } }
        )
    }
}
