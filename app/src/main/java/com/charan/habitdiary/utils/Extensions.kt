package com.charan.habitdiary.utils

import android.content.Context
import android.widget.Toast
import com.charan.habitdiary.presentation.common.model.ToastMessage

fun String.isVideo(): Boolean {
    return endsWith(".mp4", true) ||
            endsWith(".mov", true) ||
            endsWith(".mkv", true) ||
            endsWith(".webm", true)
}

fun Context.showToast(toastMessage: ToastMessage){
    when(toastMessage){
        is ToastMessage.Res -> {
            Toast.makeText(
                this,
                toastMessage.resId,
                Toast.LENGTH_SHORT
            ).show()
        }
        is ToastMessage.Text -> {
            Toast.makeText(
                this,
                toastMessage.text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
