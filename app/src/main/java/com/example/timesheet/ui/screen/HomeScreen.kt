package com.example.timesheet.ui.screen

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.ui.screen.DrawerMenu
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, isClockedIn: Boolean, onToggleClockIn: () -> Unit) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val currentDate = remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date()) }

    var isDrawerOpen by remember { mutableStateOf(false) }
    var isClockedIn by remember { mutableStateOf(false) }
    var clockTime by remember { mutableStateOf("") }
    var elapsedTime by remember { mutableStateOf(0L) }
    var startTime by remember { mutableStateOf<Long?>(null) }

    val currentClockedInState by rememberUpdatedState(isClockedIn)

    LaunchedEffect(currentClockedInState) {
        if (currentClockedInState) {
            startTime = System.currentTimeMillis()
            while (currentClockedInState) {
                elapsedTime = (System.currentTimeMillis() - (startTime ?: 0)) / 1000
                delay(1000)
            }
        } else {
            startTime = null
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isClockedIn = !isClockedIn
                    clockTime = timeFormat.format(Date())

                    val message = if (isClockedIn) "Clocked-In at $clockTime" else "Clocked-Out at $clockTime"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                },
                containerColor = if (isClockedIn) Color(0xFF9A3636) else Color(0xFF499A36)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(if (isClockedIn) R.drawable.stop_circle else R.drawable.play_circle),
                        contentDescription = if (isClockedIn) "Clock Out" else "Clock In",
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isClockedIn) "Clock Out" else "Clock In",
                        color = Color.White
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Jairosoft", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { isDrawerOpen = !isDrawerOpen }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = !isDrawerOpen,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                BottomAppBar(
                    modifier = Modifier
                        .height(65.dp)
                        .background(Color.Transparent),
                    containerColor = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        NavigationItem("Home", navController, R.drawable.home, "home", Color(0xFF4C60A9))
                        NavigationItem("Attendance", navController, R.drawable.clock, "attendance_screen")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfoCard(R.drawable.calendar, "Today is", currentDate)
                InfoCard(R.drawable.clock, "Time Clock", formatElapsedTime(elapsedTime))
                InfoCard(R.drawable.people, "Attendance", "")
                InfoCard(null, "Tracked Hours", "")
            }

            AnimatedVisibility(
                visible = isDrawerOpen,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ) {
                DrawerMenu(navController) { isDrawerOpen = false }
            }
        }
    }
}

@Composable
fun InfoCard(iconRes: Int?, title: String, content: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                iconRes?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = "$title Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF4C60A9)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            if (content.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun NavigationItem(label: String, navController: NavController, iconRes: Int, route: String, color: Color = Color.Gray, onClick: (() -> Unit)? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = {
            navController.navigate(route)
            onClick?.invoke()
        }) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(25.dp),
                tint = color
            )
        }
        Text(label, fontSize = 12.sp, maxLines = 1, color = color)
    }
}

fun formatElapsedTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val sec = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, sec)
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    var isClockedIn by remember { mutableStateOf(false) }

    HomeScreen(navController, isClockedIn) { isClockedIn = !isClockedIn }
}



