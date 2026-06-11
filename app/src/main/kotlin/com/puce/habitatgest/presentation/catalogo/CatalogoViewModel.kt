package com.puce.habitatgest.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.puce.habitatgest.domain.usecase.GetEspaciosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class CatalogoViewModel(
    getEspacios: GetEspaciosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()

    init {
        getEspacios()
            .onEach { list ->
                _uiState.update { it.copy(espacios = list, cargando = false) }
            }
            .catch { _uiState.update { it.copy(cargando = false) } }
            .launchIn(viewModelScope)
    }

    companion object {
        fun factory(getEspacios: GetEspaciosUseCase): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T = CatalogoViewModel(getEspacios) as T
            }
    }
}
