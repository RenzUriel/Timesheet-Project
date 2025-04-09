package com.example.timesheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.timesheet.ui.screen.HomeScreen
import com.example.timesheet.ui.screen.misc.ForgotScreen
import com.example.timesheet.ui.screen.misc.AttendanceScreen
import com.example.timesheet.ui.screen.misc.SignUpScreen
import com.example.timesheet.ui.screen.login.TimeSheetLoginScreen
import com.example.timesheet.ui.screen.postlog.PostLogScreen

class MainActivity : ComponentActivity() {
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(this)

        setContent {
            val navController = rememberNavController()
            var isClockedIn by remember { mutableStateOf(false) }
            var token by remember { mutableStateOf("") }

            // Check for existing token
            LaunchedEffect(Unit) {
                tokenManager.token.collect { savedToken ->
                    token = savedToken ?: ""
                    if (token.isNotEmpty()) {
                        navController.navigate("home/$token") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }

            NavHost(navController = navController, startDestination = if (token.isEmpty()) "login" else "home/$token") {
                composable("login") { TimeSheetLoginScreen(navController) }
                composable("signup") { SignUpScreen(navController) }
                composable("forgot") { ForgotScreen(navController) }
                composable("home/{token}") { backStackEntry ->
                    val token = backStackEntry.arguments?.getString("token") ?: ""
                    HomeScreen(navController, isClockedIn, token) { isClockedIn = !isClockedIn }
                }
                composable("attendance_screen") { AttendanceScreen(navController, isClockedIn) { isClockedIn = !isClockedIn } }
                composable("logout") { TimeSheetLoginScreen(navController) }
                composable("postlogscreen/{token}") { backStackEntry ->
                    val token = backStackEntry.arguments?.getString("token") ?: ""
                    PostLogScreen(navController, token)
                }
            }
        }
    }
}