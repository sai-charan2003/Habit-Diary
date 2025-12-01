package com.charan.habitdiary.presentation.home

import com.charan.habitdiary.presentation.common.model.DailyLogItemUIState

data class HomeScreenState(
    val habits : List<HabitItemUIState> = emptyList(),
    val dailyLogs : List<DailyLogItemUIState> = emptyList(),
    val isLoading : Boolean = false,
    val isFabExpanded : Boolean = false,
    val todayDate : String = ""
)

data class HabitItemUIState(
    val id : Int,
    val habitName : String,
    val habitDescription : String,
    val habitTime : String,
    val isDone : Boolean = false,
    val logId  : Int?
)


