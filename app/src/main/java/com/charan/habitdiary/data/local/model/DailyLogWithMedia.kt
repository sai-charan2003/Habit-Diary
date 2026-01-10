package com.charan.habitdiary.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.charan.habitdiary.data.local.entity.DailyLogEntity
import kotlinx.serialization.Serializable

@Serializable
data class DailyLogWithMedia(
    @Embedded val dailyLogEntity : DailyLogEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "dailyLogId"
    )
    val mediaEntities : List<DailyLogWithMedia>
)
