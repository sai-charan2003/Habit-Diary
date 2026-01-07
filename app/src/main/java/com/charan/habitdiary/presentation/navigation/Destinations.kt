package com.charan.habitdiary.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

sealed class BottomBarNavDestinations : NavKey{
    @Serializable
    object Home : BottomBarNavDestinations()

    @Serializable
    object Calender : BottomBarNavDestinations()

    @Serializable
    object Settings : BottomBarNavDestinations()
}

sealed class Destinations : NavKey {

    @Serializable
    data object BottomBarNav : Destinations()
    @Serializable
    data class AddHabit(val id : Int?) : Destinations()

    @Serializable
    data class AddDailyLog(val id : Int?, val date : LocalDate?,val openCameraOnLaunch : Boolean = false) : Destinations()

    @Serializable
    data object LibrariesScreenNav : Destinations()

    @Serializable
    data object OnBoardingScreenNav : Destinations()

    @Serializable
    data class ImageViewerScreenNav(val allImagePaths : List<String>,val currentImage : String) : Destinations()

    @Serializable
    data class HabitStatsScreeNav(val habitId : Int) : Destinations()

}