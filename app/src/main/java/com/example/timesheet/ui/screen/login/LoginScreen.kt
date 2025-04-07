package com.example.timesheet.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timesheet.R
import com.example.timesheet.ui.components.InputField
import com.example.timesheet.ui.components.StandardButton

@Composable
fun TimeSheetLoginScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.jairosoft_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Let's bring your digital dreams to life",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
        InputField(label = "Email", value = email, onValueChange = { email = it }, imeAction = ImeAction.Next)
        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
            passwordVisible = passwordVisible,
            onVisibilityToggle = { passwordVisible = !passwordVisible },
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Forgot Password?",
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth().clickable { navController.navigate("forgot") },
            color = Color(0xFF4C60A9)
        )

        Spacer(modifier = Modifier.height(50.dp))

        StandardButton(
            text = "Login",
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    loginViewModel.login(
                        email = email,
                        password = password,
                        onSuccess = { token ->
                            Log.e("LoginViewModel", "Login response status: Success")
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                            navController.navigate("home/$token")
                        },
                        onFailure = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                    )
                } else {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Create a new account",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable { navController.navigate("signup") },
            style = MaterialTheme.typography.bodySmall
        )
    }

    Box(modifier = Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.BottomCenter) {
        Text("© 2025 Jairosoft", color = Color(0xFF4C60A9), textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeSheetLoginScreen() {
    TimeSheetLoginScreen(navController = rememberNavController())
}
