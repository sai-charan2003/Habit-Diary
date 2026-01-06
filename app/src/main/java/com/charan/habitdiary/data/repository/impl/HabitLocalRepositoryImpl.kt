package com.charan.habitdiary.data.repository.impl

import com.charan.habitdiary.data.local.dao.DailyLogDao
import com.charan.habitdiary.data.local.dao.DailyLogMediaDao
import com.charan.habitdiary.data.local.dao.HabitDao
import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.DailyLogMediaEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.DailyLogWithHabit
import com.charan.habitdiary.data.local.model.HabitWithDone
import com.charan.habitdiary.data.repository.HabitLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class HabitLocalRepositoryImpl(
    private val habitDao : HabitDao,
    private val dailyLogDao : DailyLogDao,
    private val dailyLogMediaDao: DailyLogMediaDao
) : HabitLocalRepository {

    override fun upsetHabit(habit: HabitEntity) : Long {
        return habitDao.upsetHabit(habit)

    }

    override fun upsetDailyLog(
        dailyLog: DailyLogEntity,
        mediaEntity : List<DailyLogMediaEntity>
    ) {
        val id = dailyLogDao.upsetDailyLog(dailyLog)
        if(mediaEntity.isNotEmpty()){
            val mediaEntity = mediaEntity.map { it.copy(dailyLogId = id.toInt()) }
            dailyLogMediaDao.upsertMedia(mediaEntity)
        }
    }

    override fun getAllHabitsFlow(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabitsFlow()
    }

    override fun getAllHabits(): List<HabitEntity> {
        return habitDao.getAllHabits()
    }

    override fun getAllDailyLogsFlow(): Flow<List<DailyLogEntity>> {
        return dailyLogDao.getAllDailyLogsFlow()
    }
    override fun getAllDailyLogs(): List<DailyLogEntity> {
        return dailyLogDao.getAllDailyLogs()
    }

    override fun getDailyLogsInRange(
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): Flow<List<DailyLogWithHabit>> {
        return dailyLogDao
            .getDailyLogsInRange(startOfDay, endOfDay)
            .map { logs ->
                logs.map { log ->
                    log.copy(
                        mediaEntities = log.mediaEntities.filter { !it.isDeleted }
                    )
                }
            }
    }



    /**
     * Produces a reactive list of active habits annotated with completion status.
     *
     * Each `HabitWithDone` has `isDone` set to `true` when a corresponding daily log exists within the repository's tracked range; `logId` and `created` reflect the matching daily log when present.
     *
     * @return A `Flow` that emits lists of `HabitWithDone` for active habits.
     */
    override fun getActiveHabits(): Flow<List<HabitWithDone>> {
        return combine(
            habitDao.getActiveHabitsFlow(),
            getLoggedHabitIdsForRange()
        ) { habits, dailyLogs ->
            val logMap = dailyLogs.associateBy { it.habitId }
            habits.map { habit ->
                val log = logMap[habit.id]
                HabitWithDone(
                    habitEntity = habit,
                    isDone = log != null,
                    logId = log?.id,
                    created = log?.createdAt
                )
            }
        }


    }


    override fun getDailyLogWithId(id: Int): DailyLogEntity {
        return dailyLogDao.getDailyLogWithId(id)
    }

    override fun getDailyLogsWithHabitWithId(id: Int): DailyLogWithHabit {
        return dailyLogDao.getDailyLogsWithHabitWithId(id)
    }

    override fun getHabitWithId(id: Int): HabitEntity {
        return habitDao.getHabitWithId(id)
    }

    override fun deleteDailyLog(id: Int) {
        dailyLogDao.deleteDailyLog(id)

    }

    override fun deleteHabit(id: Int) {
        habitDao.deleteHabit(id)
    }

    override fun getLoggedHabitIdsForRange(startOfDay: LocalDateTime,endOfDay: LocalDateTime): Flow<List<DailyLogEntity>> {
        return dailyLogDao.getLoggedHabitIdsForToday(startOfDay,endOfDay)
    }

    override fun getLoggedHabitFromIdForRange(
        habitId: Int,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): DailyLogEntity? {
        return dailyLogDao.getLoggedHabitFromIdForRange(habitId, startOfDay, endOfDay)
    }

    override fun upsetDailyLogMediaEntities(mediaEntity: List<DailyLogMediaEntity>) {
        dailyLogMediaDao.upsertMedia(mediaEntity)
    }

    override fun getLoggedDatesInRange(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<List<LocalDate>> {
        return dailyLogDao.getLoggedDatesInRange(start,end)
    }

    override fun getAllMedia(): List<DailyLogMediaEntity> {
        return dailyLogMediaDao.getAllMedia()
    }

    override fun insertDailyLogs(dailyLogs: List<DailyLogEntity>): List<Long> {
        return dailyLogDao.insertDailyLogs(dailyLogs)
    }
    override fun insertHabits(habits: List<HabitEntity>) : List<Long> {
        return habitDao.insertHabits(habits)
    }

    /**
     * Provides a reactive stream of all daily logs for a given habit.
     *
     * @param habitId The primary key of the habit whose logs are requested.
     * @return A Flow that emits lists of DailyLogEntity belonging to the specified habit.
     */
    override fun getAllLogsWithHabitId(habitId: Int): Flow<List<DailyLogEntity>> {
        return dailyLogDao.getAllLogsForHabitId(habitId)
    }

    /**
     * Produces a reactive list of habits scheduled for the given day, each annotated with completion metadata.
     *
     * Each emitted HabitWithDone has `isDone` set to `true` if a daily log exists for that habit in the repository's current log range; when present, `logId` and `created` contain the corresponding log's id and creation timestamp.
     *
     * @param currentDayOfWeek The day of week used to select habits scheduled for that day.
     * @return A Flow that emits lists of HabitWithDone reflecting scheduled habits and their logged state.
     */
    override fun getTodayHabits(currentDayOfWeek: DayOfWeek): Flow<List<HabitWithDone>> {
        return combine(
            habitDao.getTodayHabits(currentDayOfWeek),
            getLoggedHabitIdsForRange()
        ) { habits, dailyLogs ->
            val logMap = dailyLogs.associateBy { it.habitId }
            habits.map { habit ->
                val log = logMap[habit.id]
                HabitWithDone(
                    habitEntity = habit,
                    isDone = log != null,
                    logId = log?.id,
                    created = log?.createdAt
                )
            }
        }
    }
}