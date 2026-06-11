package com.puce.habitatgest.presentation.detalle

import com.puce.habitatgest.domain.model.CondicionesAmbientales
import com.puce.habitatgest.domain.model.Espacio

/** Estado sellado para la llamada a la API del clima. */
sealed class RemoteState {
    data object Idle    : RemoteState()
    data object Loading : RemoteState()
    data class  Success(val data: CondicionesAmbientales) : RemoteState()
    data class  Error(val message: String) : RemoteState()
}

data class DetalleUiState(
    val espacio         : Espacio?    = null,
    val condiciones     : RemoteState = RemoteState.Idle,
    val cargandoEspacio : Boolean     = true,
)
