package com.example.tatkala.data

import android.content.Context
import com.example.tatkala.data.repository.HabitRepository
import com.example.tatkala.data.repository.SettingsRepository
import com.example.tatkala.data.repository.TaskRepository
import com.example.tatkala.data.repository.UserRepository

object AppContainer {
    fun init(context: Context) {
        TaskRepository.init(context)
        HabitRepository.init(context)
        SettingsRepository.init(context)
        UserRepository.init(context)
    }
}
