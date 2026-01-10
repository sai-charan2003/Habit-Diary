package com.charan.habitdiary.data.model.enums

import androidx.annotation.StringRes

enum class HabitSortType {
    ALL_HABITS,
    TODAY_HABITS;

    fun toLocaleString() : Int{
        return when(this){
            ALL_HABITS -> com.charan.habitdiary.R.string.all_habits
            TODAY_HABITS -> com.charan.habitdiary.R.string.today_habits
        }
    }
    companion object {
        fun fromRes(resId: Int): HabitSortType {
            return entries.firstOrNull { it.toLocaleString() == resId }
                ?: ALL_HABITS
        }
    }
}