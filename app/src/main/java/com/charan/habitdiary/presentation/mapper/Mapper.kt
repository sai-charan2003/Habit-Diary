package com.charan.habitdiary.presentation.mapper

import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.DailyLogWithHabit
import com.charan.habitdiary.data.local.model.HabitWithDone
import com.charan.habitdiary.presentation.add_daily_log.DailyLogItemDetails
import com.charan.habitdiary.presentation.add_daily_log.DailyLogState
import com.charan.habitdiary.presentation.add_habit.AddHabitState
import com.charan.habitdiary.presentation.common.model.DailyLogItemUIState
import com.charan.habitdiary.presentation.home.HabitItemUIState
import com.charan.habitdiary.utils.DateUtil
import com.charan.habitdiary.utils.DateUtil.toHourMinutesLong

fun List<HabitEntity>.toHabitUIList() : List<HabitItemUIState>{
    return this.map {
        HabitItemUIState(
            id = it.id,
            habitName = it.habitName,
            habitDescription = it.habitDescription,
            habitTime = it.habitTime.toString(),
            logId = null
        )
    }
}

fun HabitWithDone.toHabitUIState() : HabitItemUIState {
    return HabitItemUIState(
        id = this.habitEntity.id,
        habitName = this.habitEntity.habitName,
        habitDescription = this.habitEntity.habitDescription,
        habitTime = this.habitEntity.habitTime.toString(),
        isDone = this.isDone,
        logId = this.logId
    )
}

fun List<HabitWithDone>.toHabitUIState() : List<HabitItemUIState> {
    return this.map {
        it.toHabitUIState()
    }
}

fun AddHabitState.toHabitEntity(): HabitEntity {
    return HabitEntity(
        habitName = this.habitTitle,
        habitDescription = this.habitDescription,
        habitTime = this.habitTime.toHourMinutesLong(),
        habitReminder = this.habitReminderTime?.toHourMinutesLong() ?: 0L,
        habitFrequency = this.habitFrequency.joinToString(","),
        isReminderEnabled = this.isReminderEnabled,
        id = this.habitId ?: 0,
        createdAt = System.currentTimeMillis()
    )
}

fun HabitItemUIState.toDailyLogEntity(date : Long) : DailyLogEntity {
    return DailyLogEntity(
        logNote = "",
        imagePath = "",
        createdAt = date,
        habitId = this.id
    )
}

fun DailyLogState.toDailyLogEntity(): DailyLogEntity {
    val item = this.dailyLogItemDetails
    return DailyLogEntity(
        id = item.id ?: 0,
        logNote = item.notesText,
        imagePath = item.imagePath,
        createdAt = DateUtil.mergeDateTimeToMillis(item.dateMillis, item.timeMillis),
        habitId = item.habitId
    )
}





fun DailyLogWithHabit.toDailyLogUIState(is24HourFormat : Boolean) : DailyLogItemUIState {
    return DailyLogItemUIState(
        id = this.dailyLogEntity.id,
        logNote = this.dailyLogEntity.logNote,
        imagePath = this.dailyLogEntity.imagePath,
        createdAt = DateUtil.convertTimeMillisToTimeString(this.dailyLogEntity.createdAt,is24HourFormat),
        habitId = this.dailyLogEntity.habitId,
        habitName = this.habitEntity?.habitName
    )
}

fun List<DailyLogWithHabit>.toDailyLogUIStateList(is24HourFormat: Boolean) : List<DailyLogItemUIState> {
    return this.map {
        it.toDailyLogUIState(is24HourFormat)
    }
}

fun DailyLogWithHabit.toDailyLogItemDetails() : DailyLogItemDetails {
    return DailyLogItemDetails(
        id = this.dailyLogEntity.id,
        notesText = this.dailyLogEntity.logNote,
        imagePath = this.dailyLogEntity.imagePath,
        dateMillis = this.dailyLogEntity.createdAt,
        timeMillis = this.dailyLogEntity.createdAt,
        habitId = this.dailyLogEntity.habitId,
        habitName = this.habitEntity?.habitName,
        habitDescription = this.habitEntity?.habitDescription
    )
}
