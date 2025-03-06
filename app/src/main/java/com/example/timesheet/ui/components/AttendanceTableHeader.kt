package com.example.timesheet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timesheet.data.AttendanceSheetData

@Composable
fun AttendanceTableHeader(textAlign: TextAlign = TextAlign.Center) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = "Date",
            modifier = Modifier.weight(1f),
            textAlign = textAlign
        )
        Text(
            text = "Time In",
            modifier = Modifier.weight(1f),
            textAlign = textAlign
        )
        Text(
            text = "Time Out",
            modifier = Modifier.weight(1f),
            textAlign = textAlign
        )
        Text(
            text = "Location",
            modifier = Modifier.weight(1f),
            textAlign = textAlign
        )


    }
}
@Composable
fun AttendanceItem(attendance: AttendanceSheetData, index: Int) {
    val backgroundColor = if (index % 2 == 0) Color(0xFF6A6ECF) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = attendance.date,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
        )
        Text(
            text = attendance.timeIn,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
        )
        Text(
            text = attendance.timeOut,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
        )
        Text(
            text = attendance.location,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
        )
    }
}