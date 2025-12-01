package com.charan.habitdiary.presentation.calendar

import com.charan.habitdiary.presentation.common.model.DailyLogItemUIState
import com.charan.habitdiary.utils.DateUtil
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.minusYears
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlin.time.ExperimentalTime
@OptIn(ExperimentalTime::class)
data class LogCalendarState(
    val selectedDate : LocalDate = DateUtil.getCurrentDate(),
    val selectedCalendarView : CalendarViewType = CalendarViewType.WEEK,
    val currentMonth : YearMonth  = YearMonth.now(),
    val startOfMonth : YearMonth = currentMonth.minusYears(100),
    val endOfMonth : YearMonth = currentMonth.plusMonths(1),
    val currentDate : LocalDate = LocalDate.now(),
    val startOfDate : LocalDate = currentDate.minusMonths(100),
    val endOfDate : LocalDate = currentDate.plusMonths(1),
    val dailyLogItem : List<DailyLogItemUIState> = emptyList()
)


enum class CalendarViewType{
    WEEK,
    MONTH
}
