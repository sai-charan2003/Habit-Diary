package com.charan.habitdiary.presentation.common.model

data class DailyLogItemUIState(
    val id : Long,
    val logNote : String,
    val mediaPaths : List<String>,
    val createdAt : String,
    val habitId : Long?,
    val habitName : String?
)