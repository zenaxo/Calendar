package com.hannessjolander.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.hannessjolander.calendar.ui.composable.DayCarousel
import com.hannessjolander.calendar.ui.composable.DayTimeline
import com.hannessjolander.calendar.ui.composable.Weekdays
import com.hannessjolander.calendar.ui.composable.rememberDayTimelineState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.abs

@Composable
fun TimelineActivity(viewModel: MainViewModel) {
    val activeActivities = viewModel.activeActivities.collectAsState()
    val activeDate = viewModel.activeDate.collectAsState()
    val activeDay = remember {
        derivedStateOf { viewModel.getDayString(activeDate.value.dayOfWeek) }
    }
    val dayTimelineState = rememberDayTimelineState()

    Column {
        Weekdays(
            activeDay = activeDay.value,
            setDay = { day: String ->
                viewModel.setActiveDate(findClosestWeekday(activeDate.value, viewModel.getDayOfWeek(day)))
            })
        DayCarousel(
            activeDate.value,
            setDay = { date: LocalDate ->
                viewModel.setActiveDate(date)
            }
        )
        DayTimeline(
            activeActivities.value,
            dayTimelineState,
            activeDate.value,
            openEdit = { activity ->
                viewModel.setActiveEdit(activity)
                viewModel.toggleEditSheet(true)
            }
        )

        LaunchedEffect(dayTimelineState) {
            val currentHour = LocalTime.now().hour
            dayTimelineState.scrollToHour(currentHour-2)
        }
    }
}

/**
 * Helper method to find the closest date in a range of +-3 days of the current date
 *
 * @param currentDate The current date
 * @param targetDayOfWeek The weekday
 *
 * @return the closest date if found, else the current date
 */
private fun findClosestWeekday(currentDate: LocalDate, targetDayOfWeek: DayOfWeek): LocalDate {
    val daysBefore = (1..3).map { currentDate.minusDays(it.toLong()) }
    val daysAfter = (1..3).map { currentDate.plusDays(it.toLong()) }
    val possibleDates = (daysBefore + currentDate + daysAfter) // Ordered list centered on currentDate

    return possibleDates.minByOrNull { abs(it.dayOfWeek.value - targetDayOfWeek.value) } ?: currentDate
}
