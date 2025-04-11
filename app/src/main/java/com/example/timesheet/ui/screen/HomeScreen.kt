package com.example.timesheet.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.data.LocalAttendanceDataProvider.getAttendanceData
import com.example.timesheet.data.TimesheetData
import com.example.timesheet.data.TrackedHoursGraph
import com.example.timesheet.features.ClockInOutButton
import com.example.timesheet.features.DrawerMenu
import com.example.timesheet.features.TimerProgressBar
import com.example.timesheet.ui.components.TopBar
import com.example.timesheet.ui.theme.gradientDayLight
import com.example.timesheet.ui.theme.gradientNightLight
import com.example.timesheet.ui.theme.gradientSky
import com.example.timesheet.ui.theme.gradientSky2
import com.example.timesheet.ui.theme.gradientSoftCyan
import com.example.timesheet.ui.theme.gradientSunset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val REQUEST_CODE = 100
    val currentLocation = getCurrentLocation(context)


    var isDrawerOpen by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    val permissionGranted = remember { mutableStateOf(false) } // Request location permission when HomeScreen is displayed
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        } else {
            permissionGranted.value = true
        }
    }

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
                            .background(gradientSoftCyan)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (permissionGranted.value) {
                                "Current Location: $currentLocation"
                            } else {
                                "Requesting Location Permission..."
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(getGradientForTime())
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
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .fillMaxWidth()
                    ) {
                        Column {
                            // Header
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(gradientSoftCyan)
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Tracked Hours",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.TopStart)
                                )
                                Text(
                                    text = "${TimesheetData.calculateTotalWeeklyHours()} worked",
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )
                            }

                            TrackedHoursGraph()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .background(Color(0xFF29B6F6))
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getGradientForTime(): Brush {
    val now = java.util.Calendar.getInstance()
    val hour = now.get(java.util.Calendar.HOUR_OF_DAY)
    val minute = now.get(java.util.Calendar.MINUTE)

    val totalMinutes = hour * 60 + minute

    return when {
        totalMinutes in (5 * 60 + 30)..(7 * 60 + 59) -> gradientDayLight  // 5:30 AM - 7:59 AM
        totalMinutes in (8 * 60)..(17 * 60 + 59) -> gradientSky2          // 8:00 AM - 5:59 PM
        else -> gradientNightLight                                        // 6:00 PM - 5:29 AM
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
@Composable
fun getCurrentLocation(context: Context): String {
    val locationState = remember { mutableStateOf("Fetching location...") }

    LaunchedEffect(Unit) {
        val location = withContext(Dispatchers.IO) { fetchLocation(context) }
        locationState.value = location ?: "Location unavailable"
    }

    return locationState.value
}

@SuppressLint("MissingPermission")
fun fetchLocation(context: Context): String? {
    val locationManager = getSystemService(context, LocationManager::class.java) ?: return "Location service unavailable"

    // Check permissions before accessing location
    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return "Location permission not granted"
    }

    val location: android.location.Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

    return if (location != null) {
        val latitude = location.latitude
        val longitude = location.longitude
        getAddressFromCoordinates(context, latitude, longitude)
    } else {
        "Location not found"
    }
}
fun getAddressFromCoordinates(context: Context, latitude: Double, longitude: Double): String { // Convert latitude and longitude into a human-readable address using Geocoder
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        if (!addresses.isNullOrEmpty()) { // if addresses is not null and has at least one element
            // "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
//            "${address.getAddressLine(0)}, ${address.locality}"
            val address = addresses[0]
            val sublocale = address.subLocality ?: ""
            val city = address.locality ?: ""

            // Return the formatted string with the sublocality and city
            if (sublocale.isNotEmpty() && city.isNotEmpty()) {
                "$sublocale, $city"
            } else {
                // Fallback if either sublocality or city is not available
                "Address not fully available"
            }
        } else {
            "Address not found"
        }
    } catch (e: Exception) {
        "Error fetching address: ${e.localizedMessage}"
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    var isClockedIn by remember { mutableStateOf(false) }

    HomeScreen(navController, isClockedIn) { isClockedIn = !isClockedIn }
}
