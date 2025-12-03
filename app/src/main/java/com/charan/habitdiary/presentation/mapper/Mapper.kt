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
import com.charan.habitdiary.utils.DateUtil.toFormattedString
import kotlinx.datetime.LocalDateTime

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
        habitTime = this.habitTime,
        habitReminder = this.habitReminderTime,
        habitFrequency = this.habitFrequency.joinToString(","),
        isReminderEnabled = this.isReminderEnabled,
        id = this.habitId ?: 0,
        createdAt = DateUtil.getCurrentDateTime()
    )
}

fun HabitItemUIState.toDailyLogEntity(date : LocalDateTime) : DailyLogEntity {
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
        createdAt = DateUtil.mergeDateTime(item.date, item.time),
        habitId = item.habitId
    )
}





fun DailyLogWithHabit.toDailyLogUIState(is24HourFormat : Boolean) : DailyLogItemUIState {
    return DailyLogItemUIState(
        id = this.dailyLogEntity.id,
        logNote = this.dailyLogEntity.logNote,
        imagePath = this.dailyLogEntity.imagePath,
        createdAt = this.dailyLogEntity.createdAt.time.toFormattedString(is24HourFormat),
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
        date = this.dailyLogEntity.createdAt.date,
        time = this.dailyLogEntity.createdAt.time,
        habitId = this.dailyLogEntity.habitId,
        habitName = this.habitEntity?.habitName,
        habitDescription = this.habitEntity?.habitDescription
    )
}
