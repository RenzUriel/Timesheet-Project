package com.example.timesheet.data.others

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun TimesheetTable(data: List<TimesheetData.Entry>, navController: NavController) {
    val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")
    val horizontalScrollState = rememberScrollState()
    var weekOffset by remember { mutableStateOf(0) }
    val currentDate = Calendar.getInstance()
    currentDate.add(Calendar.WEEK_OF_YEAR, weekOffset)
    currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startOfWeek = currentDate.time
    currentDate.add(Calendar.DAY_OF_WEEK, 6)
    val endOfWeek = currentDate.time
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val dateFormat = SimpleDateFormat("d", Locale.getDefault())
    val month = monthFormat.format(startOfWeek)
    val startDate = dateFormat.format(startOfWeek)
    val endDate = dateFormat.format(endOfWeek)
    val dateRange = "$month $startDate - $endDate"
    var expanded by remember { mutableStateOf(false) }
    val months = listOf("January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December")



    Column(modifier = Modifier.padding(10.dp).height(300.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                "Generate Report",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4C60A9),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { navController.navigate("postlogscreen") },
                modifier = Modifier
                    .sizeIn(minWidth = 100.dp, minHeight = 35.dp) // Adjust width and height
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C60A9))
            ) {
                Text("Log Report", color = Color.White, fontSize = 15.sp) // Adjust font size if needed
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { weekOffset-- }) {
                Text("<")
            }

            Box {
                Button(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(dateRange,fontWeight = FontWeight.Bold)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    months.forEach { month ->
                        DropdownMenuItem(
                            text = { Text(month) },
                            onClick = { expanded = false
                            }
                        )
                    }

                }
            }
            IconButton(onClick = { weekOffset++ }) { Text(">") }
        }

        Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
                Column (modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    Row { TableCell(" ", true, Modifier.width(100.dp).padding(2.dp)) }
                    Row { TableCell("Date", true, Modifier.width(100.dp).padding(2.dp)) }
                    Row { TableCell("Time-in", true, Modifier.width(100.dp).padding(2.dp)) }
                    Row { TableCell("Time-out", true, Modifier.width(100.dp).padding(2.dp)) }
                    Row { TableCell("Tracked Hrs", true, Modifier.width(100.dp).padding(2.dp)) }
                    Row { TableCell("Location", true, Modifier.width(100.dp).padding(2.dp)) }
                }

                Column (modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    Row { weekDays.forEachIndexed { index, day ->
                        TableCell(day, true, Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White).padding(2.dp))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.date, true, Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White).padding(2.dp))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.timeIn, modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White).padding(2.dp))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.timeOut, modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White).padding(2.dp))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(TimesheetData.calculateHours(entry.timeIn, entry.timeOut), modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White).padding(2.dp))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.location, modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White).padding(2.dp))
                    }}
                }
            }

            ScrollIndicator(scrollState = horizontalScrollState, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}



@Composable
fun TableCell(text: String, isHeader: Boolean = false, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(4.dp),
        textAlign = TextAlign.Center,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal
    )
}

@Composable
fun ScrollIndicator(scrollState: ScrollState, modifier: Modifier = Modifier) {
    val progress = scrollState.value.toFloat() / scrollState.maxValue.toFloat()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(4.dp)
                .background(Color.DarkGray)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimesheetTable() {
    val fakeNavController = rememberNavController()
    TimesheetTable(data = TimesheetData.sampleData, navController = fakeNavController)
}