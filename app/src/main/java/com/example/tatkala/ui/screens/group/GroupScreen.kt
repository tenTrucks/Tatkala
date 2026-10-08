package com.example.tatkala.ui.screens.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tatkala.data.local.entity.TaskEntity
import com.example.tatkala.data.repository.TaskRepository

private enum class GroupFilter { UPCOMING, COMPLETED }

@Composable
fun GroupScreen(
    onEditTask: (Long) -> Unit
) {
    val tasks by TaskRepository.allTasksState().collectAsState()
    var filter by remember { mutableStateOf(GroupFilter.UPCOMING) }
    val collaborative = tasks
        .filter { it.isCollaborative }
        .filter { if (filter == GroupFilter.COMPLETED) it.isCompleted else !it.isCompleted }
        .sortedWith(compareBy<TaskEntity> { it.date }.thenBy { it.startTime })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Group",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Local collaborative schedules",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GroupFilter.entries.forEach {
                    FilterChip(
                        selected = filter == it,
                        onClick = { filter = it },
                        label = { Text(it.name.lowercase().replaceFirstChar { c -> c.titlecase() }) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        if (collaborative.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Groups, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No collaborative task", fontWeight = FontWeight.SemiBold)
                        Text(
                            "Create one from Add and choose Collaboration.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(collaborative, key = { it.id }) { task ->
                GroupTaskCard(task = task, onClick = { onEditTask(task.id) })
            }
        }
    }
}

@Composable
private fun GroupTaskCard(task: TaskEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Groups, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = task.title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.Edit,
                    contentDescription = if (task.isCompleted) "Completed" else "Edit collaborative task",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "${task.date} • ${task.startTime} • ${task.category}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Collaborators: ${task.collaborators.ifBlank { "No collaborator listed" }}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(if (task.isCompleted) "Completed" else "Pending") })
                if (task.meetingLink.isNotBlank()) AssistChip(onClick = {}, label = { Text("Meeting link") })
                if (task.location.isNotBlank()) AssistChip(onClick = {}, label = { Text("Location") })
            }
        }
    }
}
