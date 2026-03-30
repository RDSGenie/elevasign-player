package com.elevasign.player.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.domain.model.AnnouncementDisplayType
import com.elevasign.player.domain.usecase.SyncManifestUseCase

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    prefs: PlayerPreferences,
    syncManifest: SyncManifestUseCase,
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSettingsMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        // --- Main content layer ---
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
                    text = "Sin contenido asignado.\nConfigure una playlist desde el panel de administración.",
                    color = Color.White.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp),
                )
            }

            uiState.isMultiZone && uiState.zones.isNotEmpty() -> {
                // Multi-zone layout: render each zone at its position
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val totalWidth = constraints.maxWidth
                    val totalHeight = constraints.maxHeight
                    for (zone in uiState.zones) {
                        val item = zone.currentItem ?: continue
                        val xPx = (zone.positionXPercent / 100f * totalWidth).toInt()
                        val yPx = (zone.positionYPercent / 100f * totalHeight).toInt()
                        val wPx = (zone.widthPercent / 100f * totalWidth).toInt()
                        val hPx = (zone.heightPercent / 100f * totalHeight).toInt()

                        val density = LocalDensity.current
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = with(density) { xPx.toDp() },
                                    y = with(density) { yPx.toDp() },
                                )
                                .width(with(density) { wPx.toDp() })
                                .height(with(density) { hPx.toDp() })
                                .background(Color.Black),
                        ) {
                            androidx.compose.runtime.key(zone.playbackGeneration) {
                                if (item.isVideo) {
                                    VideoPlayer(
                                        localPath = item.localPath,
                                        remoteUrl = item.fileUrl,
                                        onVideoEnded = { /* zone loops handle this via timer */ },
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
                        }
                    }
                }
            }

            else -> {
                // Single-zone: fullscreen playback
                val item = uiState.currentItem
                if (item != null) {
                    androidx.compose.runtime.key(uiState.playbackGeneration) {
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
                }
            }
        }

        // --- Announcement overlays — always rendered on top regardless of playlist state ---
        val announcements = uiState.announcements
        if (announcements.isNotEmpty()) {
            val fullscreen = announcements.firstOrNull {
                it.displayType == AnnouncementDisplayType.FULLSCREEN
            }
            val overlay = announcements.firstOrNull {
                it.displayType == AnnouncementDisplayType.OVERLAY
            }
            val ticker = announcements.firstOrNull {
                it.displayType == AnnouncementDisplayType.TICKER
            }

            if (fullscreen != null) {
                AnnouncementOverlay(announcement = fullscreen, modifier = Modifier.fillMaxSize())
            } else {
                if (overlay != null) {
                    AnnouncementOverlay(announcement = overlay, modifier = Modifier.fillMaxSize())
                }
                if (ticker != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        TickerOverlay(announcement = ticker)
                    }
                }
            }
        }

        // --- Settings menu overlay ---
        if (showSettingsMenu) {
            SettingsMenuOverlay(
                prefs = prefs,
                syncManifest = syncManifest,
                onDismiss = { showSettingsMenu = false },
            )
        }
    }

    // BACK button toggles settings menu (single press opens, second press closes)
    BackHandler {
        showSettingsMenu = !showSettingsMenu
    }
}
