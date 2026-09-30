package com.example.tatkala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.tatkala.navigation.TatakalaNavigation
import com.example.tatkala.ui.theme.TatakalaTheme
import com.example.tatkala.data.repository.TaskRepository

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TaskRepository.init(applicationContext)

        setContent {
            TatakalaTheme {
                TatakalaNavigation()
            }
        }
    }
}