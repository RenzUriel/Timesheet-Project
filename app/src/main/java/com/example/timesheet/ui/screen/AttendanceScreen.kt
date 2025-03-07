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
import com.example.timesheet.data.AttendanceItem
import com.example.timesheet.data.LocalAttendanceDataProvider.getAttendanceData
import com.example.timesheet.data.MonthlyTimesheetScreen
import com.example.timesheet.features.ClockInOutButton
import com.example.timesheet.features.DrawerMenu
import com.example.timesheet.ui.components.AttendanceTableHeader
import com.example.timesheet.ui.components.TopBar
import com.example.timesheet.ui.theme.gradientPurplePink
import com.example.timesheet.ui.theme.gradientSky
import com.example.timesheet.ui.theme.gradientSoftCyan
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(navController: NavController, isClockedIn: Boolean, onToggleClockIn: (Boolean) -> Unit) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var elapsedTime by remember { mutableStateOf(0L) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                    visible = drawerState.isClosed,
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
                            NavigationItem(
                                "Attendance",
                                navController,
                                R.drawable.clock,
                                "attendance_screen",
                                Color(0xFF4C60A9)
                            )
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(15.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    item {
                        Text(
                            "Generate Reports",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4C60A9)
                        )
                    }

                    item {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            label = { Text("Search") },
                            colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFDADCEC)),
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            }
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
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
                                        onClick = { /* Implement Download Report Function */ },
                                        modifier = Modifier
                                            .sizeIn(minWidth = 80.dp, minHeight = 28.dp)
                                            .padding(2.dp),
                                        shape = RoundedCornerShape(60.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4C60A9))
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

                    item {
                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(gradientSoftCyan)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MonthlyTimesheetScreen()
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
