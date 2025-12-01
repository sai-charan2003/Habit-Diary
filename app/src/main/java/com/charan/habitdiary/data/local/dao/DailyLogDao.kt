package com.charan.habitdiary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.model.DailyLogWithHabit
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyLogDao {

    @Upsert
    fun upsetDailyLog(dailyLog: DailyLogEntity)

    @Update
    fun updateDailyLog(dailyLog: DailyLogEntity)

    @Insert
    fun insertDailyLogs(dailyLogs: List<DailyLogEntity>)

    @Query("SELECT * FROM daily_log_entity ORDER BY createdAt DESC")
    fun getAllDailyLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_log_entity WHERE createdAt >= :startOfDay and createdAt <= :endOfDay and isDeleted = 0 ORDER BY createdAt DESC")
    fun getDailyLogsInRange(startOfDay: Long , endOfDay : Long): Flow<List<DailyLogWithHabit>>

    @Query("SELECT * FROM daily_log_entity WHERE id = :id")
    fun getDailyLogWithId(id: Int): DailyLogEntity

    @Query("SELECT * FROM daily_log_entity WHERE id = :id")
    fun getDailyLogsWithHabitWithId(id: Int): DailyLogWithHabit

    @Query("UPDATE daily_log_entity SET isDeleted = 1 WHERE id = :id")
    fun deleteDailyLog(id: Int)

    @Query("SELECT * FROM daily_log_entity WHERE createdAt >= :startOfDay AND isDeleted = 0")
    fun getLoggedHabitIdsForToday(startOfDay: Long): Flow<List<DailyLogEntity>>


}