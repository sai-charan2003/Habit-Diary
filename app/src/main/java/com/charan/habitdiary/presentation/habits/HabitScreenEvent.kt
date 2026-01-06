package com.charan.habitdiary.presentation.habits

import com.charan.habitdiary.data.model.enums.HabitSortType

sealed class HabitScreenEvent {
    data object OnFabExpandToggle : HabitScreenEvent()

    data object OnAddHabitClick : HabitScreenEvent()
    data object OnAddDailyLogClick : HabitScreenEvent()

    data class OnHabitStatsScreen(val id : Int) : HabitScreenEvent()

    data class OnHabitCheckToggle(val habit : HabitItemUIState, val isChecked : Boolean) : HabitScreenEvent()

    data class OnDailyLogEdit(val id : Int) : HabitScreenEvent()

    data class OnSortTypeChange(val sortType : HabitSortType) : HabitScreenEvent()

    data object OnSortDropDownToggle : HabitScreenEvent()


}