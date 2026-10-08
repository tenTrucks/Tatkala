package com.example.tatkala.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tatkala.data.local.dao.HabitDao
import com.example.tatkala.data.local.dao.TaskDao
import com.example.tatkala.data.local.entity.HabitEntity
import com.example.tatkala.data.local.entity.HabitLogEntity
import com.example.tatkala.data.local.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        HabitEntity::class,
        HabitLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TatkalaDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var instance: TatkalaDatabase? = null

        fun getInstance(context: Context): TatkalaDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TatkalaDatabase::class.java,
                    "tatakala.db"
                ).build().also { instance = it }
            }
        }
    }
}
