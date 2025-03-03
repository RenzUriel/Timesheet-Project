package com.example.timesheet.ui.screen

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.timesheet.data.LocalAttendanceDataProvider.getAttendanceData
import com.example.timesheet.features.ClockInOutButton
import com.example.timesheet.features.DrawerMenu
import com.example.timesheet.ui.components.AttendanceItem
import com.example.timesheet.ui.components.AttendanceTableHeader
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController, isClockedIn: Boolean, onToggleClockIn: (Boolean) -> Unit) {
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
            var text by remember { mutableStateOf("") }
            var fromInput by remember { mutableStateOf("") }
            var toInput by remember { mutableStateOf("") }
            val attendanceData = getAttendanceData()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Generate Reports", modifier = Modifier.padding(bottom = 25.dp))
                }

                item {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Search") },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFDADCEC)),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                        }
                    )
                }

                item {
                    Card(modifier = Modifier.padding(top = 16.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                com.example.timesheet.ui.components.DateInputTextField(
                                    value = fromInput,
                                    onValueChange = { fromInput = it },
                                    label = "From",
                                    modifier = Modifier.weight(1f)
                                )
                                com.example.timesheet.ui.components.DateInputTextField(
                                    value = toInput,
                                    onValueChange = { toInput = it },
                                    label = "To",
                                    modifier = Modifier.weight(1f)
                                )

                                Button(
                                    onClick = { },
                                    modifier = Modifier
                                        .sizeIn(minWidth = 80.dp, minHeight = 28.dp)
                                        .padding(2.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C50A9))
                                ) {
                                    Text("Download Report", color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    AttendanceTableHeader()
                }

                itemsIndexed(attendanceData) { index, attendance ->
                    AttendanceItem(attendance, index)
                }
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


@Preview(showBackground = true)
@Composable
fun PreviewAttendanceScreen() {
    val navController = rememberNavController()
    var isClockedIn by remember { mutableStateOf(false) }

    AttendanceScreen(navController, isClockedIn) { isClockedIn = !isClockedIn }
}