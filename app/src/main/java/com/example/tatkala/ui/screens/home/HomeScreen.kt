package com.example.tatkala.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tatkala.data.repository.TaskRepository


import com.example.tatkala.ui.theme.TatakalaPurple
import com.example.tatkala.ui.theme.TatakalaDarkPurple
import com.example.tatkala.ui.theme.TatakalaLime
import com.example.tatkala.ui.theme.TatakalaLightGray
import com.example.tatkala.ui.theme.TatakalaGray
import com.example.tatkala.ui.theme.TatakalaTextPrimary


data class HomeTask(
    val title: String,
    val time: String,
    val category: String,
    val highlighted: Boolean = false
)

@Composable
fun HomeScreen(
    onProgressClick: () -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedView by remember {
        mutableStateOf("Today")
    }

    var selectedBottom by remember {
        mutableIntStateOf(0)
    }
    val tasks = TaskRepository.tasks.map { task ->

    HomeTask(
        title = task.title,
        time = if (task.duration.isNotBlank()) {
            "${task.startTime} • ${task.duration}"
        } else {
            task.startTime
        },
        category = task.category,
        highlighted = false
        )
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            HomeBottomBar(
                selectedIndex = selectedBottom,
                onHomeClick = {
                    selectedBottom = 0
                },
                onProgressClick = {
                    selectedBottom = 1
                    onProgressClick()
                },
                onAddClick = {
                    selectedBottom = 2
                    onAddClick()
                },
                onSettingsClick = {
                    selectedBottom = 4
                    onSettingsClick()
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {

            HomeHeader()

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                ),
                color = Color.White
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 20.dp
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
                        "Today" -> TodayView(tasks)
                        "Week" -> WeekView()
                        "Month" -> MonthView()
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TatakalaLime)
            .padding(
                horizontal = 20.dp,
                vertical = 22.dp
            )
    ) {

        Text(
            text = "Good Morning, Sutan",
            fontSize = 14.sp,
            color = TatakalaTextPrimary
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "28",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Light,
                    color = TatakalaDarkPurple
                )

                Text(
                    text = "September",
                    fontSize = 16.sp,
                    color = TatakalaDarkPurple
                )
            }

            Spacer(
                modifier = Modifier.width(28.dp)
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(54.dp)
                    .background(TatakalaDarkPurple)
            )

            Spacer(
                modifier = Modifier.width(28.dp)
            )

            Text(
                text = "Monday",
                fontSize = 28.sp,
                color = TatakalaDarkPurple
            )
        }
    }
}

@Composable
private fun CalendarSwitcher(
    selected: String,
    onSelected: (String) -> Unit
) {
    val items = listOf("Today", "Week", "Month")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { item ->

            val active = item == selected

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clickable {
                        onSelected(item)
                    },
                shape = RoundedCornerShape(20.dp),
                color = if (active) TatakalaPurple else TatakalaLightGray
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        color = if (active) Color.White else TatakalaDarkPurple
                    )
                }
            }
        }
    }
}



@Composable
private fun TodayView(
    tasks: List<HomeTask>
) {
    Column {

        Text(
            text = "Today's Tasks",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TatakalaDarkPurple
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        if (tasks.isEmpty()) {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = TatakalaLightGray
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "No schedule yet",
                        fontWeight = FontWeight.SemiBold,
                        color = TatakalaDarkPurple
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Add your first schedule from the Add menu.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task)
                }
            }
        }
    }
}

@Composable
private fun TaskCard(
    task: HomeTask
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (task.highlighted) TatakalaLime
                else TatakalaGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TatakalaDarkPurple
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = task.time,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = task.category,
                    fontSize = 12.sp,
                    color = TatakalaPurple
                )
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Task menu",
                tint = TatakalaPurple
            )
        }
    }
}

@Composable
private fun WeekView() {
    Column {

        Text(
            text = "This Week",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TatakalaDarkPurple
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            val days = listOf(
                "Mon",
                "Tue",
                "Wed",
                "Thu",
                "Fri",
                "Sat",
                "Sun"
            )

            days.forEachIndexed { index, day ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = day,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color =
                                    if (index == 0) TatakalaPurple
                                    else Color.Transparent,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "${28 + index}",
                            fontSize = 12.sp,
                            color =
                                if (index == 0) Color.White
                                else TatakalaTextPrimary
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Text(
            text = "3 scheduled activities",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun MonthView() {
    Column {

        Text(
            text = "September 2026",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TatakalaDarkPurple
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        repeat(5) { week ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                repeat(7) { day ->

                    val date = week * 7 + day + 1

                    if (date <= 30) {

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    color =
                                        if (date == 28) TatakalaPurple
                                        else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = date.toString(),
                                fontSize = 12.sp,
                                color =
                                    if (date == 28) Color.White
                                    else TatakalaTextPrimary
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
                modifier = Modifier.height(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "12 scheduled activities",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun HomeBottomBar(
    selectedIndex: Int,
    onHomeClick: () -> Unit,
    onProgressClick: () -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    NavigationBar(
        containerColor = TatakalaPurple
    ) {

        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = onHomeClick,
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text("Home")
            },
            colors = bottomNavColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = onProgressClick,
            icon = {
                Icon(
                    Icons.Default.BarChart,
                    contentDescription = "Progress"
                )
            },
            label = {
                Text("Progress")
            },
            colors = bottomNavColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = onAddClick,
            icon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Add",
                    modifier = Modifier.size(28.dp)
                )
            },
            label = {
                Text("Add")
            },
            colors = bottomNavColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = {},
            icon = {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = "Group"
                )
            },
            label = {
                Text("Group")
            },
            colors = bottomNavColors()
        )

        NavigationBarItem(
            selected = selectedIndex == 4,
            onClick = onSettingsClick,
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            },
            label = {
                Text("Settings")
            },
            colors = bottomNavColors()
        )
    }
}

@Composable
private fun bottomNavColors() =
    NavigationBarItemDefaults.colors(
        selectedIconColor = TatakalaLime,
        selectedTextColor = TatakalaLime,
        unselectedIconColor = Color(0xFFD4C9E8),
        unselectedTextColor = Color(0xFFD4C9E8),
        indicatorColor = TatakalaDarkPurple
    )


