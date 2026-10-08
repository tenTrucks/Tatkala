package com.example.tatkala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tatkala.data.AppContainer
import com.example.tatkala.data.repository.SettingsRepository
import com.example.tatkala.navigation.TatakalaNavigation
import com.example.tatkala.ui.theme.TatakalaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContainer.init(applicationContext)

        setContent {
            val themeMode by SettingsRepository.themeMode.collectAsState()
            TatakalaTheme(themeMode = themeMode) {
                TatakalaNavigation()
            }
        }
    }
}
