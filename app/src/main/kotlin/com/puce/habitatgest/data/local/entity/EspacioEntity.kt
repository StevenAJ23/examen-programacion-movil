package com.puce.habitatgest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.puce.habitatgest.domain.model.Espacio
import com.puce.habitatgest.domain.model.TipoEspacio

@Entity(tableName = "espacios")
data class EspacioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val capacidad: Int,
    val tipo: String,           // TipoEspacio.name serializado
    val descripcion: String,
    val disponible: Boolean,
    val ubicacion: String,
)

// ── Mappers ───────────────────────────────────────────────────────────────────

fun EspacioEntity.toDomain() = Espacio(
    id          = id,
    nombre      = nombre,
    capacidad   = capacidad,
    tipo        = TipoEspacio.valueOf(tipo),
    descripcion = descripcion,
    disponible  = disponible,
    ubicacion   = ubicacion,
)

fun Espacio.toEntity() = EspacioEntity(
    id          = id,
    nombre      = nombre,
    capacidad   = capacidad,
    tipo        = tipo.name,
    descripcion = descripcion,
    disponible  = disponible,
    ubicacion   = ubicacion,
)
