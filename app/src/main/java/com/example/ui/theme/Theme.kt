package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val StaticDarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = GreenInk,
    background = Color(0xFF14181C),
    onBackground = Ink,
    surface = Color(0xFF1C2228),
    onSurface = Ink,
    surfaceVariant = Tile,
    onSurfaceVariant = Dim,
    outline = Line
)

@Composable
fun MyApplicationTheme(
    themeStyle: String = "classic",
    content: @Composable () -> Unit
) {
    val currentBg = if (themeStyle == "oled") Color.Black else Color(0xFF14181C)
    val currentBar = if (themeStyle == "oled") Color.Black else Color(0xFF10151A)
    val currentSurface = if (themeStyle == "oled") Color.Black else Color(0xFF1C2228)

    val colorScheme = StaticDarkColorScheme.copy(
        background = currentBg,
        surface = currentSurface
    )

    CompositionLocalProvider(
        LocalBg provides currentBg,
        LocalBar provides currentBar,
        LocalSurface provides currentSurface
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
