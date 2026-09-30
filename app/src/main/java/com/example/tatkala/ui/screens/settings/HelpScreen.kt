package com.example.tatakala.ui.screens.settings

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
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
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
                    tint = Color(0xFF6D49AE)
                )
            }

            Text(
                text = "Help",
                color = Color(0xFF4C258C),
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Icon(
            imageVector = Icons.Default.Help,
            contentDescription = null,
            tint = Color(0xFF6D49AE)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = "Bantuan Tatakala",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            color = Color(0xFF252525)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = "Tatakala membantu kamu mengatur waktu, membuat jadwal, " +
                    "mengelola tugas, dan membangun kebiasaan secara lebih teratur.",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            color = Color(0xFF555555)
        )
    }
}