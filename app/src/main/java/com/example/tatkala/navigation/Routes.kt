package com.example.tatkala.navigation

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"

    const val HOME = "home"
    const val PROGRESS = "progress"
    const val ADD_TASK = "add_task"
    const val GROUP = "group"

    const val SETTINGS = "settings"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val NOTIFICATION = "notification"

    const val LANGUAGE = "language"
    const val THEME = "theme"
    const val PRIVACY = "privacy"
    const val HELP = "help"
    const val FAQ = "faq"
    const val RATING = "rating"
    const val ABOUT = "about"

    const val ADD_TASK_PATTERN = "add_task?taskId={taskId}"

    fun addTask(taskId: Long? = null): String =
        if (taskId == null) ADD_TASK else "add_task?taskId=$taskId"
}
