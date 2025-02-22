import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hannessjolander.calendar.model.CalendarActivity
import com.hannessjolander.calendar.model.RepeatAt
import com.hannessjolander.calendar.model.displayName
import java.time.LocalDate
import java.util.Locale
import java.util.UUID

@SuppressLint("DefaultLocale")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditActivitySheet(
    initialActivity: CalendarActivity,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onSubmit: (CalendarActivity) -> Unit,
    onDelete: (UUID) -> Unit
) {

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(initialActivity.name))
    }
    var selectedRepeat by remember { mutableStateOf(initialActivity.repeatAt) }
    val focusRequester = remember { FocusRequester() }

    var startDate by remember { mutableStateOf(initialActivity.date) }
    var startTime by remember {
        mutableStateOf(Pair(initialActivity.startTime.hour, initialActivity.startTime.minute))
    }
    var endDate by remember { mutableStateOf(initialActivity.date) }
    var endTime by remember {
        mutableStateOf(Pair(initialActivity.endTime.hour, initialActivity.endTime.minute))
    }

    fun dateToString(date: LocalDate):String {
        return "${date.dayOfMonth} ${date.month.toString().substring(0,3).lowercase(Locale.ROOT)}"
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 16.dp,
        modifier = Modifier.fillMaxHeight(),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .width(50.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp)
        ) {
            Text(
                "Avbryt",
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { onDismissRequest() }
            )
            Text(
                "Redigera aktivitet",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Uppdatera",
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable {
                        val activity = CalendarActivity(
                            textFieldValue.text,
                            startDate,
                            startTime.first,
                            startTime.second,
                            endTime.first,
                            endTime.second,
                            selectedRepeat
                        )
                        onSubmit(activity)
                    }
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { newValue -> textFieldValue = newValue },
                    label = { Text("Aktivitet") },
                    placeholder = { Text("Gå till gymmet...") },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
                }
            }
            Spacer(Modifier.height(10.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Startar")
                    Row {
                        Button(
                            onClick = { showStartDatePicker = !showStartDatePicker },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(dateToString(startDate), fontSize = 14.sp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Button(
                            onClick = { showStartTimePicker = !showStartTimePicker },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(String.format("%02d:%02d", startTime.first, startTime.second), fontSize = 14.sp)
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Slutar")
                    Row {
                        Button(
                            onClick = { showEndDatePicker = !showEndDatePicker },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(dateToString(endDate), fontSize = 14.sp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Button(
                            onClick = { showEndTimePicker = !showEndTimePicker },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(String.format("%02d:%02d", endTime.first, endTime.second), fontSize = 14.sp)
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Upprepa")
                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        TextButton(
                            onClick = { expanded = true },
                        ) {
                            Text(selectedRepeat.displayName(), fontSize = 14.sp)
                            Spacer(Modifier.width(4.dp))
                            Column {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Välj upprepa",
                                    modifier = Modifier
                                        .size(12.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Välj upprepa",
                                    modifier = Modifier
                                        .size(12.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            RepeatAt.entries.forEach { repeat ->
                                DropdownMenuItem(
                                    text = { Text(repeat.displayName()) },
                                    onClick = {
                                        selectedRepeat = repeat
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { onDelete(initialActivity.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Ta bort")
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Ta bort aktivitet")
                }
            }
        }
        if(showStartDatePicker) {
            DatePickerModal(
                title = "Välj startdatum",
                initialDate = startDate,
                onDateSelected = { date ->
                    if (date != null) {
                        startDate = date
                    }
                    showStartDatePicker = false
                    if(startDate.isAfter(endDate)) {
                        endDate = startDate
                    }
                },
                onDismiss = { showStartDatePicker = false}
            )
        }
        if(showEndDatePicker) {
            DatePickerModal(
                title = "Välj slutdatum",
                initialDate = endDate,
                onDateSelected = { date ->
                    if (date != null) {
                        endDate = date
                    }
                    showEndDatePicker = false
                    if(endDate.isBefore(startDate)) {
                        startDate = endDate
                    }
                },
                onDismiss = { showEndDatePicker = false}
            )
        }
        if(showStartTimePicker) {
            TimePickerModal(
                title = "Välj starttid",
                onConfirm = { newTime ->
                    startTime = newTime
                    showStartTimePicker = false

                    if(startTime.first > endTime.first) {
                        endTime = Pair(startTime.first+1, endTime.second)
                    }
                },
                onDismiss = {
                    showStartTimePicker = false
                }
            )
        }
        if(showEndTimePicker) {
            TimePickerModal(
                title = "Välj sluttid",
                onConfirm = { newTime ->
                    endTime = newTime
                    showEndTimePicker = false

                    if(endTime.first < startTime.first) {
                        startTime = Pair(endTime.first-1, startTime.second)
                    }
                },
                onDismiss = { showEndTimePicker = false }
            )
        }
    }
}





