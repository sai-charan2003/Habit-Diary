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

    /**
     * Provides daily log entries paired with their associated habits for the specified time range.
     *
     * @param startOfDay The start of the time range; defaults to the start of today via `DateUtil.todayStartOfDay()`.
     * @param endOfDay The end of the time range; defaults to the end of today via `DateUtil.todayEndOfDay()`.
     * @return A Flow that emits lists of `DailyLogWithHabit` items occurring within the specified time range.
     */
    fun getDailyLogsInRange(
        startOfDay: LocalDateTime = DateUtil.todayStartOfDay(),
        endOfDay: LocalDateTime = DateUtil.todayEndOfDay()
    ): Flow<List<DailyLogWithHabit>>

    /**
 * Provides a reactive stream of active habits annotated with their done status.
 *
 * @return A Flow emitting lists of HabitWithDone representing active habits and their completion state.
 */
fun getActiveHabits(): Flow<List<HabitWithDone>>

    /**
 * Retrieve the daily log with the specified identifier.
 *
 * @param id The identifier of the daily log to retrieve.
 * @return The DailyLogEntity matching the given id.
 */
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

    /**
     * Provides a reactive stream of calendar dates that have log entries within the specified time range.
     *
     * @param start Inclusive start of the time range to consider.
     * @param end Inclusive end of the time range to consider.
     * @return Lists of LocalDate values that contain logged entries within the specified range.
     */
    fun getLoggedDatesInRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<List<LocalDate>>

    /**
 * Provides a reactive stream of daily logs associated with a specific habit.
 *
 * @param habitId Identifier of the habit whose logs should be retrieved.
 * @return Emitted values are `List<DailyLogEntity>` containing the daily logs for the given `habitId`.
fun getAllLogsWithHabitId(habitId: Int): Flow<List<DailyLogEntity>>

    /**
 * Provide a stream of habits scheduled for the specified day annotated with their done status.
 *
 * @param currentDayOfWeek The day of week to treat as "today"; defaults to the current day of week.
 * @return Lists of HabitWithDone for habits scheduled on the specified day; each emission reflects the current stored done-status.
 */
fun getTodayHabits(currentDayOfWeek: DayOfWeek = DateUtil.getCurrentDayOfWeek()): Flow<List<HabitWithDone>>
}