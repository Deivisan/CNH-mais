package com.cnhplus

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// CNH+ Colors
val Primary = Color(0xFF1E3A5F)
val PrimaryLight = Color(0xFF2E5A8F)
val Secondary = Color(0xFF4A90D9)
val Accent = Color(0xFF87CEEB)
val Background = Color(0xFFFFFFFF)
val Surface = Color(0xFFF5F7FA)
val TextPrimary = Color(0xFF0D1B2A)
val TextSecondary = Color(0xFF6B7280)
val Success = Color(0xFF10B981)
val Warning = Color(0xFFF59E0B)
val Error = Color(0xFFEF4444)

private val CNHLightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = Color.White,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = Accent,
    onSecondaryContainer = Primary,
    tertiary = Accent,
    onTertiary = Primary,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = Surface,
    onSurfaceVariant = TextSecondary,
    error = Error,
    onError = Color.White
)

@Composable
fun CNHMaisTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CNHLightColorScheme,
        content = content
    )
}
