package com.example.tatkala.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tatkala.data.repository.SettingsRepository
import com.example.tatkala.data.repository.UserRepository
import com.example.tatkala.ui.screens.addtask.AddTaskScreen
import com.example.tatkala.ui.screens.auth.login.LoginScreen
import com.example.tatkala.ui.screens.auth.register.RegisterScreen
import com.example.tatkala.ui.screens.auth.welcome.WelcomeScreen
import com.example.tatkala.ui.screens.group.GroupScreen
import com.example.tatkala.ui.screens.home.HomeScreen
import com.example.tatkala.ui.screens.progress.ProgressScreen
import com.example.tatkala.ui.screens.settings.AboutScreen
import com.example.tatkala.ui.screens.settings.FaqScreen
import com.example.tatkala.ui.screens.settings.HelpScreen
import com.example.tatkala.ui.screens.settings.LanguageScreen
import com.example.tatkala.ui.screens.settings.NotificationScreen
import com.example.tatkala.ui.screens.settings.PrivacyScreen
import com.example.tatkala.ui.screens.settings.RatingScreen
import com.example.tatkala.ui.screens.settings.SettingsScreen
import com.example.tatkala.ui.screens.settings.ThemeScreen
import com.example.tatkala.ui.screens.settings.profile.EditProfileScreen
import com.example.tatkala.ui.screens.settings.profile.ProfileScreen

private data class MainTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val mainTabs = listOf(
    MainTab(Routes.HOME, "Home", Icons.Default.Home),
    MainTab(Routes.PROGRESS, "Progress", Icons.Default.BarChart),
    MainTab(Routes.ADD_TASK, "Add", Icons.Default.AddCircle),
    MainTab(Routes.GROUP, "Group", Icons.Default.Groups),
    MainTab(Routes.SETTINGS, "Settings", Icons.Default.Settings)
)

@Composable
fun TatakalaNavigation(
    navController: NavHostController = rememberNavController()
) {
    val onboardingCompleted by SettingsRepository.onboardingCompleted.collectAsState()
    val loggedIn by UserRepository.loggedIn.collectAsState()
    val startDestination = when {
        !onboardingCompleted -> Routes.WELCOME
        loggedIn -> Routes.HOME
        else -> Routes.LOGIN
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route
    val showBottomBar = currentRoute in mainTabs.map { it.route } ||
        currentRoute == Routes.ADD_TASK_PATTERN

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                TatakalaBottomBar(
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.WELCOME) {
                WelcomeScreen(
                    onGetStarted = {
                        SettingsRepository.completeOnboarding()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.WELCOME) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate(Routes.REGISTER) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    },
                    onLoginClick = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onAddClick = { navController.navigate(Routes.addTask()) },
                    onEditTask = { taskId -> navController.navigate(Routes.addTask(taskId)) },
                    onEditHabit = { habitId -> navController.navigate(Routes.addHabit(habitId)) }
                )
            }

            composable(Routes.PROGRESS) { ProgressScreen() }

            composable(
                route = Routes.ADD_TASK_PATTERN,
                arguments = listOf(
                    navArgument("taskId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    },
                    navArgument("habitId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) { entry ->
                val taskId = entry.arguments?.getLong("taskId")?.takeIf { it > 0L }
                val habitId = entry.arguments?.getLong("habitId")?.takeIf { it > 0L }
                AddTaskScreen(
                    taskId = taskId,
                    habitId = habitId,
                    onBack = { navController.popBackStack() },
                    onSaved = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.ADD_TASK) {
                AddTaskScreen(
                    onBack = { navController.popBackStack() },
                    onSaved = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.GROUP) {
                GroupScreen(onEditTask = { navController.navigate(Routes.addTask(it)) })
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onProfileClick = { navController.navigate(Routes.PROFILE) },
                    onNotificationClick = { navController.navigate(Routes.NOTIFICATION) },
                    onLanguageClick = { navController.navigate(Routes.LANGUAGE) },
                    onThemeClick = { navController.navigate(Routes.THEME) },
                    onPrivacyClick = { navController.navigate(Routes.PRIVACY) },
                    onHelpClick = { navController.navigate(Routes.HELP) },
                    onFaqClick = { navController.navigate(Routes.FAQ) },
                    onRatingClick = { navController.navigate(Routes.RATING) },
                    onAboutClick = { navController.navigate(Routes.ABOUT) }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                    onLogout = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Routes.EDIT_PROFILE) {
                EditProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }

            composable(Routes.NOTIFICATION) { NotificationScreen(onBack = { navController.popBackStack() }) }
            composable(Routes.LANGUAGE) { LanguageScreen(onBackClick = { navController.popBackStack() }) }
            composable(Routes.THEME) { ThemeScreen(onBackClick = { navController.popBackStack() }) }
            composable(Routes.PRIVACY) {
                PrivacyScreen(
                    onBackClick = { navController.popBackStack() },
                    onFullReset = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Routes.HELP) { HelpScreen(onBackClick = { navController.popBackStack() }) }
            composable(Routes.FAQ) { FaqScreen(onBackClick = { navController.popBackStack() }) }
            composable(Routes.RATING) { RatingScreen(onBackClick = { navController.popBackStack() }) }
            composable(Routes.ABOUT) { AboutScreen(onBackClick = { navController.popBackStack() }) }
        }
    }
}

@Composable
private fun TatakalaBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        mainTabs.forEach { tab ->
            val selected = currentDestination?.route == tab.route ||
                (tab.route == Routes.ADD_TASK && currentDestination?.route == Routes.ADD_TASK_PATTERN)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(tab.route) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label
                    )
                },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
