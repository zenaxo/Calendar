package com.hannessjolander.calendar.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Header(
    navController: NavController,
    numActivities: Int,
    date: LocalDate = LocalDate.now(),
    setToday: (date: LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale("sv"))
    val dateStr = date
        .format(dateFormatter)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    val route = navController.currentBackStackEntryAsState().value?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            dateStr,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            "$numActivities aktiviteter",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary)
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if(route != "home") {
                        navController.navigate("home")
                    }
                    setToday(today)
                },
                colors = if (route != "home" || date != today)
                    ButtonDefaults.outlinedButtonColors()
                    else ButtonDefaults.buttonColors(),
                border = if(route != "home" || date != today)
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else null
            ) {
                Text("Idag", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Button(
                onClick = { navController.navigate("calender") },
                colors = if (route != "calender")
                    ButtonDefaults.outlinedButtonColors()
                    else ButtonDefaults.buttonColors(),
                border = if(route != "calender")
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else null
            ) {
                Text("Kalender", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
