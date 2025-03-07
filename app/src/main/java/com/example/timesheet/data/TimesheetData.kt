package com.example.timesheet.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MonthlyTimesheetScreen() {
    val months = listOf(
        "January" to Pair(31, 3),  // Jan 1, 2025 is Wednesday
        "February" to Pair(28, 6), // Feb 1, 2025 is Saturday
        "March" to Pair(31, 6),    // Mar 1, 2025 is Saturday
        "April" to Pair(30, 2)     // Apr 1, 2025 is Tuesday
    )
    var selectedMonth by remember { mutableStateOf(months.first().first) }
    var selectedMonthData by remember { mutableStateOf(months.first().second) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        DropdownMenuMonthSelector(
            months = months.map { it.first },
            selectedMonth = selectedMonth,
            onMonthSelected = { month ->
                months.find { it.first == month }?.let {
                    selectedMonth = it.first
                    selectedMonthData = it.second
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        MonthlyTimesheetCalendar(selectedMonth, selectedMonthData.first, selectedMonthData.second)
    }
}

@Composable
fun DropdownMenuMonthSelector(months: List<String>, selectedMonth: String, onMonthSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }) {
            Text(text = selectedMonth)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            months.forEach { month ->
                DropdownMenuItem(text = { Text(text = month) }, onClick = {
                    onMonthSelected(month)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun WeekdayHeader() {
    val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(modifier = Modifier.fillMaxWidth()) {
        weekdays.forEach { day ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun CalendarGrid(days: List<Int>, firstDayOffset: Int) {
    var currentIndex = 0
    Column {
        WeekdayHeader()
        for (week in 0 until 6) { // Max 6 rows
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayOfWeek in 0 until 7) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!(week == 0 && dayOfWeek < firstDayOffset) && currentIndex < days.size) {
                            DayCell(days[currentIndex])
                            currentIndex++
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyTimesheetCalendar(month: String, daysInMonth: Int, firstDayOfMonth: Int) {
    val days = (1..daysInMonth).toList()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "$month 2025",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CalendarGrid(days, firstDayOfMonth)
    }
}

@Composable
fun DayCell(day: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$day", fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMonthlyTimesheetScreen() {
    MonthlyTimesheetScreen()
}