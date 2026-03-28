package com.elevasign.player

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.domain.usecase.SyncManifestUseCase
import com.elevasign.player.ui.nav.ElevaSignNavGraph
import com.elevasign.player.ui.nav.Screen
import com.elevasign.player.ui.theme.ElevaSignTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var prefs: PlayerPreferences
    @Inject lateinit var syncManifest: SyncManifestUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fullscreen immersive mode
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            ElevaSignTheme {
                val screenId by prefs.screenId.collectAsState(initial = null)
                val startDestination = if (screenId != null) {
                    Screen.Player.route
                } else {
                    Screen.Pairing.route
                }
                ElevaSignNavGraph(startDestination = startDestination)
            }
        }

        // Trigger initial sync when screen is already registered
        lifecycleScope.launch {
            val screenId = prefs.screenId.first()
            if (screenId != null) {
                syncManifest()
            }
        }
    }
}
