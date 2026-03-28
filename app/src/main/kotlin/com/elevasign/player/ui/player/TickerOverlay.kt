package com.elevasign.player.ui.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevasign.player.domain.model.ActiveAnnouncement

@Composable
fun TickerOverlay(
    announcement: ActiveAnnouncement,
    modifier: Modifier = Modifier,
) {
    val bgColor = try {
        Color(android.graphics.Color.parseColor(announcement.bgColor))
    } catch (e: Exception) {
        Color(0xFF1A1A2E)
    }
    val textColor = try {
        Color(android.graphics.Color.parseColor(announcement.textColor))
    } catch (e: Exception) {
        Color.White
    }

    val text = if (announcement.title.isNotBlank() && announcement.body.isNotBlank()) {
        "${announcement.title}  •  ${announcement.body}"
    } else {
        announcement.title.ifBlank { announcement.body }
    }

    var containerWidthPx by remember { mutableStateOf(1920f) }
    var textWidthPx by remember { mutableStateOf(0f) }
    val density = LocalDensity.current

    val totalDistance = containerWidthPx + textWidthPx
    val durationMs = (totalDistance / 0.12f).toInt().coerceAtLeast(8000)

    val infiniteTransition = rememberInfiniteTransition(label = "ticker")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = containerWidthPx,
        targetValue = -textWidthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "ticker_x",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(bgColor.copy(alpha = 0.93f))
            .onGloballyPositioned { coords ->
                containerWidthPx = coords.size.width.toFloat()
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), 0) }
                .padding(vertical = 4.dp)
                .onGloballyPositioned { coords ->
                    textWidthPx = coords.size.width.toFloat()
                },
        )
    }
}
