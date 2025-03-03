package com.example.timesheet.features

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timesheet.R
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClockInOutButton(
    isClockedIn: Boolean,
    onClockToggle: () -> Unit,
    elapsedTime: Long,
    updateElapsedTime: (Long) -> Unit
) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var showDialog by remember { mutableStateOf(false) }
    var clockTime by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(isClockedIn) {
        if (isClockedIn) {
            startTime = System.currentTimeMillis()
            while (isClockedIn) {
                updateElapsedTime((System.currentTimeMillis() - (startTime ?: 0)) / 1000)
                delay(1000)
            }
        } else {
            startTime = null
            updateElapsedTime(0)
        }
    }

    FloatingActionButton(
        onClick = {
            clockTime = timeFormat.format(Date())
            if (isClockedIn) {
                showDialog = true
            } else {
                onClockToggle()
                Toast.makeText(context, "Clocked-In at $clockTime", Toast.LENGTH_SHORT).show()
            }
        },
        containerColor = if (isClockedIn) Color(0xFF9A3636) else Color(0xFF499A36)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(if (isClockedIn) R.drawable.stop_circle else R.drawable.play_circle),
                contentDescription = if (isClockedIn) "Clock Out" else "Clock In",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isClockedIn) "Clock Out" else "Clock In",
                color = Color.White
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Clock Out") },
            text = { Text("Confirm clock-out at $clockTime?", modifier = Modifier.fillMaxWidth()) },
            confirmButton = {
                TextButton(onClick = {
                    onClockToggle()
                    Toast.makeText(context, "Clocked-Out at $clockTime", Toast.LENGTH_SHORT).show()
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClockInOutButton() {
    var isClockedIn by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    ClockInOutButton(
        isClockedIn = isClockedIn,
        onClockToggle = { isClockedIn = !isClockedIn },
        elapsedTime = elapsedTime,
        updateElapsedTime = { elapsedTime = it }
    )
}
