package com.example.timesheet.data.others

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

data class AttendanceSheetData(
    val date: String,
    val timeIn: String,
    val timeOut: String,
    val location: String
)


@Composable
fun AttendanceItem(attendance: AttendanceSheetData, index: Int) {
    val backgroundColor = if (index % 2 == 0) Color(0xFF87CEEB) else Color.Transparent

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