package com.puce.habitatgest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.puce.habitatgest.data.local.entity.EspacioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EspacioDao {

    /** Flujo reactivo: emite cada vez que la tabla cambia. */
    @Query("SELECT * FROM espacios ORDER BY nombre ASC")
    fun observarTodos(): Flow<List<EspacioEntity>>

    @Query("SELECT * FROM espacios WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): EspacioEntity?

    /** REPLACE garantiza upsert: inserta o actualiza si el id ya existe. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(entity: EspacioEntity)
}
