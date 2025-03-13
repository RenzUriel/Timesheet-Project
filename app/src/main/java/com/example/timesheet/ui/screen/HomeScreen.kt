package com.example.timesheet.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
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
import com.example.timesheet.features.TimerProgressBar
import com.example.timesheet.ui.components.TopBar
import com.example.timesheet.ui.theme.gradientDayLight
import com.example.timesheet.ui.theme.gradientSky
import com.example.timesheet.ui.theme.gradientSunset
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, isClockedIn: Boolean, onToggleClockIn: (Boolean) -> Unit) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val currentDate = Calendar.getInstance()
    val month = remember { SimpleDateFormat("MMMM", Locale.getDefault()).format(currentDate.time) }
    val day = remember { SimpleDateFormat("d", Locale.getDefault()).format(currentDate.time) }
    val year = remember { SimpleDateFormat("yyyy", Locale.getDefault()).format(currentDate.time) }
    val attendanceData = getAttendanceData()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    var isDrawerOpen by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.fillMaxHeight().width(300.dp).background(Color.LightGray)) {
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
                        .padding(5.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    item {
//                        Text("Dashboard", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4C60A9))
//                    }

                        Box(
                            modifier = Modifier
                                .padding(16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(gradientSky),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("TODAY IS", fontSize = 50.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(month.uppercase(), fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        Text(day, fontSize = 70.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                        Text(year, fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }
                        }
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(gradientDayLight)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp, horizontal = 18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = timeFormat.format(Date()).uppercase(),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(50.dp)
                                    .background(Color.White.copy(alpha = 0.5f))
                            )
                            // Elapsed Time and Hours Worked
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Sand Timer Icon
                                    Image(
                                        painter = painterResource(id = R.drawable.sand_timer),
                                        contentDescription = "Sand Timer",
                                        modifier = Modifier
                                            .size(26.dp)
                                            .padding(end = 4.dp)
                                    )
                                    // Elapsed Time
                                    Text(
                                        text = "${formatElapsedTime(elapsedTime)}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                                // Hours Worked
                                Text(
                                    text = "HOURS WORKED",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                        TimerProgressBar(elapsedTime = elapsedTime, gradientSunset = gradientSunset)

                        InfoCard(null, "Tracked Hours") {
                            TrackedHoursGraph()
                        }

                }
            }
        }
    }
}
//test test

@Composable
fun InfoCard(
    iconRes: Int?,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                verticalAlignment = Alignment.CenterVertically,
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
                modifier = Modifier.padding(start = if (iconRes != null) 28.dp else 0.dp)
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
