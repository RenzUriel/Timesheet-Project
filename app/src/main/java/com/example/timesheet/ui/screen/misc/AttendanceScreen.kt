package com.example.timesheet.ui.screen.misc

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.data.Temp.LocalAttendanceDataProvider.getAttendanceData
import com.example.timesheet.data.TimesheetData
import com.example.timesheet.data.Temp.TimesheetTable
import com.example.timesheet.features.AttendanceAnalytics
import com.example.timesheet.features.ClockInOutButton
import com.example.timesheet.features.DrawerMenu
import com.example.timesheet.features.NavigationItem
import com.example.timesheet.ui.components.TopBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController, isClockedIn: Boolean, onToggleClockIn: (Boolean) -> Unit) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var elapsedTime by remember { mutableStateOf(0L) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isDrawerOpen by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(Color.LightGray)
            ) {
                DrawerMenu(navController) { scope.launch { drawerState.close() } }
            }
        }
    ) {
        Scaffold(
            containerColor = Color(0xFFF6F6F6),
            floatingActionButton = {
                ClockInOutButton(
                    isClockedIn = isClockedIn,
                    onClockToggle = onToggleClockIn,
                    elapsedTime = elapsedTime,
                    updateElapsedTime = { elapsedTime = it }
                )
            },
            topBar = { TopBar(navController) { scope.launch { drawerState.open() } } },
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
                            NavigationItem("Home", navController, R.drawable.home, "home")
                            NavigationItem("Attendance", navController, R.drawable.clock, "attendance_screen", Color(0xFF4C60A9))
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                var searchText by remember { mutableStateOf("") }
                var fromInput by remember { mutableStateOf("") }
                var toInput by remember { mutableStateOf("") }
                val attendanceData = getAttendanceData()

                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(5.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                    ) {
                        TimesheetTable(data = TimesheetData.sampleData, navController = navController)
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp).clip(RoundedCornerShape(16.dp)).background(Color.White),
                    ){
                        AttendanceAnalytics()
                    }

                    Box() {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                }
                            }
                        }

                    }
                }
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