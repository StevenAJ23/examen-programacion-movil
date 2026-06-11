package com.puce.habitatgest

import android.app.Application
import com.puce.habitatgest.data.di.AppContainer

class HabitatApp : Application() {

    /** Punto único de acceso al grafo de dependencias. */
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
