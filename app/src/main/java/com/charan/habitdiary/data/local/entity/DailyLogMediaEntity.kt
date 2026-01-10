package com.charan.habitdiary.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "daily_log_media_entity",
    foreignKeys = [
        ForeignKey(
            entity = DailyLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["dailyLogId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["dailyLogId"])
    ]
)

data class DailyLogMediaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dailyLogId: Long,
    val mediaPath: String,
    val isDeleted : Boolean = false
)
