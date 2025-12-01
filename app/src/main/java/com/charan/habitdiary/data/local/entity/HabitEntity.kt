package com.charan.habitdiary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "habit_entity")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val habitName : String,
    val habitReminder : Long,
    val createdAt : Long,
    val habitDescription : String,
    val habitTime : Long,
    val habitFrequency : String,
    val isDeleted : Boolean = false,
    val isReminderEnabled : Boolean = false
)
