package com.charan.habitdiary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.charan.habitdiary.data.local.Converters
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "habit_entity")
@TypeConverters(
    Converters::class
)
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val habitName : String,
    val habitReminder : LocalTime?,
    val createdAt : LocalDateTime,
    val habitDescription : String,
    val habitTime : LocalTime,
    val habitFrequency : List<DayOfWeek>,
    val isDeleted : Boolean = false,
    val isReminderEnabled : Boolean = false
)
