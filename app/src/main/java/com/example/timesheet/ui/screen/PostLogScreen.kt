package com.example.timesheet.ui.screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.ui.components.TopBar
import com.example.timesheet.data.TimesheetData
import kotlinx.coroutines.launch

@Composable
fun PostLogScreen(navController: NavController) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isDrawerOpen by remember { derivedStateOf { drawerState.isOpen } }

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
                        NavigationItem("Home", navController, R.drawable.home, "home")
                        NavigationItem("Attendance", navController, R.drawable.clock, "attendance_screen", Color(0xFF4C60A9))
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
                TableCell(text = "Date", isHeader = true)
                TableCell(text = "Time-In", isHeader = true)
                TableCell(text = "Time-Out", isHeader = true)
                TableCell(text = "Tracked Hours", isHeader = true)
                TableCell(text = "Location", isHeader = true)
            }

            Column {
                TimesheetData.sampleData
                    .filter { it.timeIn != "00:00" || it.timeOut != "00:00" }
                    .forEach { entry ->
                        Row(modifier = Modifier.horizontalScroll(horizontalScrollState)) {
                            TableCell(text = entry.date)
                            TableCell(text = entry.timeIn)
                            TableCell(text = entry.timeOut)
                            TableCell(text = TimesheetData.calculateHours(entry.timeIn, entry.timeOut))
                            TableCell(text = entry.location)
                        }
                    }
            }
        }
    }
}

@Composable
fun TableCell(text: String, isHeader: Boolean = false) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .padding(4.dp)
            .background(if (isHeader) Color.DarkGray else Color.LightGray, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
            color = if (isHeader) Color.White else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostLogScreen() {
    PostLogScreen(navController = rememberNavController())
}