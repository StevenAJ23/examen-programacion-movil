package com.puce.habitatgest.domain.model

/** Resultado del servicio remoto Open-Meteo para el campus PUCE, Quito. */
data class CondicionesAmbientales(
    val temperaturaC: Double,
    val humedadPct: Int,
    val vientoKmH: Double,
)
