package com.cnhplus.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.Elevation
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Shapes

/**
 * Card premium do CNH+ com elevação e gradientes.
 */
@Composable
fun CnhCard(
    modifier: Modifier = Modifier,
    elevation: Dp = Elevation.medium,
    shape: Shape = Shapes.large,
    gradient: Brush? = null,
    containerColor: Color = Color.White,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = modifier
        .shadow(
            elevation = elevation,
            shape = shape,
            spotColor = Primary.copy(alpha = 0.08f)
        )
        .then(
            if (onClick != null) {
                Modifier.clickable(onClick = onClick)
            } else Modifier
        )
        .animateContentSize()

    Card(
        modifier = cardModifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (gradient == null) containerColor else Color.Transparent
        )
    ) {
        if (gradient != null) {
            Box(
                modifier = Modifier.background(gradient),
                contentAlignment = Alignment.Center
            ) {
                Column(content = content)
            }
        } else {
            Column(content = content)
        }
    }
}

/**
 * Card com estado de loading (shimmer placeholder).
 */
@Composable
fun CnhCardLoading(
    modifier: Modifier = Modifier,
    height: Dp = 120.dp
) {
    Card(
        modifier = modifier
            .shadow(elevation = Elevation.small, shape = Shapes.large)
            .background(Color.LightGray.copy(alpha = 0.3f)),
        shape = Shapes.large
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
        )
    }
}
