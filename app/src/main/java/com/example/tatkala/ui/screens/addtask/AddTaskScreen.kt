package com.example.tatkala.ui.screens.addtask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tatkala.model.Task

import com.example.tatkala.ui.theme.TatakalaPurple
import com.example.tatkala.ui.theme.TatakalaDarkPurple
import com.example.tatkala.ui.theme.TatakalaLime
import com.example.tatkala.ui.theme.TatakalaBackground



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onBack: () -> Unit,
    onSave: (Task) -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    var isCollaborative by remember { mutableStateOf(false) }
    var collaborators by remember { mutableStateOf("") }

    var category by remember { mutableStateOf("Study") }
    var categoryExpanded by remember { mutableStateOf(false) }

    var link by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val categories = listOf(
        "Study",
        "Project",
        "Habit",
        "Meeting",
        "Personal",
        "Other"
    )

    Scaffold(
        containerColor = TatakalaBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Schedule",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    Text(
                        text = "←",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onBack()
                            },
                        fontSize = 24.sp,
                        color = TatakalaPurple
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TatakalaDarkPurple
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Text(
                text = "Schedule Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TatakalaDarkPurple
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OutlinedTextField(
                value = taskName,
                onValueChange = {
                    taskName = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Task Name")
                },
                placeholder = {
                    Text("e.g. Mobile Computing Class")
                },
                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = date,
                onValueChange = {
                    date = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Date")
                },
                placeholder = {
                    Text("DD/MM/YYYY")
                },
                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = startTime,
                    onValueChange = {
                        startTime = it
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Start Time")
                    },
                    placeholder = {
                        Text("08:00")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = {
                        duration = it
                    },
                    modifier = Modifier.weight(1f),
                    label = {
                        Text("Duration")
                    },
                    placeholder = {
                        Text("90 min")
                    },
                    singleLine = true
                )
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Text(
                text = "Schedule Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TatakalaDarkPurple
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            isCollaborative = false
                        }
                ) {
                    RadioButton(
                        selected = !isCollaborative,
                        onClick = {
                            isCollaborative = false
                        }
                    )

                    Text(
                        text = "Personal",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            isCollaborative = true
                        }
                ) {
                    RadioButton(
                        selected = isCollaborative,
                        onClick = {
                            isCollaborative = true
                        }
                    )

                    Text(
                        text = "Collaboration",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            if (isCollaborative) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = collaborators,
                    onValueChange = {
                        collaborators = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Collaborators")
                    },
                    placeholder = {
                        Text("e.g. Dika, Sutan, Khalid")
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Category",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TatakalaDarkPurple
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        categoryExpanded = true
                    },
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {

                Text(
                    text = category,
                    modifier = Modifier.padding(16.dp),
                    color = TatakalaDarkPurple
                )

                DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = {
                        categoryExpanded = false
                    }
                ) {

                    categories.forEach { item ->

                        DropdownMenuItem(
                            text = {
                                Text(item)
                            },
                            onClick = {
                                category = item
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            OutlinedTextField(
                value = link,
                onValueChange = {
                    link = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Meeting Link")
                },
                placeholder = {
                    Text("Zoom / Google Meet / other")
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Location")
                },
                placeholder = {
                    Text("Room, building, or Maps location")
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = {
                    Text("Notes")
                },
                placeholder = {
                    Text("Additional information...")
                }
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                onClick = {

                val task = Task(
                    title = taskName,
                    date = date,
                    startTime = startTime,
                    duration = duration,
                    category = category,
                    isCollaborative = isCollaborative,
                    collaborators = collaborators,
                    meetingLink = link,
                    location = location,
                    description = description
                )

                    onSave(task)
                }
                ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TatakalaPurple,
                    contentColor = Color.White
                ),
                enabled = taskName.isNotBlank()
            ) {

                Text(
                    text = "Save Schedule",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }
    }
}


