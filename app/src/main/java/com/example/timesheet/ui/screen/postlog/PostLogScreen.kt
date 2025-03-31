package com.example.timesheet.ui.screen.postlog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.features.AttendanceAnalytics
import com.example.timesheet.features.DrawerMenu
import com.example.timesheet.ui.components.TopBar
import com.example.timesheet.ui.screen.NavigationItem
import com.example.timesheet.ui.theme.gradientSoftCyan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostLogScreen(navController: NavController, token: String) {
    val screenScrollState = rememberScrollState()
    val cardVerticalScrollState = rememberScrollState() // 🔹 UPDATED
    val cardHorizontalScrollState = rememberScrollState() // 🔹 UPDATED
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val postLogViewModel: PostLogViewModel = viewModel()

    LaunchedEffect(key1 = token) {
        postLogViewModel.fetchLogs(token)
    }

    val logs by postLogViewModel.logs.collectAsState()

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
            topBar = { TopBar(navController) { scope.launch { drawerState.open() } } },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.height(65.dp),
                    containerColor = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        NavigationItem("Home", navController, R.drawable.home, "home/${token}")
                        NavigationItem("Attendance", navController, R.drawable.clock, "postlogscreen/${token}", Color(0xFF4C60A9))
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(screenScrollState)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    AttendanceAnalytics()
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(gradientSoftCyan)
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.activity_tracker),
                                contentDescription = "Log Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Log Report",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .scrollIndicators(
                                verticalState = cardVerticalScrollState,
                                horizontalState = cardHorizontalScrollState
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(cardVerticalScrollState)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .horizontalScroll(cardHorizontalScrollState)
                            ) {
                                TableCell(text = "Date", isHeader = true, modifier = Modifier.width(100.dp))
                                TableCell(text = "Time-In", isHeader = true, modifier = Modifier.width(100.dp))
                                TableCell(text = "Time-Out", isHeader = true, modifier = Modifier.width(100.dp))
                                TableCell(text = "Tracked Hours", isHeader = true, modifier = Modifier.width(150.dp))
                                TableCell(text = "Status", isHeader = true, modifier = Modifier.width(100.dp))
                                TableCell(text = "Log ID", isHeader = true, modifier = Modifier.width(80.dp))
                            }

                            if (logs.isEmpty()) {
                                Text(
                                    text = "No logs available.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                logs.forEach { log ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                            .horizontalScroll(cardHorizontalScrollState) // 🔹 UPDATED
                                    ) {
                                        TableCell(text = formatDate(log.date) ?: "N/A", modifier = Modifier.width(100.dp))
                                        TableCell(text = formatTime(log.timeIn) ?: "N/A", modifier = Modifier.width(100.dp))
                                        TableCell(text = formatTime(log.timeOut) ?: "N/A", modifier = Modifier.width(100.dp))
                                        TableCell(text = log.totalHours100?.toString() ?: "N/A", modifier = Modifier.width(150.dp))
                                        TableCell(text = log.attendanceStatus ?: "N/A", modifier = Modifier.width(100.dp))
                                        TableCell(text = log.id?.toString()?.takeLast(4) ?: "N/A", modifier = Modifier.width(80.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun formatDate(timestamp: Long?): String? {
    return if (timestamp != null) {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) // MM for months
        val date = Date(timestamp) // No need for * 1000 if already in milliseconds
        sdf.format(date)
    } else {
        null
    }
}

fun formatTime(timestamp: Long?): String? {
    return if (timestamp != null) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault()) // 12-hour format with AM/PM
        val date = Date(timestamp) // No need for * 1000 if already in milliseconds
        sdf.format(date)
    } else {
        null
    }
}


@Composable
fun TableCell(
    text: String,
    isHeader: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp) // Consistent height for all cells
            .padding(4.dp)
            .background(if (isHeader) Color.LightGray else Color.Transparent, RoundedCornerShape(4.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            color = if (isHeader) Color.White else Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun Modifier.scrollIndicators(
    verticalState: ScrollState? = null,
    horizontalState: ScrollState? = null
): Modifier {
    var isScrollingVertically by remember { mutableStateOf(false) }
    var isScrollingHorizontally by remember { mutableStateOf(false) }

    LaunchedEffect(verticalState?.value) {
        if (verticalState != null) {
            isScrollingVertically = true
            delay(500)
            isScrollingVertically = false
        }
    }

    LaunchedEffect(horizontalState?.value) {
        if (horizontalState != null) {
            isScrollingHorizontally = true
            delay(500)
            isScrollingHorizontally = false
        }
    }

    return this.then(
        Modifier.drawWithContent {
            drawContent()

            if (verticalState != null && verticalState.maxValue > 0 && isScrollingVertically) {
                drawRect(
                    color = Color.Gray.copy(alpha = 0.5f),
                    topLeft = Offset(size.width - 8.dp.toPx(), (size.height * verticalState.value / verticalState.maxValue)),
                    size = Size(8.dp.toPx(), 50.dp.toPx())
                )
            }

            if (horizontalState != null && horizontalState.maxValue > 0 && isScrollingHorizontally) {
                drawRect(
                    color = Color.Gray.copy(alpha = 0.5f),
                    topLeft = Offset((size.width * horizontalState.value / horizontalState.maxValue), size.height - 8.dp.toPx()),
                    size = Size(50.dp.toPx(), 8.dp.toPx())
                )
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun PreviewPostLogScreen() {
    PostLogScreen(navController = rememberNavController(), token = "sampleToken")
}
