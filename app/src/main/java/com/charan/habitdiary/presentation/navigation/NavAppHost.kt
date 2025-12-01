package com.charan.habitdiary.presentation.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.charan.habitdiary.presentation.add_daily_log.AddDailyLogScreen
import com.charan.habitdiary.presentation.add_habit.AddHabitScreen
import com.charan.habitdiary.presentation.on_boarding.OnBoardingScreen
import com.charan.habitdiary.presentation.settings.about_libraries.AboutLibrariesScreen

@Composable
fun RootNavigation(
    onBoardingCompleted : Boolean = true
) {
    val backStack = rememberNavBackStack(Destinations.BottomBarNav)
    LaunchedEffect(onBoardingCompleted) {
        if(onBoardingCompleted){
            backStack.add(Destinations.BottomBarNav)
        } else {
            backStack.add(Destinations.OnBoardingScreenNav)
        }
    }
    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key->
            when(key){
                is Destinations.BottomBarNav -> NavEntry(key){
                    BottomBarNavigation(
                        onAddHabitNav = {
                            backStack.add(Destinations.AddHabit(id = it))
                        },
                        onAddDailyLogNav = {
                            backStack.add(Destinations.AddDailyLog(id = it))
                        },
                        onNavigateToAboutLibraries = {
                            backStack.add(Destinations.LibrariesScreenNav)
                        }

                    )
                }
                is Destinations.AddHabit -> NavEntry(key){
                    AddHabitScreen(
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        },
                        key.id
                    )
                }
                is Destinations.AddDailyLog -> NavEntry(key){
                    AddDailyLogScreen(
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        },
                        logId = key.id
                    )
                }
                is Destinations.LibrariesScreenNav -> NavEntry(key){
                    AboutLibrariesScreen(
                        onBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                is Destinations.OnBoardingScreenNav -> NavEntry(key){
                    OnBoardingScreen {
                        backStack.add(Destinations.BottomBarNav)
                    }
                }
                else -> NavEntry(key) { Text("Unknown route") }
            }

        }
    )
}

