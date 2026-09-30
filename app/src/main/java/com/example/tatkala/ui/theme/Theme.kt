package com.example.tatkala.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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

@Composable
fun TatakalaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TatakalaLightColors,
        typography = Typography,
        content = content
    )
}