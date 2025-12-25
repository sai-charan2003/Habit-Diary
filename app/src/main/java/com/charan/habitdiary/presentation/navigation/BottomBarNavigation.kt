package com.charan.habitdiary.presentation.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.charan.habitdiary.R
import com.charan.habitdiary.presentation.calendar.LogCalendarScreen
import com.charan.habitdiary.presentation.home.HomeScreen
import com.charan.habitdiary.presentation.settings.SettingsScreen

@Composable
fun BottomBarNavigation(
    onAddHabitNav : (Int?) -> Unit,
    onAddDailyLogNav : (Int?) -> Unit,
    onNavigateToAboutLibraries : () -> Unit,
    onImageOpen : (allImage : List<String>,currentImage : String) -> Unit,

) {
    val bottomBarBackStack = rememberNavBackStack(BottomBarNavDestinations.Home)
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }
    var previousSelectedItem by rememberSaveable { mutableIntStateOf(0) }

    val navSuiteType =
        NavigationSuiteScaffoldDefaults.navigationSuiteType(currentWindowAdaptiveInfo())
    val (entryAnimation, exitAnimation) = remember(
        selectedItem,
        previousSelectedItem,
        navSuiteType
    ) {
        val forward = selectedItem > previousSelectedItem
        val enter = when (navSuiteType) {
            NavigationSuiteType.ShortNavigationBarCompact,
            NavigationSuiteType.ShortNavigationBarMedium -> {
                slideInHorizontally(
                    initialOffsetX = { if (forward) it else -it },
                    animationSpec = tween(250, easing = LinearOutSlowInEasing)
                )
            }

            NavigationSuiteType.WideNavigationRailCollapsed -> {
                slideInVertically(
                    initialOffsetY = { if (forward) it else -it },
                    animationSpec = tween(250, easing = LinearOutSlowInEasing)
                )
            }

            else -> fadeIn(animationSpec = tween(250))
        }

        val exit = when (navSuiteType) {
            NavigationSuiteType.ShortNavigationBarCompact,
            NavigationSuiteType.ShortNavigationBarMedium -> {
                slideOutHorizontally(
                    targetOffsetX = { if (forward) -it else it },
                    animationSpec = tween(250, easing = LinearOutSlowInEasing)
                )
            }

            NavigationSuiteType.WideNavigationRailCollapsed -> {
                slideOutVertically(
                    targetOffsetY = { if (forward) -it else it },
                    animationSpec = tween(250, easing = LinearOutSlowInEasing)
                )
            }

            else -> fadeOut(animationSpec = tween(250))
        }

        enter to exit
    }

    LaunchedEffect(selectedItem) {
        when (BottomNavItem.entries[selectedItem]) {
            BottomNavItem.HOME -> {
                bottomBarBackStack.clear()
                bottomBarBackStack.add(BottomBarNavDestinations.Home)
            }


            BottomNavItem.CALENDAR -> {
                bottomBarBackStack.clear()
                bottomBarBackStack.add(BottomBarNavDestinations.Calender)
            }

            BottomNavItem.SETTINGS -> {
                bottomBarBackStack.clear()
                bottomBarBackStack.add(BottomBarNavDestinations.Settings)
            }

        }
    }

    NavigationSuiteScaffold(
        navigationSuiteType = navSuiteType,
        navigationItems = {
            BottomNavItem.entries.mapIndexed { index, item ->
                NavigationSuiteItem(
                    selected = index == selectedItem,
                    onClick = {
                        previousSelectedItem = selectedItem
                        selectedItem = index
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItem) item.selectedIcon else item.unselectedIcon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(stringResource(item.title))
                    }
                )
            }

        }
    ) {
        NavDisplay(
            backStack = bottomBarBackStack,
            onBack = { bottomBarBackStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            transitionSpec = {
                entryAnimation togetherWith exitAnimation
            },
            entryProvider = { key ->
                when (key) {
                    is BottomBarNavDestinations.Home -> NavEntry(key) {
                        HomeScreen(
                            onAddHabitClick = { id->
                                onAddHabitNav(
                                    id
                                )

                            },
                            onAddDailyLog = { id->
                                onAddDailyLogNav(
                                    id
                                )

                            },
                            onImageOpen = { allImages, currentImage ->
                                onImageOpen(
                                    allImages,
                                    currentImage

                                )
                            }

                        )
                    }

                    is BottomBarNavDestinations.Calender -> NavEntry(key){
                        LogCalendarScreen(
                            onNavigateToDailyLogScreen = {
                                onAddDailyLogNav(
                                    it
                                )
                            },
                            onImageOpen = { allImages, currentImage ->
                                onImageOpen(
                                    allImages,
                                    currentImage
                                )
                            }
                        )
                    }

                    is BottomBarNavDestinations.Settings -> NavEntry(key) {
                        SettingsScreen(
                            navigateToAboutLibraries = {
                                onNavigateToAboutLibraries()

                            }
                        )
                    }
                    else -> NavEntry(key) { Text("Unknown route") }

                }
            }


        )
    }
}

enum class BottomNavItem(
    val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(
        title = R.string.home,
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,

        ),
    CALENDAR(
        title = R.string.calendar,
        selectedIcon = Icons.Rounded.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
    ),
    SETTINGS(
        title = R.string.settings,
        selectedIcon = Icons.Rounded.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    )
}