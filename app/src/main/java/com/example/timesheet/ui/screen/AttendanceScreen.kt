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
import com.example.timesheet.features.ClockInOutButton
import com.example.timesheet.features.DrawerMenu
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController, isClockedIn: Boolean, onToggleClockIn: () -> Unit) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var isDrawerOpen by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    Scaffold(
        floatingActionButton = {
            ClockInOutButton(
                isClockedIn = isClockedIn,
                onClockToggle = onToggleClockIn,
                elapsedTime = elapsedTime,
                updateElapsedTime = { elapsedTime = it }
            )
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