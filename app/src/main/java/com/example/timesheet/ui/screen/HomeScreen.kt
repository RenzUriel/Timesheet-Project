package com.example.timesheet.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.timesheet.data.LocalAttendanceDataProvider.getAttendanceData
import com.example.timesheet.data.TrackedHoursGraph
import com.example.timesheet.features.ClockInOutButton
import com.example.timesheet.features.DrawerMenu
import com.example.timesheet.ui.components.AttendanceTableHeader
import com.example.timesheet.ui.components.TopBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, isClockedIn: Boolean, onToggleClockIn: (Boolean) -> Unit) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val currentDate =
        remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(Date()) }
    val attendanceData = getAttendanceData()

    var isDrawerOpen by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.fillMaxHeight().width(300.dp).background(Color.LightGray)) {
                DrawerMenu(navController) { scope.launch { drawerState.close() } }
            }
        }
    ) {
        Scaffold(
            floatingActionButton = {
                ClockInOutButton(
                    isClockedIn = isClockedIn,
                    onClockToggle = onToggleClockIn,
                    elapsedTime = elapsedTime,
                    updateElapsedTime = { elapsedTime = it }
                )
            },

                topBar = {TopBar(navController) { scope.launch { drawerState.open() } }},

            bottomBar = {
                AnimatedVisibility(
                    visible = !isDrawerOpen,
                    enter = fadeIn() + slideInVertically { it },
                    exit = fadeOut() + slideOutVertically { it }
                ) {
                    BottomAppBar(
                        modifier = Modifier.height(65.dp),
                        containerColor = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            NavigationItem(
                                "Home",
                                navController,
                                R.drawable.home,
                                "home",
                                Color(0xFF4C60A9)
                            )
                            NavigationItem(
                                "Attendance",
                                navController,
                                R.drawable.clock,
                                "attendance_screen"
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            "Dashboard",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4C60A9)
                        )
                    }
                    item {
                        InfoCard(R.drawable.calendar, currentDate) {
                            Text("Have a Nice Day!", modifier = Modifier.fillMaxWidth())
                        }
                    }
                    item {
                        InfoCard(R.drawable.clock, "Time Clock") {
                            Text(formatElapsedTime(elapsedTime), modifier = Modifier.fillMaxWidth())
                        }
                    }
                    item {
                        InfoCard(R.drawable.people, "Attendance") {
                            AttendanceTableHeader()

                        }
                    }
                    item {
                        InfoCard(null, "Tracked Hours") {
                            TrackedHoursGraph()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    iconRes: Int?,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // Align icon & title
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.padding(start = if (iconRes != null) 28.dp else 0.dp) // Indent content if icon exists
            ) {
                content()
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
