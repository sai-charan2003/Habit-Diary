package com.charan.habitdiary.utils

fun String.isVideo(): Boolean {
    return endsWith(".mp4", true) ||
            endsWith(".mov", true) ||
            endsWith(".mkv", true) ||
            endsWith(".webm", true)
}
