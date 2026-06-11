package com.puce.habitatgest.domain.usecase

import com.puce.habitatgest.domain.model.CondicionesAmbientales
import com.puce.habitatgest.domain.repository.EspacioRepository

class GetCondicionesUseCase(private val repository: EspacioRepository) {
    suspend operator fun invoke(): Result<CondicionesAmbientales> =
        repository.obtenerCondicionesAmbientales()
}
