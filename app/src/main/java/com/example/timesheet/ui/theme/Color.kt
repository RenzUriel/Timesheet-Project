package com.example.timesheet.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val PrimaryBlue = Color(0xFF4C60A9)

val gradientSunset = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFF7E5F), Color(0xFFFFA07A), Color(0xFFFFDAB9))
)

val gradientDayLight = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFAA00), Color(0xFFFFC300), Color(0xFFFFDD00))
)

val gradientPurplePink = Brush.verticalGradient(
    colors = listOf(Color(0xFF363553), Color(0xFF903775), Color(0xFFE8458B))
)

val gradientSky = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF4C60A9),
        Color(0xFF50ABE7),
        Color(0xFF6CBDE9),
        Color(0xFF87CEEB),
        Color(0xFFBAE0F3)
    )
)

val gradientBlue = Brush.verticalGradient(
    colors = listOf(Color(0xFF4C60A9), Color(0xFF7083C2), Color(0xFFA3B0DB))
)

val gradientGalactic = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0D0B1A), // Deep Space Black-Blue
        Color(0xFF2A1B4A), // Dark Purple Nebula
        Color(0xFF4C60A9), // Night Sky Blue
        Color(0xFF705DAD), // Soft Cosmic Purple
        Color(0xFFA06CC2), // Luminous Nebula Glow
        Color(0xFFD297E3)  // Faint Galactic Light
    )
)

val gradientSoftCyan = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFE0F7FA), // Very Light Cyan (Near White)
        Color(0xFFB2EBF2), // Soft Cyan Blue
        Color(0xFF81D4FA), // Light Sky Cyan
        Color(0xFF4FC3F7), // Medium Light Cyan
        Color(0xFF29B6F6)  // Slightly Deeper Cyan
    )
)