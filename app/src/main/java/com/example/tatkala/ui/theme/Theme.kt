package com.example.tatkala.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import com.example.tatkala.data.repository.ThemeMode

private val TatakalaLightColors = lightColorScheme(
    primary = TatakalaPurple,
    onPrimary = TatakalaWhite,

    secondary = TatakalaLime,
    onSecondary = TatakalaDarkPurple,

    background = TatakalaBackground,
    onBackground = TatakalaTextPrimary,

    surface = TatakalaWhite,
    onSurface = TatakalaTextPrimary,

    surfaceVariant = TatakalaLightGray,
    onSurfaceVariant = TatakalaTextSecondary
)

private val TatakalaDarkColors = darkColorScheme(
    primary = TatakalaBrightPurple,
    onPrimary = TatakalaWhite,
    secondary = TatakalaLime,
    onSecondary = TatakalaDarkPurple,
    background = TatakalaDarkBackground,
    onBackground = TatakalaDarkOnSurface,
    surface = TatakalaDarkSurface,
    onSurface = TatakalaDarkOnSurface,
    surfaceVariant = TatakalaDarkElevated,
    onSurfaceVariant = TatakalaDarkMuted,
    outline = Color(0xFF655B78)
)

@Composable
fun TatakalaTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    MaterialTheme(
        colorScheme = if (darkTheme) TatakalaDarkColors else TatakalaLightColors,
        typography = Typography,
        content = content
    )
}
