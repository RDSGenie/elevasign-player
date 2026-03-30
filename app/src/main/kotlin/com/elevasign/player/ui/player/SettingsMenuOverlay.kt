package com.elevasign.player.ui.player

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elevasign.player.BuildConfig
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.domain.usecase.SyncManifestUseCase
import kotlinx.coroutines.launch

@Composable
fun SettingsMenuOverlay(
    prefs: PlayerPreferences,
    syncManifest: SyncManifestUseCase,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as? Activity
    val screenId by prefs.screenId.collectAsState(initial = "—")
    val screenName by prefs.screenName.collectAsState(initial = "—")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .width(420.dp)
                .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp))
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Header
            Text(
                text = "ElevaSign Player",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )

            HorizontalDivider(color = Color(0xFF444444))

            // Device Info
            InfoRow("Screen Name", screenName ?: "—")
            InfoRow("Screen ID", screenId?.take(16) ?: "—")
            InfoRow("App Version", "v${BuildConfig.VERSION_NAME}")

            HorizontalDivider(color = Color(0xFF444444))

            Spacer(modifier = Modifier.height(4.dp))

            // Force Sync
            Button(
                onClick = {
                    scope.launch {
                        syncManifest()
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0EA5E9),
                ),
            ) {
                Text("Force Sync Now", color = Color.White)
            }

            // Exit App
            Button(
                onClick = { activity?.finishAffinity() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626),
                ),
            ) {
                Text("Exit App", color = Color.White)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Dismiss hint
            Text(
                text = "Press BACK to close this menu",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF888888),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            color = Color(0xFFAAAAAA),
            fontSize = 13.sp,
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
        )
    }
}
