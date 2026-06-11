package com.puce.habitatgest.domain.repository

import com.puce.habitatgest.domain.model.CondicionesAmbientales
import com.puce.habitatgest.domain.model.Espacio
import kotlinx.coroutines.flow.Flow

/**
 * Contrato abstracto del repositorio — pertenece al dominio puro.
 * La capa Data implementa esta interfaz; la capa Domain sólo la conoce.
 */
interface EspacioRepository {
    /** Flujo reactivo de todos los espacios almacenados localmente. */
    fun observarEspacios(): Flow<List<Espacio>>

    /** Inserta o actualiza un espacio en la base de datos local. */
    suspend fun guardarEspacio(espacio: Espacio)

    /** Consulta un espacio por su identificador. */
    suspend fun obtenerEspacioPorId(id: Int): Espacio?

    /** Llama al servicio remoto para obtener condiciones ambientales del campus. */
    suspend fun obtenerCondicionesAmbientales(): Result<CondicionesAmbientales>
}
