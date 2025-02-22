package com.hannessjolander.calendar.ui.composable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hannessjolander.calendar.model.CalendarActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Manages the scroll state and provides a function to scroll to a specific hour.
 * @param scrollState The scroll state of the timeline.
 * @param coroutineScope The coroutine scope for handling scrolling actions.
 * @param stepPx The pixel height of each hour step in the timeline.
 */
class DayTimelineState internal constructor(
    val scrollState: ScrollState,
    private val coroutineScope: CoroutineScope,
    private val stepPx: Float
) {
    /**
     * Scrolls smoothly to the given hour.
     * @param hour The hour to scroll to (1-24).
     */
    fun scrollToHour(hour: Int) {
        coroutineScope.launch {
            scrollState.animateScrollTo((hour * stepPx).toInt())
        }
    }
}

/**
 * Creates and remembers a [DayTimelineState] with a given step size for scrolling.
 * @param stepSize The height of each hour in pixels (default is 100dp).
 * @return A remembered instance of [DayTimelineState].
 */
@Composable
fun rememberDayTimelineState(stepSize: Int = 100): DayTimelineState {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    // Convert stepSize dp to pixels
    val stepPx = with(density) { stepSize.dp.toPx() }
    return remember { DayTimelineState(scrollState, coroutineScope, stepPx) }
}

/**
 * Displays a timeline for a day, showing scheduled activities.
 * @param activities List of calendar activities to be displayed.
 * @param state The timeline's scroll state.
 * @param activeDay The currently active date being displayed.
 * @param openEdit A callback function for opening the edit screen for an activity.
 */
@Composable
fun DayTimeline(
    activities: List<CalendarActivity>,
    state: DayTimelineState = rememberDayTimelineState(),
    activeDay: LocalDate,
    openEdit: (CalendarActivity) -> Unit
) {
    val stepSize = 100 // Height for one hour in pixels
    val hours = (1..24).map { if (it < 10) "0$it" else "$it" }

    // Find overlapping groups to adjust UI layout
    val overlappingGroups = findOverlappingGroups(activities)

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(20.dp, 0.dp)
    ) {
        Column(modifier = Modifier.verticalScroll(state.scrollState)) {
            hours.forEach { hour ->
                Box(modifier = Modifier.height(stepSize.dp)) {
                    Text(
                        text = "$hour:00",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Transparent, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .size(16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
                    .width(2.dp)
            )
        }
        var parentWidthDp by remember { mutableFloatStateOf(0f) }
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    parentWidthDp = coordinates.size.width / density.density
                }
                .verticalScroll(state.scrollState)
                .height((stepSize * hours.size).dp)
        ) {
            activities.forEachIndexed { i, activity ->
                val (numOverlaps, order) = getActivityOverlapData(activity, overlappingGroups)
                ActivityCard(activity, stepSize, numOverlaps, order, parentWidthDp, isEven = i % 2 == 0, activeDay, openEdit)
            }
        }
    }
}

/**
 * Groups activities that overlap in time to help with layout adjustments.
 * @param activities List of calendar activities to be analyzed for overlaps.
 * @return A list of overlapping groups.
 */
fun findOverlappingGroups(activities: List<CalendarActivity>): List<List<CalendarActivity>> {
    val groups = mutableListOf<MutableList<CalendarActivity>>()

    for (activity in activities) {
        var added = false
        for (group in groups) {
            if (group.any { isOverlapping(it, activity) }) {
                group.add(activity)
                added = true
                break
            }
        }
        if (!added) {
            groups.add(mutableListOf(activity))
        }
    }
    return groups
}

/**
 * Determines if two activities overlap based on their start and end times.
 * @param a First activity.
 * @param b Second activity.
 * @return True if the activities overlap, false otherwise.
 */
fun isOverlapping(a: CalendarActivity, b: CalendarActivity): Boolean {
    val aStart = a.startTime.hour * 60 + a.startTime.minute
    val aEnd = aStart + a.getDurationInMinutes()
    val bStart = b.startTime.hour * 60 + b.startTime.minute
    val bEnd = bStart + b.getDurationInMinutes()
    return (aStart < bEnd && bStart < aEnd)
}

/**
 * Gets the number of overlapping activities and determines the position of a specific activity within its group.
 * @param activity The activity to check.
 * @param groups List of activity groups that overlap.
 * @return A pair containing the number of overlapping activities and the order of the current activity.
 */
fun getActivityOverlapData(activity: CalendarActivity, groups: List<List<CalendarActivity>>): Pair<Int, Int> {
    for (group in groups) {
        if (group.contains(activity)) {
            return Pair(group.size, group.indexOf(activity))
        }
    }
    return Pair(1, 0) // Default if no overlap found
}
