package com.charan.habitdiary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "daily_log_entity")
data class DailyLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val logNote : String,
    val imagePath : String,
    val createdAt : Long,
    val isDeleted : Boolean = false,
    val habitId : Int? = null
)
