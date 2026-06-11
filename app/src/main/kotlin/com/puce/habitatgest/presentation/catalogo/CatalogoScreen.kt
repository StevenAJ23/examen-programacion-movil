package com.puce.habitatgest.presentation.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.puce.habitatgest.domain.model.Espacio

// ── Stateful ─────────────────────────────────────────────────────────────────

@Composable
fun CatalogoScreen(
    viewModel : CatalogoViewModel,
    onNuevo   : () -> Unit,
    onDetalle : (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CatalogoContent(uiState = uiState, onNuevo = onNuevo, onDetalle = onDetalle)
}

// ── Stateless ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoContent(
    uiState   : CatalogoUiState,
    onNuevo   : () -> Unit,
    onDetalle : (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Espacios — Facultad de Hábitat") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevo) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo espacio")
            }
        },
    ) { innerPadding ->

        if (uiState.cargando) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        if (uiState.espacios.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) { Text("No hay espacios registrados aún.") }
            return@Scaffold
        }

        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 12.dp),
        ) {
            items(
                items = uiState.espacios,
                key   = { espacio -> espacio.id },
            ) { espacio ->
                EspacioCard(espacio = espacio, onClick = { onDetalle(espacio.id) })
            }
        }
    }
}

@Composable
private fun EspacioCard(
    espacio : Espacio,
    onClick : () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(espacio.nombre, style = MaterialTheme.typography.titleLarge)
                Text(
                    "${espacio.tipo.label} · Capacidad: ${espacio.capacidad}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    espacio.ubicacion,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            Badge(
                containerColor = if (espacio.disponible)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer,
            ) {
                Text(if (espacio.disponible) "Disponible" else "No disponible")
            }
        }
    }
}
