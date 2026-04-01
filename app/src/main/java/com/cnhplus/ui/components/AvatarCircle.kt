package com.cnhplus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cnhplus.ui.theme.Secondary

@Composable
fun AvatarCircle(
    photoUrl: String?,
    userName: String = "User",
    size: Int = 40,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val sizeInDp = size.dp

    Box(
        modifier = modifier
            .size(sizeInDp)
            .clip(CircleShape)
            .background(Secondary)
            .border(2.dp, Secondary, CircleShape)
            .then(
                if (onClick != null) Modifier.clickable { onClick() }
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!photoUrl.isNullOrEmpty()) {
            AsyncImage(
                model = photoUrl,
                contentDescription = userName,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(sizeInDp),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = userName,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size((sizeInDp * 0.6f))
            )
        }
    }
}
