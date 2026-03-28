package com.elevasign.player.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elevasign.player.domain.model.AnnouncementDisplayType

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        when {
            uiState.isLoading -> {
                Text(
                    text = "Cargando contenido\u2026",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            uiState.isEmpty -> {
                Text(
                    text = "Sin contenido asignado.\nCrea una playlist en el panel admin.",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp),
                )
            }

            else -> {
                val item = uiState.currentItem

                if (item != null) {
                    // --- Main media content ---
                    if (item.isVideo) {
                        VideoPlayer(
                            localPath = item.localPath,
                            remoteUrl = item.fileUrl,
                            onVideoEnded = { viewModel.onVideoEnded() },
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        ImageSlide(
                            localPath = item.localPath,
                            remoteUrl = item.fileUrl,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                // --- Announcement overlays ---
                val announcements = uiState.announcements
                val fullscreen = announcements.firstOrNull {
                    it.displayType == AnnouncementDisplayType.FULLSCREEN
                }
                val overlay = announcements.firstOrNull {
                    it.displayType == AnnouncementDisplayType.OVERLAY
                }
                val ticker = announcements.firstOrNull {
                    it.displayType == AnnouncementDisplayType.TICKER
                }

                // Fullscreen announcement takes over entire screen
                if (fullscreen != null) {
                    AnnouncementOverlay(announcement = fullscreen, modifier = Modifier.fillMaxSize())
                } else {
                    // Overlay (bottom banner) on top of content
                    if (overlay != null) {
                        AnnouncementOverlay(announcement = overlay, modifier = Modifier.fillMaxSize())
                    }
                    // Ticker at the very bottom
                    if (ticker != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 0.dp),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            TickerOverlay(announcement = ticker)
                        }
                    }
                }
            }
        }
    }
}
