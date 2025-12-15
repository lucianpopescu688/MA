package com.example.servicebuddy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.servicebuddy.dao.MaintenanceEventDao
import com.example.servicebuddy.model.MaintenanceEvent

@Database(entities = [MaintenanceEvent::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): MaintenanceEventDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "service_buddy_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
