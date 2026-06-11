package com.puce.habitatgest.presentation.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.puce.habitatgest.domain.usecase.GetCondicionesUseCase
import com.puce.habitatgest.domain.usecase.GetEspaciosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val id: Int,
    private val getEspacios: GetEspaciosUseCase,
    private val getCondiciones: GetCondicionesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        cargarEspacio()
        cargarCondiciones()
    }

    private fun cargarEspacio() {
        viewModelScope.launch {
            val espacio = getEspacios().first().find { it.id == id }
            _uiState.update { it.copy(espacio = espacio, cargandoEspacio = false) }
        }
    }

    fun cargarCondiciones() {
        _uiState.update { it.copy(condiciones = RemoteState.Loading) }
        viewModelScope.launch {
            getCondiciones().fold(
                onSuccess = { c ->
                    _uiState.update { it.copy(condiciones = RemoteState.Success(c)) }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(condiciones = RemoteState.Error(e.message ?: "Error de red"))
                    }
                },
            )
        }
    }

    companion object {
        fun factory(
            id: Int,
            getEspaciosUseCase: GetEspaciosUseCase,
            getCondicionesUseCase: GetCondicionesUseCase,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T = DetalleViewModel(
                    id             = id,
                    getEspacios    = getEspaciosUseCase,
                    getCondiciones = getCondicionesUseCase,
                ) as T
            }
    }
}
