package com.example.tatkala.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.tatkala.ui.screens.settings.components.SettingsRow

private val TatakalaPurple = Color(0xFF6D49AE)
private val TatakalaBackground = Color(0xFFF8F8F8)

@Composable
fun PrivacyScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TatakalaBackground)
            .statusBarsPadding()
            .padding(20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TatakalaPurple
                )
            }

            Text(
                text = "Privacy & Security",
                color = Color(0xFF4C258C),
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.padding(12.dp))

        SettingsRow(
            icon = Icons.Default.Key,
            title = "Change Password",
            onClick = {}
        )

        Spacer(modifier = Modifier.padding(6.dp))

        SettingsRow(
            icon = Icons.Default.Policy,
            title = "Privacy Policy",
            onClick = {}
        )

        Spacer(modifier = Modifier.padding(6.dp))

        SettingsRow(
            icon = Icons.Default.Delete,
            title = "Manage Local Data",
            onClick = {}
        )

        Spacer(modifier = Modifier.padding(6.dp))

        SettingsRow(
            icon = Icons.Default.Lock,
            title = "Security",
            onClick = {}
        )
    }
}
