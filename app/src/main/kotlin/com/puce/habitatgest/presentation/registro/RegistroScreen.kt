package com.puce.habitatgest.presentation.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.puce.habitatgest.domain.model.TipoEspacio

// ── Stateful ─────────────────────────────────────────────────────────────────

@Composable
fun RegistroScreen(
    viewModel : RegistroViewModel,
    onGuardado: () -> Unit,
    onCancelar: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.guardado) {
        if (uiState.guardado) onGuardado()
    }

    RegistroContent(
        uiState           = uiState,
        onNombreChange    = viewModel::onNombreChange,
        onCapacidadChange = viewModel::onCapacidadChange,
        onTipoChange      = viewModel::onTipoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onUbicacionChange = viewModel::onUbicacionChange,
        onDisponibleChange = viewModel::onDisponibleChange,
        onGuardar         = viewModel::guardar,
        onCancelar        = onCancelar,
    )
}

// ── Stateless ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroContent(
    uiState             : RegistroUiState,
    onNombreChange      : (String) -> Unit,
    onCapacidadChange   : (String) -> Unit,
    onTipoChange        : (TipoEspacio) -> Unit,
    onDescripcionChange : (String) -> Unit,
    onUbicacionChange   : (String) -> Unit,
    onDisponibleChange  : (Boolean) -> Unit,
    onGuardar           : () -> Unit,
    onCancelar          : () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar espacio") },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->

        if (uiState.guardando) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value         = uiState.nombre,
                onValueChange = onNombreChange,
                label         = { Text("Nombre") },
                isError       = uiState.nombreError != null,
                supportingText = uiState.nombreError?.let { { Text(it) } },
                modifier      = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value           = uiState.capacidad,
                onValueChange   = onCapacidadChange,
                label           = { Text("Capacidad") },
                isError         = uiState.capacidadError != null,
                supportingText  = uiState.capacidadError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier        = Modifier.fillMaxWidth(),
            )

            TipoEspacioDropdown(
                selected = uiState.tipo,
                onSelected = onTipoChange,
            )

            OutlinedTextField(
                value         = uiState.descripcion,
                onValueChange = onDescripcionChange,
                label         = { Text("Descripción") },
                minLines      = 3,
                modifier      = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value         = uiState.ubicacion,
                onValueChange = onUbicacionChange,
                label         = { Text("Ubicación") },
                isError       = uiState.ubicacionError != null,
                supportingText = uiState.ubicacionError?.let { { Text(it) } },
                modifier      = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Disponible", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = uiState.disponible, onCheckedChange = onDisponibleChange)
            }

            uiState.errorGuardar?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            ) {
                TextButton(onClick = onCancelar) { Text("Cancelar") }
                Button(onClick = onGuardar)     { Text("Guardar") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipoEspacioDropdown(
    selected   : TipoEspacio,
    onSelected : (TipoEspacio) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded         = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value             = selected.label,
            onValueChange     = {},
            readOnly          = true,
            label             = { Text("Tipo de espacio") },
            trailingIcon      = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier          = Modifier.menuAnchor().fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
        ) {
            TipoEspacio.entries.forEach { tipo ->
                DropdownMenuItem(
                    text    = { Text(tipo.label) },
                    onClick = { onSelected(tipo); expanded = false },
                )
            }
        }
    }
}
