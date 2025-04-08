package com.example.timesheet.features

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timesheet.R
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClockInOutButton(
    isClockedIn: Boolean,
    onClockToggle: (Boolean) -> Unit,
    elapsedTime: Long,
    updateElapsedTime: (Long) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("TimeSheetPrefs", Context.MODE_PRIVATE) }
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    var showDialog by remember { mutableStateOf(false) }
    var clockTime by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf(getStoredStartTime(sharedPreferences)) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isClockedIn) 360f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "Rotate Animation"
    )

    LaunchedEffect(isClockedIn) {
        if (isClockedIn) {
            if (startTime == 0L) {
                startTime = System.currentTimeMillis()
                saveStartTime(sharedPreferences, startTime)
            }
            while (isClockedIn) {
                updateElapsedTime((System.currentTimeMillis() - startTime) / 1000)
                delay(1000)
            }
        } else {
            saveStartTime(sharedPreferences, 0L)
            updateElapsedTime(0)
        }
    }

    FloatingActionButton(
        onClick = {
            clockTime = timeFormat.format(Date())
            if (isClockedIn) {
                showDialog = true
            } else {
                startTime = System.currentTimeMillis()
                saveStartTime(sharedPreferences, startTime)
                onClockToggle(true)
                Toast.makeText(context, "Clocked-In at $clockTime", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.rotate(rotationAngle),
        shape = RoundedCornerShape(15.dp),
        containerColor = if (isClockedIn) Color(0xFF9A3636) else Color(0xFF499A36)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Icon(
                painter = painterResource(if (isClockedIn) R.drawable.stop_circle else R.drawable.play_circle),
                contentDescription = if (isClockedIn) "Clock-out" else "Clock-in",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isClockedIn) "Clock-out" else "Clock-in",
                color = Color.White
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Clock Out") },
            text = { Text("Confirm clock-out at $clockTime?") },
            confirmButton = {
                TextButton(onClick = {
                    saveStartTime(sharedPreferences, 0L)
                    onClockToggle(false)
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

@Composable
fun TimerProgressBar(elapsedTime: Long, totalTime: Long = 600L, gradientSunset: Brush) {
    val progress = remember(elapsedTime) { (elapsedTime.toFloat() / totalTime.toFloat()).coerceIn(0f, 1f) }

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
            .height(10.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFB0BEC5))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(brush = gradientSunset)
        )
    }
}


private fun saveStartTime(sharedPreferences: SharedPreferences, time: Long) {
    sharedPreferences.edit().putLong("start_time", time).apply()
}

private fun getStoredStartTime(sharedPreferences: SharedPreferences): Long {
    return sharedPreferences.getLong("start_time", 0L)
}

@Preview(showBackground = true)
@Composable
fun PreviewClockInOutButton() {
    var isClockedIn by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    ClockInOutButton(
        isClockedIn = isClockedIn,
        onClockToggle = { isClockedIn = it },
        elapsedTime = elapsedTime,
        updateElapsedTime = { elapsedTime = it }
    )
}
