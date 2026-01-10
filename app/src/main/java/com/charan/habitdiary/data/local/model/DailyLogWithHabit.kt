package com.charan.habitdiary.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.charan.habitdiary.data.local.entity.DailyLogEntity
import com.charan.habitdiary.data.local.entity.DailyLogMediaEntity
import com.charan.habitdiary.data.local.entity.HabitEntity
import kotlinx.serialization.Serializable

@Serializable
data class DailyLogWithHabit(
    @Embedded val dailyLogEntity: DailyLogEntity,

    @Relation(
        parentColumn = "habitId",
        entityColumn = "id"
    )
    val habitEntity: HabitEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "dailyLogId"
    )
    val mediaEntities : List<DailyLogMediaEntity>
)

