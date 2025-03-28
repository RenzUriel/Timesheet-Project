package com.example.timesheet.ui.screen.postlog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.ui.components.TopBar
import com.example.timesheet.ui.screen.NavigationItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PostLogScreen(navController: NavController, token: String) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDrawerOpen by remember { derivedStateOf { drawerState.isOpen } }

    val postLogViewModel: PostLogViewModel = viewModel()

    // Fetch logs when the screen is first loaded
    LaunchedEffect(key1 = token) {
        postLogViewModel.fetchLogs(token)
    }

    val logs by postLogViewModel.logs.collectAsState()

    Scaffold(

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
                        NavigationItem("Home", navController, R.drawable.home, "home/${token}", Color(0xFF4C60A9))
                        NavigationItem("Attendance", navController, R.drawable.clock, "postlogscreen/${token}", Color(0xFF4C60A9))
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(verticalScrollState)
        ) {
            Text(
                text = "Token: $token",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Attendance")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Report",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Table Header
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .background(Color.Gray)
                    .padding(8.dp)
            ) {
                TableCell(text = "Log ID", isHeader = true)
                TableCell(text = "Attendance Status", isHeader = true)
                TableCell(text = "Time-In", isHeader = true)
                TableCell(text = "Time-Out", isHeader = true)
                TableCell(text = "Tracked Hours", isHeader = true)
//                TableCell(text = "Location", isHeader = true)
            }

            // Table Rows - Logs
            if (logs.isEmpty()) {
                Text(
                    text = "No logs available.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                logs.forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        TableCell(text = log.id?.toString()?.takeLast(4) ?: "N/A")
                        TableCell(text = log.attendanceStatus ?: "N/A")
                        TableCell(text = formatTime(log.timeIn) ?: "N/A")
                        TableCell(text = formatTime(log.timeOut) ?: "N/A")
                        TableCell(text = log.totalHours100?.toString() ?: "N/A")
                    }
                }
            }
        }
    }
}

fun formatDate(timestamp: Long?): String? {
    return if (timestamp != null) {
        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = Date(timestamp)
        sdf.format(date)
    } else {
        null
    }
}

fun formatTime(timestamp: Long?): String? {
    return if (timestamp != null) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault()) // Format to hh:mm AM/PM
        val date = Date(timestamp)
        sdf.format(date)
    } else {
        null
    }
}


@Composable
fun TableCell(text: String, isHeader: Boolean = false) {
    Box(
        modifier = Modifier
//            .weight(1f) // Ensure all cells have equal width in the row
            .height(48.dp) // Consistent height for all cells
            .padding(4.dp)
            .background(if (isHeader) Color.DarkGray else Color.LightGray, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            color = if (isHeader) Color.White else Color.Black,
            maxLines = 1, // To avoid text overflow in small cells
            overflow = TextOverflow.Ellipsis // Truncate long text if necessary
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPostLogScreen() {
    PostLogScreen(navController = rememberNavController(), token = "sampleToken")
}
