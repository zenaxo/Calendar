package com.hannessjolander.calendar

import EditActivitySheet
import NewActivitySheet
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hannessjolander.calendar.ui.composable.Header
import com.hannessjolander.calendar.ui.theme.CalendarTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class) // Usage of experimental sheets
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel by viewModels()
            val navController = rememberNavController()
            val sheetState = rememberModalBottomSheetState()
            val coroutineScope = rememberCoroutineScope()

            val activeActivities = viewModel.activeActivities.collectAsState()
            val activeDate = viewModel.activeDate.collectAsState()
            val showNewSheet = viewModel.showNewSheet.collectAsState()
            val showEditSheet = viewModel.showEditSheet.collectAsState()

            CalendarTheme {
                Scaffold(
                    topBar = {
                        Header(
                            navController,
                            numActivities = activeActivities.value.size,
                            date = activeDate.value,
                            setToday = { today: LocalDate ->
                                viewModel.setActiveDate(today)
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { viewModel.toggleNewSheet(true) },
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = Color.White,
                            shape = CircleShape,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Skapa aktivitet")
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        AppNavigation(navController, viewModel)

                        if (showNewSheet.value) {
                            NewActivitySheet(
                                activeDate = activeDate.value,
                                onDismissRequest = {
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        viewModel.toggleNewSheet(false)
                                    }
                                },
                                sheetState = sheetState,
                                onSubmit = {
                                    viewModel.addActivity(it)
                                }
                            )
                        }

                        if (showEditSheet.value && viewModel.activeEdit != null) {
                            EditActivitySheet(
                                initialActivity = viewModel.activeEdit!!,
                                sheetState = sheetState,
                                onDismissRequest = {
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        viewModel.setActiveEdit(null)
                                    }
                                },
                                onSubmit = {
                                    viewModel.updateActivity(it)
                                    viewModel.setActiveEdit(null)
                                },
                                onDelete = {
                                    viewModel.deleteActivity(it)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Set up the navigation routes of the application
 */
@Composable
fun AppNavigation(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { TimelineActivity(viewModel) }
        composable("calender") { CalenderActivity(viewModel) }
    }
}
