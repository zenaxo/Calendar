package com.hannessjolander.calendar.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate

/**
 * Displays an infinite carousel of dates
 *
 * @param selectedDay the active day which will be highlighted in a different color
 * @param setDay callback to update the selected day
 */
@Composable
fun DayCarousel(selectedDay: LocalDate = LocalDate.now(), setDay: (day: LocalDate) -> Unit) {
    val daysShown = 7
    val listState = rememberLazyListState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // Calculate the width of a day button depending on how many is configured to be shown at a time
    val itemWidth: Dp = screenWidth / daysShown
    // Retrieve the middle index from the "infinite" amount of days
    val middleIndex = Int.MAX_VALUE / 2

    // Center the selected day when it changes
    LaunchedEffect(selectedDay) {
        val index = middleIndex + selectedDay.toEpochDay().toInt() - LocalDate.now().toEpochDay().toInt()
        listState.animateScrollToItem(index - daysShown / 2)
    }

    // Lazy row to only render the visible days
    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(Int.MAX_VALUE) { index ->
            val dayOffset = index - middleIndex
            val day = LocalDate.now().plusDays(dayOffset.toLong())
            val isSelected = day == selectedDay

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(itemWidth)
                    .padding(4.dp)
                    .aspectRatio(1f)
                    .shadow(if (isSelected) 4.dp else 0.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.primary
                    )
                    .clickable {
                        setDay(day)
                    }
            ) {
                Text(
                    day.dayOfMonth.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

