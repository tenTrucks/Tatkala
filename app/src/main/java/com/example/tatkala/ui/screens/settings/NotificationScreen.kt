package com.example.tatkala.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Purple = Color(0xFF6D49AE)
private val DarkPurple = Color(0xFF4C258C)
private val Background = Color(0xFFF8F8F8)

@Composable
fun NotificationScreen(
    onBack: () -> Unit = {}
) {
    var pushNotificationEnabled by remember {
        mutableStateOf(true)
    }

    var taskReminderEnabled by remember {
        mutableStateOf(true)
    }

    var habitReminderEnabled by remember {
        mutableStateOf(true)
    }

    var collaborationNotificationEnabled by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Notification",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPurple
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    NotificationItem(
                        title = "Push Notifications",
                        description = "Receive notifications from Tatakala.",
                        checked = pushNotificationEnabled,
                        onCheckedChange = {
                            pushNotificationEnabled = it
                        }
                    )

                    HorizontalDivider()

                    NotificationItem(
                        title = "Task Reminder",
                        description = "Get reminded about upcoming tasks.",
                        checked = taskReminderEnabled,
                        onCheckedChange = {
                            taskReminderEnabled = it
                        }
                    )

                    HorizontalDivider()

                    NotificationItem(
                        title = "Habit Reminder",
                        description = "Get reminded to complete your habits.",
                        checked = habitReminderEnabled,
                        onCheckedChange = {
                            habitReminderEnabled = it
                        }
                    )

                    HorizontalDivider()

                    NotificationItem(
                        title = "Collaboration Notification",
                        description = "Receive updates from collaborations.",
                        checked = collaborationNotificationEnabled,
                        onCheckedChange = {
                            collaborationNotificationEnabled = it
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkPurple
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Purple
            )
        )
    }
}