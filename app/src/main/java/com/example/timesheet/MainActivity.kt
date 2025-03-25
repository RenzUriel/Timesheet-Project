package com.example.timesheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.timesheet.ui.screen.ForgotScreen
import com.example.timesheet.ui.screen.TimeSheetLoginScreen
import com.example.timesheet.ui.screen.HomeScreen
import com.example.timesheet.ui.screen.AttendanceScreen
import com.example.timesheet.ui.screen.PostLogScreen
import com.example.timesheet.ui.screen.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var isClockedIn by remember { mutableStateOf(false) }

            NavHost(navController = navController, startDestination = "login") {
                composable("login") { TimeSheetLoginScreen(navController) }
                composable("signup") { SignUpScreen(navController) }
                composable("forgot") { ForgotScreen(navController) }
                composable("home") { HomeScreen(navController, isClockedIn) { isClockedIn = !isClockedIn } }
                composable("attendance_screen") { AttendanceScreen(navController, isClockedIn) { isClockedIn = !isClockedIn } }
                composable("logout") { TimeSheetLoginScreen(navController) }
                composable("postlogscreen") { PostLogScreen(navController) }
            }
        }
    }
}