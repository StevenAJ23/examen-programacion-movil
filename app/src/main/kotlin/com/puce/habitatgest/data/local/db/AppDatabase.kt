package com.puce.habitatgest.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.puce.habitatgest.data.local.dao.EspacioDao
import com.puce.habitatgest.data.local.entity.EspacioEntity

@Database(
    entities  = [EspacioEntity::class],
    version   = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun espacioDao(): EspacioDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitatgest.db",
                ).build().also { INSTANCE = it }
            }
    }
}
