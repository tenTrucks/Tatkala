package com.example.tatkala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TatkalaApp()
        }
    }
}

/*
 * ==========================================================
 * COLORS
 * ==========================================================
 */

val TatkalaPurple = Color(0xFF6D49AE)
val TatkalaDarkPurple = Color(0xFF4C258C)
val TatkalaLime = Color(0xFFE7FCA7)
val TatkalaGreen = Color(0xFF18B99A)

val TaskGreen = Color(0xFFE9FFA6)
val TaskGray = Color(0xFFE0E0E0)

val BackgroundWhite = Color(0xFFFDFDFD)

/*
 * ==========================================================
 * MODEL
 * ==========================================================
 */

data class Task(
    val id: Int,
    val title: String,
    val startTime: String,
    val endTime: String,
    val category: String,
    val description: String = "",
    val collaborative: Boolean = false
)

/*
 * ==========================================================
 * APP
 * ==========================================================
 */

@Composable
fun TatkalaApp() {

    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundWhite
        ) {
            HomeScreen()
        }
    }
}

/*
 * ==========================================================
 * HOME SCREEN
 * ==========================================================
 */

@Composable
fun HomeScreen() {

    var selectedView by remember {
        mutableStateOf("Today")
    }

    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }

    var selectedBottomMenu by remember {
        mutableIntStateOf(0)
    }

    val tasks = remember {
        listOf(
            Task(
                id = 1,
                title = "Mobile Computing",
                startTime = "08:00",
                endTime = "09:40",
                category = "Study",
                description = "Kuis dan review materi Android."
            ),

            Task(
                id = 2,
                title = "Kerjakan Project Tatkala",
                startTime = "13:00",
                endTime = "15:00",
                category = "Project",
                description = "Implementasi homepage menggunakan Kotlin."
            ),

            Task(
                id = 3,
                title = "Workout",
                startTime = "17:00",
                endTime = "18:00",
                category = "Habit",
                description = "Workout rutin."
            ),

            Task(
                id = 4,
                title = "Meeting Kelompok",
                startTime = "20:00",
                endTime = "21:00",
                category = "Collaboration",
                description = "Meeting kelompok Tatkala.",
                collaborative = true
            )
        )
    }

    Scaffold(

        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedBottomMenu,
                onSelected = {
                    selectedBottomMenu = it
                }
            )
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    // Nanti diarahkan ke AddTaskScreen
                },
                containerColor = TatkalaPurple,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            HomeHeader()

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp
                ),
                color = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 20.dp
                        )
                ) {

                    CalendarSwitcher(
                        selected = selectedView,
                        onSelected = {
                            selectedView = it
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    when (selectedView) {

                        "Today" -> {

                            Text(
                                text = "Today's Tasks",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TatkalaDarkPurple
                            )

                            Spacer(
                                modifier = Modifier.height(14.dp)
                            )

                            tasks.forEachIndexed { index, task ->

                                TaskCard(
                                    task = task,
                                    highlighted = index == 0,
                                    onClick = {
                                        selectedTask = task
                                    }
                                )

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )
                            }
                        }

                        "Week" -> {
                            WeeklyView(tasks)
                        }

                        "Month" -> {
                            MonthlyView(tasks)
                        }
                    }
                }
            }
        }
    }

    selectedTask?.let { task ->

        TaskDetailDialog(
            task = task,
            onDismiss = {
                selectedTask = null
            }
        )
    }
}

/*
 * ==========================================================
 * HEADER
 * ==========================================================
 */

@Composable
fun HomeHeader() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TatkalaLime)
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = "Good Morning, Sutan",
                    fontSize = 14.sp,
                    color = Color(0xFF222222)
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        Color(0xFFD7D7D7),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.DarkGray
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "28",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Light,
                    color = TatkalaDarkPurple
                )

                Text(
                    text = "September",
                    fontSize = 17.sp,
                    color = TatkalaDarkPurple
                )
            }

            Spacer(
                modifier = Modifier.width(32.dp)
            )

            Divider(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp),
                color = TatkalaDarkPurple
            )

            Spacer(
                modifier = Modifier.width(32.dp)
            )

            Text(
                text = "Monday",
                fontSize = 27.sp,
                color = TatkalaDarkPurple
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )
    }
}

/*
 * ==========================================================
 * TODAY / WEEK / MONTH
 * ==========================================================
 */

