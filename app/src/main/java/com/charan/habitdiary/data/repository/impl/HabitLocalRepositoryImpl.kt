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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
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

    override fun getAllDailyLogs(): Flow<List<DailyLogEntity>> {
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



    override fun getTodayHabits(
        currentDayOfWeek: DayOfWeek,
    ): Flow<List<HabitWithDone>> {
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
                    logId = log?.id
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
}