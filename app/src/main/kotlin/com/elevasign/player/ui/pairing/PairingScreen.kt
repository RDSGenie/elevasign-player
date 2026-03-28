package com.elevasign.player.ui.pairing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PairingScreen(
    onPairingSuccess: () -> Unit,
    viewModel: PairingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var code by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is PairingState.Success) onPairingSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp),
        ) {
            // Logo / Title
            Text(
                text = "ElevaSign",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = Color.White,
                    fontSize = 48.sp,
                ),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pantalla Digital",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF9E9E9E)),
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Ingresa el codigo de emparejamiento",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFBDBDBD)),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = code,
                onValueChange = { new ->
                    if (new.length <= 6 && new.all { it.isDigit() }) {
                        code = new
                        viewModel.resetError()
                    }
                },
                label = { Text("Codigo de 6 digitos") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { if (code.length == 6) viewModel.pair(code) }
                ),
                modifier = Modifier.width(240.dp),
                enabled = state !is PairingState.Loading,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = state is PairingState.Error) {
                Text(
                    text = (state as? PairingState.Error)?.message ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is PairingState.Loading -> CircularProgressIndicator(color = Color.White)
                else -> Button(
                    onClick = { viewModel.pair(code) },
                    enabled = code.length == 6,
                ) {
                    Text("Emparejar pantalla")
                }
            }
        }
    }
}
