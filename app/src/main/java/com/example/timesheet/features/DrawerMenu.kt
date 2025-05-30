package com.example.timesheet.features

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timesheet.R
import com.example.timesheet.ui.theme.largeRadialGradient

@Composable
fun DrawerMenu(navController: NavController, onClose: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    DrawerMenuUI(
        onLogoutClick = { showDialog = true },
        onEditProfileClick = { /* Handle profile edit click */ }
    )

    if (showDialog) {
        LogoutDialog(
            onConfirm = {
                showDialog = false
                navController.navigate("login")
            },
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun DrawerMenuUI(onLogoutClick: () -> Unit, onEditProfileClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(largeRadialGradient)
                .align(Alignment.CenterEnd)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderSection()
                Spacer(modifier = Modifier.height(24.dp))
                ProfileSection(onEditProfileClick)
                Spacer(modifier = Modifier.weight(0.8f))
                LogoutButton(onLogoutClick)
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.jairosoft_logo),
            contentDescription = "Jairosoft Logo",
            modifier = Modifier.size(34.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Jairosoft",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun ProfileSection(onEditProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(90.dp), contentAlignment = Alignment.BottomEnd) {
            Icon(
                painter = painterResource(id = R.drawable.placeholder_profile),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape),
                tint = Color.White
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onEditProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(18.dp),
                    tint = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Admin", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Administrator", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLogoutClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = "Logout Icon",
            tint = Color.LightGray,
            modifier = Modifier.size(38.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Logout",
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
    }
}

@Composable
fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Logout") },
        text = { Text("Are you sure you want to log out?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes", color = Color(0xFF4C60A9))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No", color = Color(0xFF4C60A9))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawerMenu() {
    val navController = rememberNavController()
    DrawerMenu(navController = navController, onClose = {})
}
