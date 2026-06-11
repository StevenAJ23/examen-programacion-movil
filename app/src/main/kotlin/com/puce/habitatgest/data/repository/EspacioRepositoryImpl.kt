package com.puce.habitatgest.data.repository

import com.puce.habitatgest.data.local.dao.EspacioDao
import com.puce.habitatgest.data.local.entity.toDomain
import com.puce.habitatgest.data.local.entity.toEntity
import com.puce.habitatgest.data.remote.api.HabitatApiService
import com.puce.habitatgest.domain.model.CondicionesAmbientales
import com.puce.habitatgest.domain.model.Espacio
import com.puce.habitatgest.domain.repository.EspacioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class EspacioRepositoryImpl(
    private val dao: EspacioDao,
    private val api: HabitatApiService,
) : EspacioRepository {

    override fun observarEspacios(): Flow<List<Espacio>> =
        dao.observarTodos().map { list -> list.map { it.toDomain() } }

    override suspend fun guardarEspacio(espacio: Espacio) =
        withContext(Dispatchers.IO) { dao.insertar(espacio.toEntity()) }

    override suspend fun obtenerEspacioPorId(id: Int): Espacio? =
        withContext(Dispatchers.IO) { dao.buscarPorId(id)?.toDomain() }

    override suspend fun obtenerCondicionesAmbientales(): Result<CondicionesAmbientales> =
        withContext(Dispatchers.IO) {
            runCatching {
                val dto = api.getCondicionesAmbientales()
                CondicionesAmbientales(
                    temperaturaC = dto.current.temperatura,
                    humedadPct   = dto.current.humedad,
                    vientoKmH    = dto.current.viento,
                )
            }
        }
}