@Composable
fun CalendarSwitcher(
    selected: String,
    onSelected: (String) -> Unit
) {

    val choices = listOf(
        "Today",
        "Week",
        "Month"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        choices.forEach { item ->

            val active = selected == item

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .clickable {
                        onSelected(item)
                    },

                color = if (active)
                    TatkalaPurple
                else
                    Color(0xFFE4E4E4),

                contentColor = if (active)
                    Color.White
                else
                    TatkalaDarkPurple,

                shape = RoundedCornerShape(30.dp)
            ) {

                Box(
                    modifier = Modifier.padding(
                        vertical = 9.dp
                    ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = item,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/*
 * ==========================================================
 * TASK CARD
 * ==========================================================
 */

@Composable
fun TaskCard(
    task: Task,
    highlighted: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        colors = CardDefaults.cardColors(
            containerColor =
                if (highlighted)
                    TaskGreen
                else
                    TaskGray
        ),

        shape = RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = task.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TatkalaDarkPurple
                )

                Spacer(
                    modifier = Modifier.height(7.dp)
                )

                Text(
                    text = "${task.startTime} - ${task.endTime}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = task.category,
                    fontSize = 12.sp,
                    color = TatkalaPurple
                )
            }

            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More",
                tint = TatkalaPurple
            )
        }
    }
}

/*
 * ==========================================================
 * WEEK VIEW
 * ==========================================================
 */

@Composable
fun WeeklyView(tasks: List<Task>) {

    Column {

        Text(
            text = "This Week",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = TatkalaDarkPurple
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            listOf(
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Sat",
                "Sun"
            ).forEachIndexed { index, day ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = day,
                        fontSize = 12.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                if (index == 0)
                                    TatkalaPurple
                                else
                                    Color.Transparent,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "${28 + index}",
                            color =
                                if (index == 0)
                                    Color.White
                                else
                                    Color.DarkGray
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        tasks.forEach {

            TaskCard(
                task = it,
                highlighted = false,
                onClick = {}
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}

/*
 * ==========================================================
 * MONTH VIEW
 * ==========================================================
 */

@Composable
fun MonthlyView(tasks: List<Task>) {

    Column {

        Text(
            text = "September 2026",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TatkalaDarkPurple
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        repeat(5) { week ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                repeat(7) { day ->

                    val number = week * 7 + day + 1

                    if (number <= 30) {

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (number == 28)
                                        TatkalaPurple
                                    else
                                        Color.Transparent,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = number.toString(),
                                color =
                                    if (number == 28)
                                        Color.White
                                    else
                                        Color.DarkGray
                            )
                        }

                    } else {

                        Spacer(
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "${tasks.size} scheduled activities",
            color = Color.Gray
        )
    }
}

/*
 * ==========================================================
 * TASK DETAIL
 * ==========================================================
 */

@Composable
fun TaskDetailDialog(
    task: Task,
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = task.title,
                color = TatkalaPurple,
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                Text(
                    text = "Time"
                )

                Text(
                    text = "${task.startTime} - ${task.endTime}",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Category"
                )

                Text(
                    text = task.category,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = task.description
                )

                if (task.collaborative) {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "👥 Collaborative Task"
                    )
                }
            }
        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Close",
                    color = TatkalaPurple
                )
            }
        }
    )
}

/*
 * ==========================================================
 * BOTTOM NAVIGATION
 * ==========================================================
 */

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {

    NavigationBar(
        containerColor = TatkalaPurple
    ) {

        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = {
                onSelected(0)
            },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text("Home")
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = {
                onSelected(1)
            },
            icon = {
                Icon(
                    Icons.Default.BarChart,
                    contentDescription = "Progress"
                )
            },
            label = {
                Text("Progress")
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = {
                onSelected(2)
            },
            icon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Add"
                )
            },
            label = {
                Text("Add")
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = {
                onSelected(3)
            },
            icon = {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = "Group"
                )
            },
            label = {
                Text("Group")
            },
            colors = navColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 4,
            onClick = {
                onSelected(4)
            },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Settings"
                )
            },
            label = {
                Text("Settings")
            },
            colors = navColors()
        )
    }
}

@Composable
fun navColors() =
    NavigationBarItemDefaults.colors(

        selectedIconColor = TatkalaLime,
        selectedTextColor = TatkalaLime,

        unselectedIconColor = Color(0xFFD1C7E4),
        unselectedTextColor = Color(0xFFD1C7E4),

        indicatorColor = TatkalaDarkPurple
    )