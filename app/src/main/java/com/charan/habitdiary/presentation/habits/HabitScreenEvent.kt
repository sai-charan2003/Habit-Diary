package com.charan.habitdiary.presentation.habits

sealed class HabitScreenEvent {
    data object OnFabExpandToggle : HabitScreenEvent()

    data object OnAddHabitClick : HabitScreenEvent()
    data object OnAddDailyLogClick : HabitScreenEvent()

    data class OnHabitStatsScreen(val id : Int) : HabitScreenEvent()

    data class OnHabitCheckToggle(val habit : HabitItemUIState, val isChecked : Boolean) : HabitScreenEvent()

    data class OnDailyLogEdit(val id : Int) : HabitScreenEvent()


}