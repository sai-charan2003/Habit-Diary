package com.charan.habitdiary.presentation.home

sealed class HomeScreenEvent {
    data object OnFabExpandToggle : HomeScreenEvent()

    data object OnAddHabitClick : HomeScreenEvent()
    data object OnAddDailyLogClick : HomeScreenEvent()

    data class OnEditHabit(val id : Int? ) : HomeScreenEvent()

    data class OnHabitCheckToggle(val habit : HabitItemUIState, val isChecked : Boolean) : HomeScreenEvent()

    data class OnDailyLogEdit(val id : Int) : HomeScreenEvent()
}