package com.puce.habitatgest.domain.usecase

import com.puce.habitatgest.domain.model.Espacio
import com.puce.habitatgest.domain.repository.EspacioRepository
import kotlinx.coroutines.flow.Flow

class GetEspaciosUseCase(private val repository: EspacioRepository) {
    operator fun invoke(): Flow<List<Espacio>> = repository.observarEspacios()
}
