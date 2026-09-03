package com.charan.habitdiary.presentation.root.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.dp
import com.charan.habitdiary.presentation.adddailylog.AddDailyLogScreen
import com.charan.habitdiary.presentation.addhabit.AddHabitScreen
import com.charan.habitdiary.presentation.allentries.AllEntriesScreen
import com.charan.habitdiary.presentation.habitstats.HabitStatsScreen
import com.charan.habitdiary.presentation.mediaviewer.MediaViewerScreen
import com.charan.habitdiary.presentation.onboarding.OnBoardingScreen
import com.charan.habitdiary.presentation.settings.SettingsScreen
import com.charan.habitdiary.presentation.settings.aboutlibraries.AboutLibrariesScreen

@Composable
fun RootNavigation(
    onBoardingCompleted : Boolean = true,
    deepLinkNavKey : List<NavKey>? = null,
    mediaList : List<Uri>? = null
) {
    val backStack = rememberNavBackStack(Destinations.BottomBarNav)
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
    LaunchedEffect(deepLinkNavKey, onBoardingCompleted, mediaList) {
        if (deepLinkNavKey != null) {
            backStack.clear()
            deepLinkNavKey.forEach { backStack.add(it) }
        } else {
            val currentIsOnboarding = backStack.contains(Destinations.OnBoardingScreenNav)
            if (!onBoardingCompleted && !currentIsOnboarding) {
                backStack.clear()
                backStack.add(Destinations.OnBoardingScreenNav)
            }
        }
    }
    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberPredictiveBackCornerNavEntryDecorator(maxCornerRadius = 32.dp),
        ),
        sceneStrategies = listOf(listDetailStrategy),

        predictivePopTransitionSpec = { swipeSide ->
            when (swipeSide) {
                0 -> {
                    fadeIn(
                        animationSpec = tween(
                            300,
                            easing = LinearOutSlowInEasing
                        )
                    ) togetherWith (
                            scaleOut(
                                targetScale = 0.85f,
                                transformOrigin = TransformOrigin(0.9f, 0.5f),
                                animationSpec = tween(
                                    300,
                                    easing = LinearOutSlowInEasing
                                )
                            ) +
                                    fadeOut(
                                        animationSpec = tween(
                                            300,
                                            easing = LinearOutSlowInEasing
                                        )
                                    )
                            )
                }

                1 -> {
                    fadeIn(
                        animationSpec = tween(
                            300,
                            easing = LinearOutSlowInEasing
                        )
                    ) togetherWith (
                            scaleOut(
                                targetScale = 0.85f,
                                transformOrigin = TransformOrigin(0.1f, 0.5f),
                                animationSpec = tween(
                                    300,
                                    easing = LinearOutSlowInEasing
                                )
                            ) +
                                    fadeOut(
                                        animationSpec = tween(
                                            300,
                                            easing = LinearOutSlowInEasing
                                        )
                                    )
                            )
                }

                else -> {
                    scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(300)
                    ) +
                            slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(300)
                            ) +
                            fadeIn(
                                animationSpec = tween(300)
                            ) togetherWith (
                            scaleOut(
                                targetScale = 0.85f,
                                animationSpec = tween(300)
                            ) +
                                    fadeOut(
                                        animationSpec = tween(300)
                                    )
                            )
                }
            }
        },
        popTransitionSpec = {
            (
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                )
            ) togetherWith (
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                ) + fadeOut(
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                )
            )
        },
        transitionSpec = {
            (
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                )
            ) togetherWith (
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                ) + fadeOut(
                    animationSpec = tween(300, easing = LinearOutSlowInEasing)
                )
            )
        },

        entryProvider = { key->
            when(key){
                is Destinations.BottomBarNav -> NavEntry(
                    key,
                    metadata = ListDetailScene.listPane()
                ){
                    BottomBarNavigation(
                        onAddHabitNav = {
                            backStack.add(Destinations.AddHabit(id = it))
                        },
                        onAddDailyLogNav = { id, date->
                            backStack.add(Destinations.AddDailyLog(id = id, date = date))
                        },
                        onImageOpen = { allImages, currentImage, showLogEntryButton ->
                            backStack.add(Destinations.ImageViewerScreenNav(allImages,currentImage, showLogEntryButton))
                        },
                        onHabitStats = { habitId ->
                            backStack.add(Destinations.HabitStatsScreeNav(habitId))
                        },
                        onNavigateToAllEntries = {
                            backStack.add(Destinations.AllEntriesScreenNav)
                        },
                        onNavigateToSettings = {
                            backStack.add(Destinations.SettingsScreenNav)
                        }

                    )
                }
                is Destinations.AddHabit -> NavEntry(
                    key,
                    metadata = ListDetailScene.detailPane()
                ){
                    AddHabitScreen(
                        onNavigateBack = { isDeleted ->
                            if (isDeleted) {
                                backStack.removeIf { it is Destinations.HabitStatsScreeNav }
                            }
                            backStack.removeLastOrNull()
                        },
                        key.id
                    )
                }
                is Destinations.AddDailyLog -> NavEntry(
                    key,
                    metadata = ListDetailScene.detailPane()
                    ){
                    AddDailyLogScreen(
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        },
                        logId = key.id,
                        onImageOpen = { allImagesPaths, currentImage, showLogEntryButton ->
                            backStack.add(Destinations.ImageViewerScreenNav(
                                allImagesPaths,
                                currentImage,
                                showLogEntryButton
                            ))
                        },
                        onHabitOpen = {
                            backStack.add(Destinations.HabitStatsScreeNav(it))
                        },
                        date = key.date,
                        openImageCaptureOnLaunch = key.openCaptureImageOnLaunch,
                        openVideoRecordingOnLaunch = key.openCaptureVideoOnLaunch,
                        sharedMedia = key.mediaList

                    )
                }
                is Destinations.LibrariesScreenNav -> NavEntry(
                    key,
                    metadata = ListDetailScene.detailPane()
                    ){
                    AboutLibrariesScreen(
                        onBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                is Destinations.OnBoardingScreenNav -> NavEntry(key){
                    OnBoardingScreen {
                        backStack.removeLastOrNull()
                        backStack.add(Destinations.BottomBarNav)

                    }
                }

                is Destinations.ImageViewerScreenNav -> NavEntry(key){
                    MediaViewerScreen(
                        allImages = key.allMedia,
                        currentImage = key.currentMedia,
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        showLogEntryButton = key.showLogEntryButton,
                        onNavigateToDailyLog = { logId, date ->
                            backStack.add(Destinations.AddDailyLog(logId, date))
                        }
                    )
                }

                is Destinations.AllEntriesScreenNav -> NavEntry(
                    key,
                    metadata = ListDetailScene.detailPane()
                ) {
                    AllEntriesScreen(
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        onNavigateToDailyLog = { id ->
                            backStack.add(Destinations.AddDailyLog(id,null))
                        },
                        onNavigateToImageViewer = { allImages, currentImage, showLogEntryButton ->
                            backStack.add(Destinations.ImageViewerScreenNav(allImages, currentImage, showLogEntryButton))
                        }
                    )
                }

                is Destinations.HabitStatsScreeNav -> NavEntry(
                    key,
                    metadata = ListDetailScene.detailPane()
                    ){
                    HabitStatsScreen(
                        habitId = key.habitId,
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        },
                        onAddLog = {
                            backStack.add(Destinations.AddDailyLog(it, null))
                        },
                        onEditHabit = {
                            backStack.add(Destinations.AddHabit(it))
                        }
                    )
                }

                is Destinations.SettingsScreenNav -> NavEntry(
                    key,
                    metadata = ListDetailScene.detailPane()
                ) {
                    SettingsScreen(
                        navigateToAboutLibraries = {
                            backStack.add(Destinations.LibrariesScreenNav)
                        },
                        onBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                else -> NavEntry(key) { Text("Unknown route") }
            }

        }
    )
}

