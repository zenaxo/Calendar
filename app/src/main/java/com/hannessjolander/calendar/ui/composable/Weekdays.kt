package com.hannessjolander.calendar.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

val DAYS = arrayOf("Mån", "Tis", "Ons", "Tor", "Fre", "Lör", "Sön")
@Composable
fun Weekdays(activeDay: String = "Mån", setDay: (String) -> Unit) {
    fun rotateDays(activeDay: String, days: Array<String>): List<String> {
        val index = days.indexOf(activeDay)
        if(index == -1) return days.toList()

        val midPoint = days.size / 2
        val shiftAmount = midPoint - index

        return days.toList().let { list ->
            if(shiftAmount > 0) {
                list.drop(days.size - shiftAmount) + list.take(days.size - shiftAmount)
            } else {
                list.drop(-shiftAmount) + list.take(-shiftAmount)
            }
        }
    }
    val days = rotateDays(activeDay, DAYS)

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
    ) {
        for (day in days) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(if(day == activeDay)
                        MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary)
                    .weight(1f)
                    .clickable { setDay(day) }
                    .fillMaxWidth()
                    .padding(0.dp, 4.dp)
            ) {
                Text(
                    text = day,
                    color = MaterialTheme.colorScheme.onPrimary,
                    // To make sure all of them take up the same space
                    fontFamily = FontFamily.Monospace
                )
            }

        }
    }
}
