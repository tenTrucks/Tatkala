package com.example.tatkala.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tatkala.ui.screens.home.HomeScreen
import com.example.tatkala.ui.screens.addtask.AddTaskScreen
import androidx.compose.ui.platform.LocalContext
import com.example.tatkala.data.repository.TaskRepository

// Auth
import com.example.tatkala.ui.screens.auth.login.LoginScreen
import com.example.tatkala.ui.screens.auth.register.RegisterScreen

// Progress
import com.example.tatkala.ui.screens.progress.ProgressScreen

// Settings
import com.example.tatkala.ui.screens.settings.SettingsScreen
import com.example.tatkala.ui.screens.settings.NotificationScreen
import com.example.tatkala.ui.screens.settings.LanguageScreen
import com.example.tatkala.ui.screens.settings.ThemeScreen
import com.example.tatkala.ui.screens.settings.PrivacyScreen

// Profile
import com.example.tatkala.ui.screens.settings.profile.ProfileScreen
import com.example.tatkala.ui.screens.settings.profile.EditProfileScreen


@Composable
fun TatakalaNavigation(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,

        // sementara start dari login
        startDestination = Routes.LOGIN
    ) {

        /*
         * AUTH
         */

        composable(Routes.LOGIN) {

            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },

                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }


        composable(Routes.REGISTER) {

            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) {
                            inclusive = true
                        }
                    }
                },

                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }


        /*
         * HOME
         *
         * Masih placeholder.
         * Nanti kita ganti dengan HomeScreen()
         */
        composable(Routes.HOME) {

            HomeScreen(
                onProgressClick = {
                    navController.navigate(Routes.PROGRESS)
                },

                onAddClick = {
                    navController.navigate(Routes.ADD_TASK)
                },

                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }
        


        /*
         * PROGRESS
         */

        composable(Routes.PROGRESS) {

            ProgressScreen()
        }


        /*
         * ADD TASK
         *
         * Masih placeholder.
         */

        composable(Routes.ADD_TASK) {

            val context = LocalContext.current

            AddTaskScreen(
                onBack = {
                    navController.popBackStack()
                },

                onSave = { task ->

                    TaskRepository.addTask(
                        context = context,
                        task = task
                    )

                    navController.popBackStack()
                }
            )
        }

        


        /*
         * SETTINGS
         */

        composable(Routes.SETTINGS) {

            SettingsScreen(

                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                },

                onNotificationClick = {
                    navController.navigate(Routes.NOTIFICATION)
                },

                onLanguageClick = {
                    navController.navigate(Routes.LANGUAGE)
                },

                onThemeClick = {
                    navController.navigate(Routes.THEME)
                },

                onPrivacyClick = {
                    navController.navigate(Routes.PRIVACY)
                }
            )
        }


        /*
         * PROFILE
         */

        composable(Routes.PROFILE) {

            ProfileScreen(

                onBack = {
                    navController.popBackStack()
                },

                onEditProfile = {
                    navController.navigate(Routes.EDIT_PROFILE)
                }
            )
        }


        composable(Routes.EDIT_PROFILE) {

            EditProfileScreen(

                onBack = {
                    navController.popBackStack()
                },

                onSave = {
                    navController.popBackStack()
                }
            )
        }


        /*
         * NOTIFICATION
         */

        composable(Routes.NOTIFICATION) {

            NotificationScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        /*
         * OTHER SETTINGS
         */

        composable(Routes.LANGUAGE) {

            LanguageScreen()
        }


        composable(Routes.THEME) {

            ThemeScreen()
        }


        composable(Routes.PRIVACY) {

            PrivacyScreen()
        }
    }
}


/*
 * =========================================================
 * TEMPORARY SCREENS
 *
 * Kita pakai dulu supaya navigation bisa dites
 * sebelum HomeScreen dan AddTaskScreen milik Garis selesai.
 * =========================================================
 */

@Composable
private fun TemporaryHomeScreen(
    onProgressClick: () -> Unit,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Tatakala Home")

        Button(
            onClick = onProgressClick
        ) {
            Text("Progress")
        }

        Button(
            onClick = onAddClick
        ) {
            Text("Add Schedule")
        }

        Button(
            onClick = onSettingsClick
        ) {
            Text("Settings")
        }
    }
}


@Composable
private fun TemporaryAddTaskScreen(
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Add Schedule")

        Button(
            onClick = onBack
        ) {
            Text("Back")
        }
    }
}