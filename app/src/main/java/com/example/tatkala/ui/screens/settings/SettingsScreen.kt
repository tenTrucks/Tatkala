package com.example.tatkala.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tatkala.ui.screens.settings.components.SettingsRow

@Composable
fun SettingsScreen(
    onProfileClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onThemeClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onFaqClick: () -> Unit = {},
    onRatingClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.padding(4.dp))

        SettingsSectionTitle("ACCOUNT")

        SettingsRow(
            icon = Icons.Default.Person,
            title = "Profile",
            onClick = onProfileClick
        )

        SettingsRow(
            icon = Icons.Default.Notifications,
            title = "Notification",
            onClick = onNotificationClick
        )

        Spacer(modifier = Modifier.padding(5.dp))

        SettingsSectionTitle("PREFERENCES")

        SettingsRow(
            icon = Icons.Default.Language,
            title = "Language",
            onClick = onLanguageClick
        )

        SettingsRow(
            icon = Icons.Default.Palette,
            title = "Theme",
            onClick = onThemeClick
        )

        Spacer(modifier = Modifier.padding(5.dp))

        SettingsSectionTitle("PRIVACY")

        SettingsRow(
            icon = Icons.Default.Lock,
            title = "Privacy / Security",
            onClick = onPrivacyClick
        )

        Spacer(modifier = Modifier.padding(5.dp))

        SettingsSectionTitle("SUPPORT")

        SettingsRow(
            icon = Icons.Default.Help,
            title = "Help",
            onClick = onHelpClick
        )

        SettingsRow(
            icon = Icons.Default.QuestionAnswer,
            title = "FAQ",
            onClick = onFaqClick
        )

        SettingsRow(
            icon = Icons.Default.Star,
            title = "Rating",
            onClick = onRatingClick
        )

        SettingsRow(
            icon = Icons.Default.Info,
            title = "About Us",
            onClick = onAboutClick
        )

        Spacer(modifier = Modifier.padding(20.dp))
    }
}

@Composable
private fun SettingsSectionTitle(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 4.dp,
                bottom = 4.dp
            ),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp
    )
}
