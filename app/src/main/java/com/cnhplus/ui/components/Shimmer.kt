package com.cnhplus.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.Elevation
import com.cnhplus.ui.theme.Shapes

/**
 * Efeito Shimmer para estados de loading.
 */
@Composable
fun rememberShimmerBrush(
    shimmerColor: Color = Color.LightGray.copy(alpha = 0.6f),
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f)
): Brush {
    val shimmerWidth = 200f
    
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -shimmerWidth,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    return Brush.linearGradient(
        colors = listOf(
            backgroundColor,
            shimmerColor,
            backgroundColor
        ),
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + shimmerWidth, 0f)
    )
}

/**
 * Card com efeito shimmer para loading states.
 */
@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier,
    height: Float = 120f
) {
    val brush = rememberShimmerBrush()
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp),
        shape = Shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.small)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(brush)
        )
    }
}

/**
 * Layout completo de shimmer para uma tela.
 */
@Composable
fun ShimmerLoadingContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        // Header shimmer
        ShimmerCard(height = 80f)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content shimmers
        repeat(4) {
            ShimmerCard(height = 100f)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
