package com.mahilashakti.unnati.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkBlue  = Color(0xFF1B3A6B)
private val MidBlue   = Color(0xFF2E5FA3)
private val LightBlue = Color(0xFFD0E4F7)

private val AppColorScheme = lightColorScheme(
    primary         = DarkBlue,
    onPrimary       = Color.White,
    primaryContainer = LightBlue,
    onPrimaryContainer = DarkBlue,
    secondary       = MidBlue,
    onSecondary     = Color.White,
    background      = Color(0xFFF5F7FA),
    surface         = Color.White,
    onBackground    = Color(0xFF1A1A1A),
    onSurface       = Color(0xFF1A1A1A)
)

@Composable
fun MahilaShaktiTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AppColorScheme, content = content)
}