package com.example.timesheet.ui.screen

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.ui.screen.DrawerMenu
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController, isClockedIn: Boolean, onClockToggle: () -> Unit) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var isDrawerOpen by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onClockToggle()
                    val clockTime = timeFormat.format(Date())
                    val message = if (isClockedIn) "Clocked-Out at $clockTime" else "Clocked-In at $clockTime"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                },
                containerColor = if (isClockedIn) Color(0xFF9A3636) else Color(0xFF499A36)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                        NavigationItem("Home", navController, R.drawable.home, "home")
                        NavigationItem("Attendance", navController, R.drawable.clock, "attendance_screen", Color(0xFF4C60A9))
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
            ) {}

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

@Preview(showBackground = true)
@Composable
fun PreviewAttendanceScreen() {
    val navController = rememberNavController()
    var isClockedIn by remember { mutableStateOf(false) }

    AttendanceScreen(navController, isClockedIn) { isClockedIn = !isClockedIn }
}



