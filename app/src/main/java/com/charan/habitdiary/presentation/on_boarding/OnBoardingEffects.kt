package com.charan.habitdiary.presentation.on_boarding

sealed class OnBoardingEffects {
    data class OnScrollToPage(val page: Int) : OnBoardingEffects()
    object NavigateToHome : OnBoardingEffects()
}