package com.puce.habitatgest.data.remote.api

import com.puce.habitatgest.data.remote.dto.CondicionesDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Servicio REST — Open-Meteo (gratuito, sin API key).
 * Base URL: https://api.open-meteo.com/
 * Devuelve condiciones climáticas actuales del campus PUCE en Quito.
 */
interface HabitatApiService {

    @GET("v1/forecast")
    suspend fun getCondicionesAmbientales(
        @Query("latitude")  lat: Double = -0.2295,
        @Query("longitude") lon: Double = -78.5243,
        @Query("current")   variables: String =
            "temperature_2m,relative_humidity_2m,wind_speed_10m",
    ): CondicionesDto
}
