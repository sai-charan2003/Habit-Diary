package com.charan.habitdiary.presentation.mapper

import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.DailyLogMediaEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import com.charan.habitdiary.data.local.model.DailyLogWithHabit
import com.charan.habitdiary.data.local.model.HabitWithDone
import com.charan.habitdiary.presentation.add_daily_log.DailyLogItemDetails
import com.charan.habitdiary.presentation.add_daily_log.DailyLogMediaItem
import com.charan.habitdiary.presentation.add_daily_log.DailyLogState
import com.charan.habitdiary.presentation.add_habit.AddHabitState
import com.charan.habitdiary.presentation.common.model.DailyLogItemUIState
import com.charan.habitdiary.presentation.habits.HabitItemUIState
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
            logId = null,
            habitReminderTime = null
        )
    }
}

/**
 * Converts a HabitWithDone into a HabitItemUIState for UI presentation.
 *
 * Times in the resulting UI state are formatted according to the `is24HourFormat` flag; `habitReminderTime`
 * will be `null` if the source habit has no reminder set.
 *
 * @param is24HourFormat `true` to format times using a 24-hour clock, `false` to use a 12-hour clock.
 * @return A `HabitItemUIState` populated with id, name, description, formatted `habitTime`, `isDone`, `logId`,
 *         formatted `habitReminderTime` (or `null`), and `habitFrequency`.
 */
fun HabitWithDone.toHabitUIState(is24HourFormat: Boolean) : HabitItemUIState {
    return HabitItemUIState(
        id = this.habitEntity.id,
        habitName = this.habitEntity.habitName,
        habitDescription = this.habitEntity.habitDescription,
        habitTime = this.habitEntity.habitTime.toFormattedString(is24HourFormat),
        isDone = this.isDone,
        logId = this.logId,
        habitReminderTime = this.habitEntity.habitReminder?.toFormattedString(is24HourFormat),
        habitFrequency = this.habitEntity.habitFrequency
    )
}

fun List<HabitWithDone>.toHabitUIState(is24HourFormat: Boolean) : List<HabitItemUIState> {
    return this.map {
        it.toHabitUIState(is24HourFormat)
    }
}

fun AddHabitState.toHabitEntity(): HabitEntity {
    return HabitEntity(
        habitName = this.habitTitle,
        habitDescription = this.habitDescription,
        habitTime = this.habitTime,
        habitReminder = if(this.isReminderEnabled) this.habitReminderTime else null,
        habitFrequency = this.habitFrequency,
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
        imagePath = "",
        createdAt = DateUtil.mergeDateTime(item.date, item.time),
        habitId = item.habitId
    )
}

fun DailyLogMediaItem.toDailyLogMediaEntity() : DailyLogMediaEntity {
    return DailyLogMediaEntity(
        dailyLogId =  0,
        mediaPath = this.mediaPath,
        isDeleted = this.isDeleted
    )
}

fun List<DailyLogMediaItem>.toDailyLogMediaEntityList() : List<DailyLogMediaEntity> {
    return this.map {
        it.toDailyLogMediaEntity()
    }
}





fun DailyLogWithHabit.toDailyLogUIState(is24HourFormat : Boolean) : DailyLogItemUIState {
    return DailyLogItemUIState(
        id = this.dailyLogEntity.id,
        logNote = this.dailyLogEntity.logNote,
        mediaPaths = this.mediaEntities.map { it.mediaPath },
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

fun DailyLogMediaEntity.toDailyLogMediaItem(isPendingSave : Boolean) : DailyLogMediaItem {
    return DailyLogMediaItem(
        mediaPath = this.mediaPath,
        isDeleted = this.isDeleted,
        id = this.id,
        isPendingSave = isPendingSave
    )
}
fun List<DailyLogMediaEntity>.toDailyLogMediaItemList(isPendingSave : Boolean) : List<DailyLogMediaItem> {
    return this.map {
        it.toDailyLogMediaItem(isPendingSave)
    }
}

fun DailyLogWithHabit.toDailyLogItemDetails(pendingSaveImage : Boolean = false) : DailyLogItemDetails {
    return DailyLogItemDetails(
        id = this.dailyLogEntity.id,
        notesText = this.dailyLogEntity.logNote,
        mediaItems = this.mediaEntities.toDailyLogMediaItemList(pendingSaveImage),
        date = this.dailyLogEntity.createdAt.date,
        time = this.dailyLogEntity.createdAt.time,
        habitId = this.dailyLogEntity.habitId,
        habitName = this.habitEntity?.habitName,
        habitDescription = this.habitEntity?.habitDescription
    )
}