package com.puce.habitatgest.data.di

import android.content.Context
import com.puce.habitatgest.data.local.db.AppDatabase
import com.puce.habitatgest.data.remote.api.HabitatApiService
import com.puce.habitatgest.data.repository.EspacioRepositoryImpl
import com.puce.habitatgest.domain.repository.EspacioRepository
import com.puce.habitatgest.domain.usecase.GetCondicionesUseCase
import com.puce.habitatgest.domain.usecase.GetEspaciosUseCase
import com.puce.habitatgest.domain.usecase.SaveEspacioUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Contenedor de dependencias manual (sin Hilt).
 * Construido una sola vez en HabitatApp y accesible desde MainActivity.
 */
class AppContainer(context: Context) {

    private val database = AppDatabase.getInstance(context)

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY },
        )
        .build()

    private val api: HabitatApiService = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HabitatApiService::class.java)

    private val repository: EspacioRepository = EspacioRepositoryImpl(
        dao = database.espacioDao(),
        api = api,
    )

    // ── Use Cases expuestos ───────────────────────────────────────────────────
    val getEspaciosUseCase    = GetEspaciosUseCase(repository)
    val saveEspacioUseCase    = SaveEspacioUseCase(repository)
    val getCondicionesUseCase = GetCondicionesUseCase(repository)
}
