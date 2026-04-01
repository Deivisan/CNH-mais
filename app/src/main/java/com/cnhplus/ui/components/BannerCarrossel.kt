package com.cnhplus.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cnhplus.data.BannerDto
import com.cnhplus.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
fun BannerCarrossel(
    banners: List<BannerDto>,
    onBannerClick: (BannerDto) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) {
        return
    }

    val pagerState = rememberPagerState(pageCount = { banners.size })
    val scope = rememberCoroutineScope()
    var isDismissed by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = !isDismissed,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(modifier = modifier) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Primary)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val banner = banners[page]
                    BannerPage(
                        banner = banner,
                        onClick = { onBannerClick(banner) },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Close button
                IconButton(
                    onClick = { isDismissed = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Fechar banner",
                        tint = MaterialTheme.colorScheme.surface
                    )
                }

                // Indicators
                if (banners.size > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(banners.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (index == pagerState.currentPage)
                                            MaterialTheme.colorScheme.surface
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Auto-scroll
    LaunchedEffect(banners.size) {
        while (true) {
            kotlinx.coroutines.delay(5000)
            scope.launch {
                pagerState.animateScrollToPage(
                    (pagerState.currentPage + 1) % banners.size
                )
            }
        }
    }
}

@Composable
private fun BannerPage(
    banner: BannerDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        // Background image
        if (!banner.imagem_url.isNullOrEmpty()) {
            AsyncImage(
                model = banner.imagem_url,
                contentDescription = banner.titulo,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.3f))
        )

        // Text content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = banner.titulo,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.surface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (!banner.descricao.isNullOrEmpty()) {
                Text(
                    text = banner.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.surface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
