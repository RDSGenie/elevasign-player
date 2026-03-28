package com.elevasign.player.ui.player

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import java.io.File

@Composable
fun ImageSlide(
    localPath: String?,
    remoteUrl: String?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val model = when {
        localPath != null && File(localPath).exists() -> File(localPath)
        remoteUrl != null -> remoteUrl
        else -> null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(
            targetState = model,
            animationSpec = tween(durationMillis = 500),
            label = "image_crossfade",
        ) { src ->
            if (src != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(src)
                        .size(Size.ORIGINAL)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
