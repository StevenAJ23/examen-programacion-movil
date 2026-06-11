package com.puce.habitatgest.domain.usecase

import com.puce.habitatgest.domain.model.Espacio
import com.puce.habitatgest.domain.repository.EspacioRepository

class SaveEspacioUseCase(private val repository: EspacioRepository) {
    suspend operator fun invoke(espacio: Espacio) = repository.guardarEspacio(espacio)
}
