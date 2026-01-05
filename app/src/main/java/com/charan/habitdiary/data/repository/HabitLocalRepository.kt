package com.charan.habitdiary.data.repository

import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.DailyLogMediaEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.DailyLogWithHabit
import com.charan.habitdiary.data.local.model.HabitWithDone
import com.charan.habitdiary.utils.DateUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface HabitLocalRepository {

    fun upsetHabit(habit: HabitEntity): Long

    fun upsetDailyLog(
        dailyLog: DailyLogEntity,
        mediaEntity: List<DailyLogMediaEntity> = emptyList()
    )

    fun getAllHabitsFlow(): Flow<List<HabitEntity>>

    fun getAllHabits(): List<HabitEntity>

    fun getAllDailyLogsFlow(): Flow<List<DailyLogEntity>>

    fun getAllDailyLogs(): List<DailyLogEntity>

    fun getDailyLogsInRange(
        startOfDay: LocalDateTime = DateUtil.todayStartOfDay(),
        endOfDay: LocalDateTime = DateUtil.todayEndOfDay()
    ): Flow<List<DailyLogWithHabit>>

    fun getTodayHabits(currentDayOfWeek: DayOfWeek = DateUtil.getCurrentDayOfWeek()): Flow<List<HabitWithDone>>

    fun getDailyLogWithId(id: Int): DailyLogEntity

    fun getDailyLogsWithHabitWithId(id: Int): DailyLogWithHabit

    fun getHabitWithId(id: Int): HabitEntity

    fun deleteDailyLog(id: Int)

    fun deleteHabit(id: Int)

    fun getLoggedHabitIdsForRange(
        startOfDay: LocalDateTime = DateUtil.todayStartOfDay(),
        endOfDay: LocalDateTime = DateUtil.todayEndOfDay()
    ): Flow<List<DailyLogEntity>>

    fun getLoggedHabitFromIdForRange(
        habitId: Int,
        startOfDay: LocalDateTime = DateUtil.todayStartOfDay(),
        endOfDay: LocalDateTime = DateUtil.todayEndOfDay()
    ): DailyLogEntity?


    fun upsetDailyLogMediaEntities(mediaEntity: List<DailyLogMediaEntity>)

    fun getAllMedia() : List<DailyLogMediaEntity>

    fun insertDailyLogs(dailyLogs: List<DailyLogEntity>) : List<Long>

    fun insertHabits(habits: List<HabitEntity>) : List<Long>

    fun getLoggedDatesInRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<List<LocalDate>>

    fun getAllLogsWithHabitId(habitId: Int): Flow<List<DailyLogEntity>>
}