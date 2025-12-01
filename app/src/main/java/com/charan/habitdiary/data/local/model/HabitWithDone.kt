package com.charan.habitdiary.data.local.model

import androidx.room.Embedded
import com.charan.habitdiary.data.local.entity.HabitEntity
import kotlinx.serialization.Serializable

@Serializable
data class HabitWithDone(
    @Embedded val habitEntity: HabitEntity,
    val isDone: Boolean,
    val logId : Int?
)
