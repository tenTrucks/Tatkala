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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FaqScreen(
    onBackClick: () -> Unit = {}
) {
    val faqItems = listOf(
        "Apa itu Tatakala?" to
                "Tatakala adalah aplikasi untuk membantu mengatur waktu, " +
                "jadwal, tugas, dan kebiasaan.",

        "Bagaimana cara membuat jadwal?" to
                "Gunakan fitur Add Schedule untuk membuat dan mengatur jadwal.",

        "Apakah tema dapat diubah?" to
                "Ya. Pilihan tema tersedia pada menu Theme.",

        "Apakah bahasa aplikasi dapat diubah?" to
                "Pilihan Bahasa Indonesia dan English tersedia pada menu Language."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                text = "FAQ",
                color = Color(0xFF4C258C),
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        faqItems.forEach { (question, answer) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = question,
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        color = Color(0xFF4C258C)
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    Text(
                        text = answer,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF555555)
                    )
                }
            }
        }
    }
}
