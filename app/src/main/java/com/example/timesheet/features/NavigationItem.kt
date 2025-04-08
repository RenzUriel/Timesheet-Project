package com.example.timesheet.features

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun NavigationItem(
    label: String,
    navController: NavController,
    iconRes: Int,
    route: String,
    color: Color = Color.Gray,
    onClick: (() -> Unit)? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = {
            // Navigate with popUpTo to avoid duplicate backstack
            navController.navigate(route) {
                // If the destination already exists in the back stack, it will pop it
                launchSingleTop = true
                restoreState = true
                popUpTo(route) { inclusive = true }
            }
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


