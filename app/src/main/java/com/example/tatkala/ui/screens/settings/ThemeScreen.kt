package com.example.tatkala.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val TatakalaPurple = Color(0xFF6D49AE)
private val TatakalaBackground = Color(0xFFF8F8F8)

@Composable
fun ThemeScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedTheme by remember {
        mutableStateOf("System")
    }

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
                text = "Theme",
                color = Color(0xFF4C258C),
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.padding(12.dp))

        ThemeOption(
            title = "System",
            icon = Icons.Default.SettingsBrightness,
            selected = selectedTheme == "System",
            onClick = {
                selectedTheme = "System"
            }
        )

        Spacer(modifier = Modifier.padding(6.dp))

        ThemeOption(
            title = "Light",
            icon = Icons.Default.LightMode,
            selected = selectedTheme == "Light",
            onClick = {
                selectedTheme = "Light"
            }
        )

        Spacer(modifier = Modifier.padding(6.dp))

        ThemeOption(
            title = "Dark",
            icon = Icons.Default.DarkMode,
            selected = selectedTheme == "Dark",
            onClick = {
                selectedTheme = "Dark"
            }
        )
    }
}

@Composable
private fun ThemeOption(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TatakalaPurple
            )

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                color = Color(0xFF252525)
            )

            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    }
}
