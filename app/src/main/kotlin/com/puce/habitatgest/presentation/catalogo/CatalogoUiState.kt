package com.puce.habitatgest.presentation.catalogo

import com.puce.habitatgest.domain.model.Espacio

data class CatalogoUiState(
    val espacios  : List<Espacio> = emptyList(),
    val cargando  : Boolean       = true,
)
