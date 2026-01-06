package com.charan.habitdiary.data.model.enums

import androidx.annotation.StringRes

enum class HabitSortType {
    ALL_HABITS,
    TODAY_HABITS;

    /**
     * Provides the string resource id for this sort type for localized display.
     *
     * @return The string resource id corresponding to the enum value: `R.string.all_habits` for ALL_HABITS or `R.string.today_habits` for TODAY_HABITS.
     */
    fun toLocaleString() : Int{
        return when(this){
            ALL_HABITS -> com.charan.habitdiary.R.string.all_habits
            TODAY_HABITS -> com.charan.habitdiary.R.string.today_habits
        }
    }
    companion object {
        /**
         * Maps a string resource id to its corresponding HabitSortType.
         *
         * @param resId The string resource id to look up.
         * @return The matching HabitSortType for the given resource id, or `ALL_HABITS` if no match is found.
         */
        fun fromRes(resId: Int): HabitSortType {
            return entries.firstOrNull { it.toLocaleString() == resId }
                ?: ALL_HABITS
        }
    }
}