package com.charan.habitdiary.data.repository

import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.DailyLogWithHabit
import com.charan.habitdiary.data.local.model.HabitWithDone
import com.charan.habitdiary.utils.DateUtil
import kotlinx.coroutines.flow.Flow

interface HabitLocalRepository {

    fun upsetHabit(habit : HabitEntity) : Long

    fun upsetDailyLog(dailyLog : DailyLogEntity)

    fun getAllHabits() : Flow<List<HabitEntity>>

    fun getAllDailyLogs() : Flow<List<DailyLogEntity>>

    fun getDailyLogsInRange(startOfDay : Long = DateUtil.todayStartOfDay(),endOfDay : Long = DateUtil.todayEndOfDay()) : Flow<List<DailyLogWithHabit>>

    fun getTodayHabits(currentDayNumber : Int = DateUtil.currentDayIndex(), startOfDay : Long = DateUtil.todayStartOfDay()) : Flow<List<HabitWithDone>>

    fun getDailyLogWithId(id : Int) : DailyLogEntity

    fun getDailyLogsWithHabitWithId(id : Int) : DailyLogWithHabit

    fun getHabitWithId(id : Int) : HabitEntity

    fun deleteDailyLog(id : Int)

    fun deleteHabit(id : Int)

    fun getLoggedHabitIdsForToday(startOfDay: Long = DateUtil.todayStartOfDay()) : Flow<List<DailyLogEntity>>
}