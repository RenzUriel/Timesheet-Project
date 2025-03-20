package com.example.timesheet.ui.theme

import android.graphics.Shader
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush

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
val gradientSky = Brush.verticalGradient(
    colors = listOf(
        //Color(0xFF4C60A9),
        Color(0xFF50ABE7),
        Color(0xFF6CBDE9),
        Color(0xFF87CEEB),
        Color(0xFFBAE0F3)
    )
)

val gradientSoftCyan = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF13739F),
        Color(0xFF29B6F6)
    ),
    startX = 0f,
    endX = 1000f
)

val largeRadialGradient = object : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val biggerDimension = maxOf(size.height, size.width)
        return RadialGradientShader(
            colors = listOf(Color(0xFF29B6F6), Color(0xFF4C60A9)),
            center = size.center,
            radius = biggerDimension / 2f,
            colorStops = listOf(0f, 0.95f)
        )
    }
}
