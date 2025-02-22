package com.hannessjolander.calendar.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hannessjolander.calendar.model.CalendarActivity
import java.time.LocalDate

/**
 * A card composable which displays a calendar activity
 *
 * @param openEdit Returns the given activity as callback
 * @param activeDay The visible day on the calender
 * @param numOverlaps The number of activities present along the same time frame
 * @param order In case of overlapping activities, which order on the horizontal axis should the
 * activity be placed
 * @param parentWidthDp The width of the parent element in dp.
 * Needed to calculate the offset for overlapping activities
 * @param isEven If the index of activity is even
 * @param stepSize The height in dp for each hour on the timeline
 * @param activity The activity to display
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActivityCard(
    activity: CalendarActivity,
    stepSize: Int,
    numOverlaps: Int,
    order: Int,
    parentWidthDp: Float,
    isEven: Boolean,
    activeDay: LocalDate,
    openEdit: (CalendarActivity) -> Unit
) {
    val cardPadding = 5.dp
    // Calculate the height of the activity depending on its duration
    val height = (stepSize * (activity.getDurationInMinutes() / 60f)).dp
    // Get where on the timeline the activity should be
    val yOffset = (stepSize * ((activity.startTime.hour - 1) + activity.startTime.minute / 60f)).dp
    // Card width will depend on how many activities overlap on the given time
    val cardWidth = (parentWidthDp / numOverlaps).dp
    // Offset the activity if there are overlaps
    val xOffset = cardWidth*order

    /**
     * Sets the background color of the activity depending on:
     * - Dim color if the activity has past
     * - Accent color if the activity is on going
     * - Primary color if the index of the activity is even
     * - Secondary color
     */
    val bgColor = when {
        activity.hasPast() && !activity.date.isBefore(activeDay)
            -> MaterialTheme.colorScheme.surface
        activity.isOngoing() && activeDay.dayOfMonth == activity.date.dayOfMonth
            -> MaterialTheme.colorScheme.surfaceContainerHighest
        isEven
            -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondary
    }

    /**
     * Sets the text color of the activity depending on:
     * - Dim color if the activity has past
     * - Regular text color
     */
    val txtColor = when {
        activity.hasPast() && !activity.date.isBefore(activeDay)
            -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onPrimary
    }


    FlowRow(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .absoluteOffset(x = xOffset, y = yOffset)
            .clickable { openEdit(activity) }
            .padding(5.dp)
            .background(
                bgColor,
                RoundedCornerShape(8.dp))
            .height(height)
            .width(cardWidth-cardPadding)
            .padding(10.dp)
            .clipToBounds()
    ) {
        Text(
            activity.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = txtColor
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                activity.getTimeString(),
                fontSize = 12.sp,
                color = txtColor
            )
            Icon(
                tint = txtColor,
                imageVector = Icons.Default.DateRange,
                contentDescription = "Activity duration",
                modifier = Modifier
                    .size(16.dp)
            )
        }

    }
}