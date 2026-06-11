package com.puce.habitatgest.data.remote.dto

import com.google.gson.annotations.SerializedName

/** DTO mapeado desde la respuesta JSON de Open-Meteo. */
data class CondicionesDto(
    @SerializedName("current") val current: CondicionesCurrentDto,
)

data class CondicionesCurrentDto(
    @SerializedName("temperature_2m")        val temperatura: Double,
    @SerializedName("relative_humidity_2m")  val humedad: Int,
    @SerializedName("wind_speed_10m")        val viento: Double,
)
