package com.puce.habitatgest.domain.model

/** Entidad de negocio pura — sin dependencias de Android ni de Room. */
data class Espacio(
    val id: Int = 0,
    val nombre: String,
    val capacidad: Int,
    val tipo: TipoEspacio,
    val descripcion: String,
    val disponible: Boolean = true,
    val ubicacion: String,
)
