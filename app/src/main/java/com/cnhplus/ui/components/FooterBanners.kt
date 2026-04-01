package com.cnhplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cnhplus.ui.theme.Accent
import com.cnhplus.ui.theme.Primary
import com.cnhplus.ui.theme.Secondary

@Composable
fun FooterBanners(
    onGasolinaClick: (() -> Unit)? = null,
    onSeguroClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Gasolina Banner
        FooterBannerItem(
            icon = Icons.Filled.LocalGasStation,
            title = "Desconto Gasolina",
            description = "Postos parceiros",
            backgroundColor = Primary,
            onClick = onGasolinaClick,
            modifier = Modifier.weight(1f)
        )

        // Seguro Banner
        FooterBannerItem(
            icon = Icons.Filled.Shield,
            title = "Seguro Veículo",
            description = "Com desconto",
            backgroundColor = Secondary,
            onClick = onSeguroClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FooterBannerItem(
    icon: ImageVector,
    title: String,
    description: String,
    backgroundColor: androidx.compose.ui.graphics.Color,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            )
            .padding(12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.surface,
                    maxLines = 1
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                    maxLines = 1
                )
            }
        }
    }
}
