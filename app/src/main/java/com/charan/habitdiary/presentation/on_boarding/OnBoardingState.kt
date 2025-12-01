package com.charan.habitdiary.presentation.on_boarding

import com.charan.habitdiary.R

data class OnBoardingState(
    val currentPage : Int = 0,
    val onBoardingPage : List<OnBoardingPage> = pages
)

private val pages = listOf(
    OnBoardingPage(
        title = "Welcome to Habit Diary",
        description = "Your personal space to build habits and capture daily moments.",
        imageRes = R.drawable.app_logo
    ),
    OnBoardingPage(
        title = "Track your Habits",
        description = "Build meaningful habits while capturing your habit journey.",
        imageRes = R.drawable.habit
    ),
    OnBoardingPage(
        title = "Capture Every Moment",
        description = "Log your daily activities with notes and images.",
        imageRes = R.drawable.book
    )
)

data class OnBoardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)
