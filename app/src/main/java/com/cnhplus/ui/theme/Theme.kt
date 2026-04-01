package com.cnhplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// CNH+ Colors (importados de Colors.kt)
// val Primary, Secondary, etc. já estão definidos em Colors.kt

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
