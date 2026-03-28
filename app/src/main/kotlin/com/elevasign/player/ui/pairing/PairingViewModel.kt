package com.elevasign.player.ui.pairing

import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elevasign.player.BuildConfig
import com.elevasign.player.data.local.datastore.PlayerPreferences
import com.elevasign.player.data.remote.dto.RegisterRequest
import com.elevasign.player.data.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PairingState {
    object Idle : PairingState()
    object Loading : PairingState()
    object Success : PairingState()
    data class Error(val message: String) : PairingState()
}

@HiltViewModel
class PairingViewModel @Inject constructor(
    private val repository: PlayerRepository,
    private val prefs: PlayerPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow<PairingState>(PairingState.Idle)
    val state: StateFlow<PairingState> = _state.asStateFlow()

    fun pair(pairingCode: String) {
        if (pairingCode.length != 6) {
            _state.value = PairingState.Error("El codigo debe tener 6 digitos")
            return
        }

        viewModelScope.launch {
            _state.value = PairingState.Loading

            // Build a stable device ID from Android ID
            val deviceId = try {
                Settings.Secure.getString(
                    // We don't have context here; we'll use Build.SERIAL as fallback
                    null, Settings.Secure.ANDROID_ID
                ) ?: Build.SERIAL ?: "unknown-device"
            } catch (e: Exception) {
                Build.SERIAL ?: "unknown-device"
            }

            val request = RegisterRequest(
                pairingCode = pairingCode.trim(),
                deviceId = deviceId,
                appVersion = BuildConfig.VERSION_NAME,
                osVersion = "Android ${Build.VERSION.RELEASE}",
            )

            val result = repository.register(request)

            if (result.isSuccess) {
                val response = result.getOrThrow()
                prefs.saveRegistration(
                    screenId = response.screenId,
                    name = response.name,
                    layoutTemplate = response.layoutTemplate,
                )
                Log.i("PairingViewModel", "Paired successfully: screenId=${response.screenId}")
                _state.value = PairingState.Success
            } else {
                val error = result.exceptionOrNull()
                Log.e("PairingViewModel", "Pairing failed: ${error?.message}")
                _state.value = PairingState.Error(
                    when {
                        error?.message?.contains("404") == true ->
                            "Codigo invalido o expirado. Genera uno nuevo en el panel."
                        error?.message?.contains("UnknownHost") == true ||
                                error?.message?.contains("connect") == true ->
                            "Sin conexion. Verifica el WiFi."
                        else -> "Error de conexion. Intenta de nuevo."
                    }
                )
            }
        }
    }

    fun resetError() {
        _state.value = PairingState.Idle
    }
}
