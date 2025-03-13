package com.example.timesheet.features

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AttendanceAnalytics() {
    var selectedMonth by remember { mutableStateOf("February") }
    var selectedWeek by remember { mutableStateOf("Week 1") }
    val months = listOf("February", "March")
    val weeks = listOf("Week 1", "Week 2", "Week 3", "Week 4")

    val lastWeekHours = WorkSchedule.calculateHours(selectedMonth, selectedWeek, previous = true)
    val thisWeekHours = WorkSchedule.calculateHours(selectedMonth, selectedWeek, previous = false)
    val totalMonthHours = WorkSchedule.calculateTotalHours(selectedMonth)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DropdownSelector(
                label = "Month",
                options = months,
                selectedOption = selectedMonth,
                onOptionSelected = { selectedMonth = it },
                modifier = Modifier.weight(1f)
            )

            DropdownSelector(
                label = "Week",
                options = weeks,
                selectedOption = selectedWeek,
                onOptionSelected = { selectedWeek = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnalyticsColumn(title = "Last Week", hours = formatHoursToHM(lastWeekHours))
                Divider(
                    modifier = Modifier
                        .height(50.dp)
                        .width(1.dp)
                )
                AnalyticsColumn(title = "This Week", hours = formatHoursToHM(thisWeekHours))
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            AnalyticsColumn(title = "This Month", hours = formatHoursToHM(totalMonthHours))
        }
    }
}

object WorkSchedule {
    private val schedule = mapOf(
        "2025-02-03" to 8.5f, "2025-02-04" to 9.0f, "2025-02-05" to 8.25f, "2025-02-06" to 9.5f, "2025-02-07" to 8.75f,
        "2025-02-10" to 9.0f, "2025-02-11" to 8.5f, "2025-02-12" to 9.25f, "2025-02-13" to 8.0f, "2025-02-14" to 9.5f,
        "2025-02-17" to 8.75f, "2025-02-18" to 9.0f, "2025-02-19" to 8.5f, "2025-02-20" to 9.25f, "2025-02-21" to 8.0f,
        "2025-02-24" to 9.5f, "2025-02-25" to 8.25f, "2025-02-26" to 9.0f, "2025-02-27" to 8.75f, "2025-02-28" to 9.0f,
        "2025-03-03" to 8.25f, "2025-03-04" to 9.5f, "2025-03-05" to 8.0f, "2025-03-06" to 9.25f, "2025-03-07" to 8.75f
    )

    fun getWorkHours(date: String): Float? = schedule[date]

    fun getDatesForMonth(month: String): List<String> =
        schedule.keys.filter { it.startsWith(getMonthCode(month)) }

    fun getDatesForWeek(month: String, weekNumber: Int): List<String> {
        val dates = getDatesForMonth(month)
        val startIndex = (weekNumber - 1) * 5
        return dates.drop(startIndex).take(5)
    }

    fun calculateHours(month: String, week: String, previous: Boolean): Float {
        val weekNumber = week.replace("Week ", "").toInt()
        val targetWeek = if (previous) maxOf(weekNumber - 1, 1) else weekNumber
        val weekDates = getDatesForWeek(month, targetWeek)
        return weekDates.mapNotNull { getWorkHours(it) }.sum()
    }

    fun calculateTotalHours(month: String): Float =
        getDatesForMonth(month).mapNotNull { getWorkHours(it) }.sum()

    private fun getMonthCode(month: String): String =
        when (month) {
            "February" -> "2025-02"
            "March" -> "2025-03"
            else -> ""
        }
}

// Helper function to format decimal hours to 'Xh Ym'
fun formatHoursToHM(hours: Float): String {
    val totalMinutes = (hours * 60).toInt()
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return "${h}h ${m}m"
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown Icon"
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(180.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AnalyticsColumn(title: String, hours: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = hours, fontSize = 26.sp, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, fontSize = 18.sp, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun AttendanceAnalyticsPreview() {
    AttendanceAnalytics()
}
