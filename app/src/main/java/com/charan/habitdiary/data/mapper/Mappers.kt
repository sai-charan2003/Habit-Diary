package com.charan.habitdiary.data.mapper

import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import kotlinx.datetime.LocalDateTime

fun HabitEntity.toDailyLogEntity(date : LocalDateTime) : DailyLogEntity {
    return DailyLogEntity(
        habitId = this.id,
        createdAt = date,
        id = 0,
        logNote = "",
        imagePath = ""
    )
}