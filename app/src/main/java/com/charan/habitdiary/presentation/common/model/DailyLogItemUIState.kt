package com.charan.habitdiary.presentation.common.model

data class DailyLogItemUIState(
    val id : Int,
    val logNote : String,
    val mediaPaths : List<String>,
    val createdAt : String,
    val habitId : Int?,
    val habitName : String?
)