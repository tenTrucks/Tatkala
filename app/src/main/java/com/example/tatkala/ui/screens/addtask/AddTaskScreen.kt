package com.example.tatkala.ui.screens.addtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.tatkala.data.local.entity.HabitEntity
import com.example.tatkala.data.local.entity.TaskEntity
import com.example.tatkala.data.repository.HabitRepository
import com.example.tatkala.data.repository.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

private enum class AddMode { TASK, HABIT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskId: Long? = null,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var mode by remember { mutableStateOf(AddMode.TASK) }
    var editingTask by remember { mutableStateOf<TaskEntity?>(null) }

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var startTime by remember { mutableStateOf(LocalTime.now().withSecond(0).withNano(0).toString().take(5)) }
    var duration by remember { mutableStateOf("60") }
    var category by remember { mutableStateOf("Study") }
    var categoryExpanded by remember { mutableStateOf(false) }
    var isCollaborative by remember { mutableStateOf(false) }
    var collaborators by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var habitName by remember { mutableStateOf("") }
    var habitCategory by remember { mutableStateOf("Health") }
    var targetPerWeek by remember { mutableStateOf("7") }
    var reminderTime by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            TaskRepository.getTask(taskId)?.let { task ->
                editingTask = task
                title = task.title
                date = task.date
                startTime = task.startTime
                duration = task.durationMinutes.toString()
                category = task.category
                isCollaborative = task.isCollaborative
                collaborators = task.collaborators
                link = task.meetingLink
                location = task.location
                description = task.description
                mode = AddMode.TASK
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editingTask == null) "Add" else "Edit Task")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (editingTask == null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AddMode.entries.forEach {
                        FilterChip(
                            selected = mode == it,
                            onClick = { mode = it },
                            label = { Text(it.name.lowercase().replaceFirstChar { c -> c.titlecase() }) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (mode == AddMode.TASK) {
                SectionTitle("Schedule Information")
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; error = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Task name") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PickerField(
                        value = date,
                        label = "Date",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val parsed = runCatching { LocalDate.parse(date) }.getOrDefault(LocalDate.now())
                            DatePickerDialog(
                                context,
                                { _, year, month, day -> date = LocalDate.of(year, month + 1, day).toString() },
                                parsed.year,
                                parsed.monthValue - 1,
                                parsed.dayOfMonth
                            ).show()
                        }
                    )
                    PickerField(
                        value = startTime,
                        label = "Start time",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val parsed = runCatching { LocalTime.parse(startTime) }.getOrDefault(LocalTime.of(9, 0))
                            TimePickerDialog(
                                context,
                                { _, hour, minute -> startTime = "%02d:%02d".format(hour, minute) },
                                parsed.hour,
                                parsed.minute,
                                true
                            ).show()
                        }
                    )
                }
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it.filter(Char::isDigit); error = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Duration minutes") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { categoryExpanded = true },
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        listOf("Study", "Project", "Meeting", "Personal", "Health", "Other").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    category = it
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
                SectionTitle("Schedule Type")
                Row {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isCollaborative = false }
                    ) {
                        RadioButton(selected = !isCollaborative, onClick = { isCollaborative = false })
                        Text("Personal", modifier = Modifier.padding(top = 12.dp))
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isCollaborative = true }
                    ) {
                        RadioButton(selected = isCollaborative, onClick = { isCollaborative = true })
                        Text("Collaboration", modifier = Modifier.padding(top = 12.dp))
                    }
                }
                if (isCollaborative) {
                    OutlinedTextField(
                        value = collaborators,
                        onValueChange = { collaborators = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Collaborators") },
                        placeholder = { Text("Dika, Sutan, Khalid") }
                    )
                }
                OutlinedTextField(
                    value = link,
                    onValueChange = { link = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Meeting link") }
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Location") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp),
                    label = { Text("Notes") }
                )
            } else {
                SectionTitle("Habit Information")
                OutlinedTextField(
                    value = habitName,
                    onValueChange = { habitName = it; error = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Habit name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = habitCategory,
                    onValueChange = { habitCategory = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Category") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = targetPerWeek,
                    onValueChange = { targetPerWeek = it.filter(Char::isDigit); error = "" },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Target per week") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                PickerField(
                    value = reminderTime.ifBlank { "Optional" },
                    label = "Reminder time",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hour, minute -> reminderTime = "%02d:%02d".format(hour, minute) },
                            8,
                            0,
                            true
                        ).show()
                    }
                )
            }

            if (error.isNotBlank()) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (mode == AddMode.TASK) {
                        val minutes = duration.toIntOrNull() ?: 0
                        when {
                            title.isBlank() -> error = "Task name is required."
                            date.isBlank() -> error = "Date is required."
                            startTime.isBlank() -> error = "Start time is required."
                            minutes <= 0 -> error = "Duration must be more than 0."
                            else -> scope.launch {
                                TaskRepository.upsertTask(
                                    TaskEntity(
                                        id = editingTask?.id ?: System.currentTimeMillis(),
                                        title = title.trim(),
                                        date = date,
                                        startTime = startTime,
                                        durationMinutes = minutes,
                                        category = category,
                                        description = description.trim(),
                                        isCollaborative = isCollaborative,
                                        collaborators = collaborators.trim(),
                                        meetingLink = link.trim(),
                                        location = location.trim(),
                                        isCompleted = editingTask?.isCompleted ?: false,
                                        createdAt = editingTask?.createdAt ?: System.currentTimeMillis()
                                    )
                                )
                                onSaved()
                            }
                        }
                    } else {
                        val target = targetPerWeek.toIntOrNull() ?: 0
                        when {
                            habitName.isBlank() -> error = "Habit name is required."
                            target <= 0 -> error = "Target must be more than 0."
                            else -> scope.launch {
                                HabitRepository.upsertHabit(
                                    HabitEntity(
                                        name = habitName.trim(),
                                        category = habitCategory.ifBlank { "General" },
                                        targetPerWeek = target.coerceAtMost(7),
                                        reminderTime = reminderTime.ifBlank { null }
                                    )
                                )
                                onSaved()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (mode == AddMode.TASK) "Save Schedule" else "Save Habit",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun PickerField(
    value: String,
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = modifier.clickable(onClick = onClick),
        label = { Text(label) },
        readOnly = true,
        enabled = false,
        trailingIcon = {
            TextButton(onClick = onClick) { Text("Pick") }
        },
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.primary
        )
    )
}
