package com.charan.habitdiary.presentation.root.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImportContacts
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.rounded.ImportContacts
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
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
import com.charan.habitdiary.presentation.diary.DiaryScreen
import com.charan.habitdiary.presentation.habits.HabitScreen
import com.charan.habitdiary.presentation.settings.SettingsScreen
import com.charan.habitdiary.presentation.common.model.MediaItemUIModel
import com.charan.habitdiary.presentation.journey.JourneyScreen
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.ToggleButtonShapes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import kotlinx.datetime.LocalDate

@Composable
fun BottomBarNavigation(
    onAddHabitNav : (Long?) -> Unit,
    onAddDailyLogNav : (id : Long? , date : LocalDate?) -> Unit,
    onNavigateToAboutLibraries : () -> Unit,
    onImageOpen : (allImage : List<MediaItemUIModel>,currentImage : MediaItemUIModel, showLogEntryButton: Boolean) -> Unit,
    onHabitStats : (id : Long) -> Unit,
    onNavigateToAllEntries : () -> Unit,
) {
    val bottomBarBackStack = rememberNavBackStack(BottomBarNavDestinations.Home)
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }
    var previousSelectedItem by rememberSaveable { mutableIntStateOf(0) }

    val navSuiteType =
        NavigationSuiteScaffoldDefaults.navigationSuiteType(currentWindowAdaptiveInfoV2())
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

            BottomNavItem.JOURNEY -> {
                bottomBarBackStack.clear()
                bottomBarBackStack.add(BottomBarNavDestinations.Journey)
            }

        }
    }
    val isJourneySelected = BottomNavItem.entries[selectedItem] == BottomNavItem.JOURNEY
    val toolbarContent: @Composable RowScope.() -> Unit = {
        BottomNavItem.entries.fastForEachIndexed { index, item ->
            val isSelected = index == selectedItem
            ToggleButton(
                checked = isSelected,
                onCheckedChange = {
                    if (selectedItem != index) {
                        selectedItem = index
                        previousSelectedItem = selectedItem

                    }
                },
                shapes = ToggleButtonShapes(CircleShape, CircleShape, CircleShape),
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = item.selectedIcon,
                        contentDescription = stringResource(item.title)
                    )
                }
                Text(
                    text = stringResource(item.title),
                    modifier = Modifier.padding(horizontal = 8.dp)

                )

            }
        }
    }
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (isJourneySelected) {
                    HorizontalFloatingToolbar(
                        expanded = true,
                        modifier = Modifier
                            .navigationBarsPadding(),
                        content = toolbarContent
                    )
                } else {
                    HorizontalFloatingToolbar(
                        expanded = true,
                        modifier = Modifier
                            .navigationBarsPadding(),
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    when (BottomNavItem.entries[selectedItem]) {
                                        BottomNavItem.HOME -> {
                                            onAddHabitNav(
                                                null
                                            )
                                        }

                                        BottomNavItem.CALENDAR -> {
                                            onAddDailyLogNav(
                                                null,
                                                null
                                            )
                                        }

                                        else -> {}
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = stringResource(R.string.add_entry)
                                )
                            }
                        },
                        content = toolbarContent
                    )
                }
            }
        }
    ) { innerPadding->
        NavDisplay(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
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
                        HabitScreen(
                            onHabitDetails = { id ->
                                onAddHabitNav(
                                    id
                                )

                            },
                            onAddDailyLog = { id ->
                                onAddDailyLogNav(
                                    id,
                                    null
                                )

                            },
                            onHabitStats = { id ->
                                onHabitStats(
                                    id
                                )
                            }

                        )
                    }

                    is BottomBarNavDestinations.Calender -> NavEntry(key) {
                        DiaryScreen(
                            onNavigateToDailyLogScreen = { id, date ->
                                onAddDailyLogNav(
                                    id,
                                    date
                                )
                            },
                            onImageOpen = { allImages, currentImage, showLogEntryButton ->
                                onImageOpen(
                                    allImages,
                                    currentImage,
                                    showLogEntryButton
                                )
                            },
                            onNavigateToAllEntries = {
                                onNavigateToAllEntries()
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

                    is BottomBarNavDestinations.Journey -> NavEntry(key) {
                        JourneyScreen(
                            onImageClick = { allImages, currentImage, showLogEntryButton ->
                                onImageOpen(
                                    allImages,
                                    currentImage,
                                    showLogEntryButton
                                )
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
        title = R.string.habits,
        selectedIcon = Icons.Rounded.TaskAlt,
        unselectedIcon = Icons.Outlined.TaskAlt,

        ),
    CALENDAR(
        title = R.string.diary,
        selectedIcon = Icons.Rounded.ImportContacts,
        unselectedIcon = Icons.Outlined.ImportContacts,
    ),
    JOURNEY(
        title = R.string.profile,
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
}