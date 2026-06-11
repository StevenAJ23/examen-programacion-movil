package com.puce.habitatgest.presentation.navigation

import kotlinx.serialization.Serializable

/** Rutas tipadas para la navegación — Navigation Compose 2.8+. */
sealed interface Screen {

    @Serializable
    data object Catalogo : Screen

    @Serializable
    data object Registro : Screen

    @Serializable
    data class Detalle(val espacioId: Int) : Screen
}
