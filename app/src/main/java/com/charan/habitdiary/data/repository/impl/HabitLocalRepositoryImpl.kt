package com.charan.habitdiary.data.repository.impl

import com.charan.habitdiary.data.local.dao.DailyLogDao
import com.charan.habitdiary.data.local.dao.HabitDao
import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.DailyLogWithHabit
import com.charan.habitdiary.data.local.model.HabitWithDone
import com.charan.habitdiary.data.repository.HabitLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class HabitLocalRepositoryImpl(
    private val habitDao : HabitDao,
    private val dailyLogDao : DailyLogDao
) : HabitLocalRepository {

    override fun upsetHabit(habit: HabitEntity) : Long {
        return habitDao.upsetHabit(habit)

    }

    override fun upsetDailyLog(dailyLog: DailyLogEntity) {
        dailyLogDao.upsetDailyLog(dailyLog)
    }

    override fun getAllHabits(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabits()
    }

    override fun getAllDailyLogs(): Flow<List<DailyLogEntity>> {
        return dailyLogDao.getAllDailyLogs()
    }

    override fun getDailyLogsInRange(
        startOfDay: Long,
        endOfDay: Long
    ): Flow<List<DailyLogWithHabit>> {
        return dailyLogDao.getDailyLogsInRange(startOfDay, endOfDay)
    }


    override fun getTodayHabits(
        currentDayNumber: Int,
        startOfDay: Long
    ): Flow<List<HabitWithDone>> {
        return combine(
            habitDao.getTodayHabits(currentDayNumber),
            getLoggedHabitIdsForToday(startOfDay)
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

    override fun getLoggedHabitIdsForToday(startOfDay: Long): Flow<List<DailyLogEntity>> {
        return dailyLogDao.getLoggedHabitIdsForToday(startOfDay)
    }
}