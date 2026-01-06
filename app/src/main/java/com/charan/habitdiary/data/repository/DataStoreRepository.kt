package com.charan.habitdiary.data.repository

import com.charan.habitdiary.data.model.enums.HabitSortType
import com.charan.habitdiary.data.model.enums.ThemeOption
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun setTheme(theme : ThemeOption)

    val getTheme : Flow<ThemeOption>

    suspend fun setDynamicColorsState(isEnabled : Boolean)

    val getDynamicColorsState : Flow<Boolean>

    suspend fun setIs24HourFormat(is24HourFormat : Boolean)

    val getIs24HourFormat : Flow<Boolean>

    suspend fun setOnBoardingCompleted(isCompleted : Boolean)

    val getOnBoardingCompleted : Flow<Boolean>

    /**
 * Store the user's preference for using the system font.
 *
 * @param useSystemFont `true` to use the device's system font, `false` to use the app's default font.
 */
suspend fun setSystemFontState(useSystemFont : Boolean)

    val getSystemFontState : Flow<Boolean>

    val habitSortType : Flow<HabitSortType>

    /**
 * Updates the stored habit sorting preference to the provided value.
 *
 * @param sortType The HabitSortType to persist as the new habit sorting preference.
 */
suspend fun setHabitSortType(sortType : HabitSortType)
}