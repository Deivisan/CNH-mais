package com.cnhplus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Design System CNH+ — Elevações, Gradientes, Shapes, Animações
 * Cores estão em Colors.kt (Primary, Secondary, Accent, etc.)
 */

// ==================== GRADIENTES ====================
val GradientPrimary = Brush.verticalGradient(
    colors = listOf(Primary, PrimaryLight, Secondary)
)

val GradientSurface = Brush.verticalGradient(
    colors = listOf(
        Color.White,
        SurfaceColor,
        SurfaceColor.copy(alpha = 0.95f)
    )
)

val GradientAccent = Brush.horizontalGradient(
    colors = listOf(Secondary, Accent)
)

// ==================== ELEVAÇÕES/SHADOWS ====================
object Elevation {
    val none = 0.dp
    val small = 4.dp
    val medium = 8.dp
    val large = 16.dp
    val xlarge = 24.dp
}

// ==================== SHAPES ====================
object Shapes {
    val none = RoundedCornerShape(0.dp)
    val small = RoundedCornerShape(8.dp)
    val medium = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(16.dp)
    val xlarge = RoundedCornerShape(24.dp)
    val topRounded = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val bottomRounded = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
}

// ==================== ANIMAÇÕES (ms) ====================
object Animation {
    const val fast = 150
    const val normal = 300
    const val slow = 500
    const val emphasis = 800
}

// ==================== ESPAÇAMENTO ====================
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}
