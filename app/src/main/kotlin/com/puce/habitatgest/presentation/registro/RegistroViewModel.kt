package com.puce.habitatgest.presentation.registro

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.puce.habitatgest.domain.model.Espacio
import com.puce.habitatgest.domain.model.TipoEspacio
import com.puce.habitatgest.domain.usecase.SaveEspacioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val saveEspacio: SaveEspacioUseCase,
    private val savedState: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RegistroUiState(
            nombre      = savedState["nombre"]      ?: "",
            capacidad   = savedState["capacidad"]   ?: "",
            descripcion = savedState["descripcion"] ?: "",
            ubicacion   = savedState["ubicacion"]   ?: "",
        ),
    )
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    // ── Actualización de campos ───────────────────────────────────────────────

    fun onNombreChange(v: String) {
        savedState["nombre"] = v
        _uiState.update { it.copy(nombre = v, nombreError = null) }
    }

    fun onCapacidadChange(v: String) {
        if (v.all { c -> c.isDigit() } || v.isEmpty()) {
            savedState["capacidad"] = v
            _uiState.update { it.copy(capacidad = v, capacidadError = null) }
        }
    }

    fun onTipoChange(v: TipoEspacio) = _uiState.update { it.copy(tipo = v) }

    fun onDescripcionChange(v: String) {
        savedState["descripcion"] = v
        _uiState.update { it.copy(descripcion = v) }
    }

    fun onUbicacionChange(v: String) {
        savedState["ubicacion"] = v
        _uiState.update { it.copy(ubicacion = v, ubicacionError = null) }
    }

    fun onDisponibleChange(v: Boolean) = _uiState.update { it.copy(disponible = v) }

    // ── Guardar ───────────────────────────────────────────────────────────────

    fun guardar() {
        if (!validar()) return
        val s = _uiState.value
        _uiState.update { it.copy(guardando = true, errorGuardar = null) }
        viewModelScope.launch {
            runCatching {
                saveEspacio(
                    Espacio(
                        nombre      = s.nombre.trim(),
                        capacidad   = s.capacidad.toInt(),
                        tipo        = s.tipo,
                        descripcion = s.descripcion.trim(),
                        disponible  = s.disponible,
                        ubicacion   = s.ubicacion.trim(),
                    ),
                )
            }.onSuccess {
                _uiState.update { it.copy(guardando = false, guardado = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(guardando = false, errorGuardar = e.message) }
            }
        }
    }

    private fun validar(): Boolean {
        val s = _uiState.value
        var ok = true
        if (s.nombre.isBlank()) {
            _uiState.update { it.copy(nombreError = "El nombre es obligatorio") }
            ok = false
        }
        if (s.capacidad.isBlank() || s.capacidad.toIntOrNull() == null) {
            _uiState.update { it.copy(capacidadError = "Ingresa un número válido") }
            ok = false
        }
        if (s.ubicacion.isBlank()) {
            _uiState.update { it.copy(ubicacionError = "La ubicación es obligatoria") }
            ok = false
        }
        return ok
    }

    // ── Factory ───────────────────────────────────────────────────────────────

    companion object {
        fun factory(saveEspacio: SaveEspacioUseCase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T = RegistroViewModel(
                    saveEspacio = saveEspacio,
                    savedState  = extras.createSavedStateHandle(),
                ) as T
            }
    }
}
