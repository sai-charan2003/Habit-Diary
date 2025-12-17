package com.charan.habitdiary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.charan.habitdiary.data.local.dao.DailyLogDao
import com.charan.habitdiary.data.local.dao.DailyLogMediaDao
import com.charan.habitdiary.data.local.dao.HabitDao
import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.DailyLogMediaEntity
import com.charan.habitdiary.data.local.entity.HabitEntity

@Database(
    entities = [
        HabitEntity::class,
        DailyLogEntity::class,
        DailyLogMediaEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    Converters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao() : HabitDao
    abstract fun dailyLogDao() : DailyLogDao

    abstract fun dailyLogMediaEntityDao() : DailyLogMediaDao
    companion object {
        const val DATABASE_NAME = "habit_diary_database"
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context : Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(
                        MIGRATION_1_2
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}