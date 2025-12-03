package com.charan.habitdiary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.charan.habitdiary.data.local.Converters
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
@TypeConverters(
    Converters::class
)
@Entity(tableName = "daily_log_entity")
data class DailyLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val logNote : String,
    val imagePath : String,
    val createdAt : LocalDateTime,
    val isDeleted : Boolean = false,
    val habitId : Int? = null
)
