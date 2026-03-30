package com.elevasign.player.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.domain.usecase.SyncManifestUseCase
import com.elevasign.player.ui.pairing.PairingScreen
import com.elevasign.player.ui.player.PlayerScreen

sealed class Screen(val route: String) {
    object Pairing : Screen("pairing")
    object Player : Screen("player")
}

@Composable
fun ElevaSignNavGraph(
    startDestination: String,
    prefs: PlayerPreferences,
    syncManifest: SyncManifestUseCase,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Pairing.route) {
            PairingScreen(
                onPairingSuccess = {
                    navController.navigate(Screen.Player.route) {
                        popUpTo(Screen.Pairing.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Player.route) {
            PlayerScreen(
                prefs = prefs,
                syncManifest = syncManifest,
            )
        }
    }
}
