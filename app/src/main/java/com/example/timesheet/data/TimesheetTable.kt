package com.example.timesheet.data

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timesheet.data.TimesheetData

@Composable
fun TimesheetTable(data: List<TimesheetData.Entry>) {
    val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")

    val horizontalScrollState = rememberScrollState()

    Column(modifier = Modifier.padding(4.dp)) {
        // Week Range Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* Previous Week Logic */ }) {
                Text("<")
            }
            Text("Mar 3 - 9", fontWeight = FontWeight.Bold)
            IconButton(onClick = { /* Next Week Logic */ }) {
                Text(">")
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
                Column {
                    Row { TableCell(" ", true, Modifier.width(80.dp)) }
                    Row { TableCell("Date", true, Modifier.width(80.dp)) }
                    Row { TableCell("Time-in", true, Modifier.width(80.dp)) }
                    Row { TableCell("Time-out", true, Modifier.width(80.dp)) }
                    Row { TableCell("Trkd.Hrs", true, Modifier.width(80.dp)) }
                    Row { TableCell("Location", true, Modifier.width(80.dp)) }
                }

                Column {
                    Row { weekDays.forEachIndexed { index, day ->
                        TableCell(day, true, Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.date, true, Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.timeIn, modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.timeOut, modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(TimesheetData.calculateHours(entry.timeIn, entry.timeOut), modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White))
                    }}
                    Row { data.forEachIndexed { index, entry ->
                        TableCell(entry.location, modifier = Modifier.width(90.dp).background(if (index % 2 == 0) Color.LightGray else Color.White))
                    }}
                }
            }

            ScrollIndicator(scrollState = horizontalScrollState, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
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

@Composable
fun TableCell(text: String, isHeader: Boolean = false, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.padding(4.dp),
        textAlign = TextAlign.Center,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTimesheetTable() {
    TimesheetTable(data = TimesheetData.sampleData)
}
