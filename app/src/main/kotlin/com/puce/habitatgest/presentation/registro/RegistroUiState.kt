package com.puce.habitatgest.presentation.registro

import com.puce.habitatgest.domain.model.TipoEspacio

data class RegistroUiState(
    val nombre      : String     = "",
    val capacidad   : String     = "",           // String para el TextField
    val tipo        : TipoEspacio = TipoEspacio.AULA,
    val descripcion : String     = "",
    val ubicacion   : String     = "",
    val disponible  : Boolean    = true,
    // Errores de validación
    val nombreError    : String? = null,
    val capacidadError : String? = null,
    val ubicacionError : String? = null,
    // Estado del guardado
    val guardando  : Boolean = false,
    val guardado   : Boolean = false,
    val errorGuardar : String? = null,
)
