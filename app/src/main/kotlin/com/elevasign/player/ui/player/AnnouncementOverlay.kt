package com.elevasign.player.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevasign.player.domain.model.ActiveAnnouncement
import com.elevasign.player.domain.model.AnnouncementDisplayType

@Composable
fun AnnouncementOverlay(
    announcement: ActiveAnnouncement,
    modifier: Modifier = Modifier,
) {
    when (announcement.displayType) {
        AnnouncementDisplayType.FULLSCREEN -> FullscreenAnnouncement(announcement, modifier)
        AnnouncementDisplayType.OVERLAY -> OverlayAnnouncement(announcement, modifier)
        AnnouncementDisplayType.TICKER -> { /* handled by TickerOverlay */ }
    }
}

@Composable
private fun FullscreenAnnouncement(
    announcement: ActiveAnnouncement,
    modifier: Modifier = Modifier,
) {
    val bgColor = parseColor(announcement.bgColor, Color(0xFFDC2626))
    val textColor = parseColor(announcement.textColor, Color.White)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(48.dp),
        ) {
            Text(
                text = announcement.title,
                color = textColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            if (announcement.body.isNotBlank()) {
                Text(
                    text = announcement.body,
                    color = textColor.copy(alpha = 0.9f),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun OverlayAnnouncement(
    announcement: ActiveAnnouncement,
    modifier: Modifier = Modifier,
) {
    val bgColor = parseColor(announcement.bgColor, Color(0xFFDC2626))
    val textColor = parseColor(announcement.textColor, Color.White)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(bgColor.copy(alpha = 0.92f))
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Text(
                text = announcement.title,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            if (announcement.body.isNotBlank()) {
                Text(
                    text = announcement.body,
                    color = textColor.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                )
            }
        }
    }
}

private fun parseColor(hex: String, fallback: Color): Color {
    return try {
        val clean = hex.trimStart('#')
        val argb = when (clean.length) {
            6 -> "FF$clean"
            8 -> clean
            else -> return fallback
        }
        Color(android.graphics.Color.parseColor("#$argb"))
    } catch (e: Exception) {
        fallback
    }
}
