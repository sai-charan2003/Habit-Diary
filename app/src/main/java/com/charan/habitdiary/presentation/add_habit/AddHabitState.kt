package com.charan.habitdiary.presentation.add_habit

data class AddHabitState(
    val habitTitle : String = "",
    val habitDescription : String = "",
    val habitReminderTime : Time? = Time(),
    val formatedReminderTime : String = "08:00",
    val isLoading : String = "",
    val habitTime : Time = Time(),
    val formatedHabitTime : String = "08:00",
    val habitFrequency : List<Int> = listOf(1,2,3,4,5),
    val showHabitTimeDialog : Boolean = false,
    val showReminderTimeDialog : Boolean = false,
    val isReminderEnabled : Boolean = false,
    val showPermissionRationale : Boolean = false,
    val showDeleteDialog : Boolean = false,
    val habitId : Int? = null,
    val isEdit : Boolean = false,
    val is24HourFormat : Boolean = false
)

data class Time(
    val hour : Int = 8,
    val minutes : Int = 0
)
