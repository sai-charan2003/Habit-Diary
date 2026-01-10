package com.charan.habitdiary.presentation.common.model

sealed class ToastMessage {
    data class Text(val text: String) : ToastMessage()
    data class Res(val resId: Int) : ToastMessage()
}