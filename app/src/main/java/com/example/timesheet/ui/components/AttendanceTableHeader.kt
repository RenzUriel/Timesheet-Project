package com.example.timesheet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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