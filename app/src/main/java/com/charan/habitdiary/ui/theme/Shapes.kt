package com.charan.habitdiary.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun roundedListItemCorners(
    indexItem: IndexItem,
    topBottomRadius: Dp = 16.dp,
    middleRadius: Dp = 4.dp
): RoundedCornerShape {
    val shape = when (indexItem) {
        IndexItem.FIRST -> RoundedCornerShape(
            topStart = topBottomRadius,
            topEnd = topBottomRadius,
            bottomStart = middleRadius,
            bottomEnd = middleRadius
        )
        IndexItem.LAST -> RoundedCornerShape(
            topStart = middleRadius,
            topEnd = middleRadius,
            bottomStart = topBottomRadius,
            bottomEnd = topBottomRadius
        )

        IndexItem.MIDDLE -> {
            RoundedCornerShape(
                topStart = middleRadius,
                topEnd = middleRadius,
                bottomStart = middleRadius,
                bottomEnd = middleRadius
            )
        }

        IndexItem.FIRST_AND_LAST -> {
            RoundedCornerShape(
                topStart = topBottomRadius,
                topEnd = topBottomRadius,
                bottomStart = topBottomRadius,
                bottomEnd = topBottomRadius
            )
        }
    }

    return shape
}

fun <T> List<T>.indexItemFor(index: Int): IndexItem {
    return when {
        size == 1 -> IndexItem.FIRST_AND_LAST
        index == 0 -> IndexItem.FIRST
        index == size - 1 -> IndexItem.LAST
        else -> IndexItem.MIDDLE
    }
}


enum class IndexItem {
    FIRST,
    LAST,
    MIDDLE,

    FIRST_AND_LAST
}