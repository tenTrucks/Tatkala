package com.example.tatkala

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        requestNotificationPermissionIfNeeded()

        setContent {
            val themeMode by SettingsRepository.themeMode.collectAsState()
            TatakalaTheme(themeMode = themeMode) {
                TatakalaNavigation()
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST
            )
        }
    }

    private companion object {
        const val NOTIFICATION_PERMISSION_REQUEST = 1401
    }
}
