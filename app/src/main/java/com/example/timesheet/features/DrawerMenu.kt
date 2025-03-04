package com.example.timesheet.features

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
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

@Composable
fun DrawerMenu(navController: NavController, onClose: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color(0xFFD0D8F0))
                .align(Alignment.CenterEnd)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(80.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.people),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape),
                            tint = Color.Gray
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit Profile",
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .clickable { /* Handle edit click */ },
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Admin", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("Administrator", fontSize = 16.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                // Logout Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDialog = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout Icon",
                        tint = Color(0xFF9A3636),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", color = Color(0xFF9A3636), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("login")
                }) {
                    Text("Yes", color = Color(0xFF4C60A9))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No", color = Color(0xFF4C60A9))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDrawerMenu() {
    val navController = rememberNavController()
    DrawerMenu(navController = navController, onClose = {})
}
