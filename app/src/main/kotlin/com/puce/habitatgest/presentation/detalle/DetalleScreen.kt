package com.puce.habitatgest.presentation.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.puce.habitatgest.domain.model.CondicionesAmbientales
import com.puce.habitatgest.domain.model.Espacio

// ── Stateful ─────────────────────────────────────────────────────────────────

@Composable
fun DetalleScreen(
    viewModel : DetalleViewModel,
    onVolver  : () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DetalleContent(
        uiState            = uiState,
        onVolver           = onVolver,
        onReintentar       = viewModel::cargarCondiciones,
    )
}

// ── Stateless ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleContent(
    uiState      : DetalleUiState,
    onVolver     : () -> Unit,
    onReintentar : () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.espacio?.nombre ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->

        if (uiState.cargandoEspacio) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        val espacio = uiState.espacio
        if (espacio == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) { Text("Espacio no encontrado.") }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            InfoEspacio(espacio)
            HorizontalDivider()
            CondicionesAmbientalesSection(
                state        = uiState.condiciones,
                onReintentar = onReintentar,
            )
        }
    }
}

@Composable
private fun InfoEspacio(espacio: Espacio) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            InfoRow("Tipo", espacio.tipo.label)
            InfoRow("Capacidad", "${espacio.capacidad} personas")
            InfoRow("Ubicación", espacio.ubicacion)
            InfoRow("Estado", if (espacio.disponible) "Disponible" else "No disponible")
            if (espacio.descripcion.isNotBlank()) {
                InfoRow("Descripción", espacio.descripcion)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun CondicionesAmbientalesSection(
    state        : RemoteState,
    onReintentar : () -> Unit,
) {
    Text("Condiciones ambientales — Campus PUCE Quito",
        style = MaterialTheme.typography.titleLarge)

    when (state) {
        is RemoteState.Idle    -> Unit
        is RemoteState.Loading -> CircularProgressIndicator()
        is RemoteState.Success -> CondicionesCard(state.data)
        is RemoteState.Error   -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                Button(onClick = onReintentar) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Text("Reintentar")
                }
            }
        }
    }
}

@Composable
private fun CondicionesCard(condiciones: CondicionesAmbientales) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            InfoRow("Temperatura", "%.1f °C".format(condiciones.temperaturaC))
            InfoRow("Humedad", "${condiciones.humedadPct} %")
            InfoRow("Viento", "%.1f km/h".format(condiciones.vientoKmH))
        }
    }
}
