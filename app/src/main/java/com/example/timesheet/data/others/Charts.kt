package com.example.timesheet.data.others

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TrackedHoursGraph() {
    val hoursTracked = TimesheetData.getTrackedHours()
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
    val maxHours = 10f
    val barColor = Color(0xFF29B6F6)
    val gridColor = Color.Gray.copy(alpha = 0.0f)

    val animatedWidths = remember {
        hoursTracked.map { Animatable(0f) }
    }


    LaunchedEffect(Unit) {
        animatedWidths.forEachIndexed { index, animatable ->
            delay(index * 100L)
            animatable.animateTo(
                targetValue = hoursTracked[index],
                animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val graphWidth = size.width
                val graphHeight = size.height - 40.dp.toPx()
                val leftPadding = 40.dp.toPx()
                val rightPadding = 40.dp.toPx()
                val xScale = (graphWidth - leftPadding - rightPadding) / maxHours
                val topPadding = 40f

                val textPaint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }

                for (hour in 0..10 step 2) {
                    val xPos = leftPadding + (hour * xScale)

                    drawContext.canvas.nativeCanvas.drawText(
                        "${hour}h",
                        xPos - textPaint.measureText("${hour}h") / 2,
                        topPadding - 10f,
                        textPaint
                    )

                    drawLine(
                        color = gridColor,
                        start = Offset(xPos, topPadding + 10.dp.toPx()),
                        end = Offset(xPos, graphHeight),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                val yStep = (graphHeight - topPadding) / (hoursTracked.size + 1)

                hoursTracked.forEachIndexed { index, _ ->
                    val barWidth = animatedWidths[index].value * xScale
                    val yOffset = ((index + 1) * yStep) + topPadding

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(leftPadding, yOffset - yStep / 4),
                        size = Size(barWidth, yStep / 2),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        daysOfWeek[index],
                        25f,
                        yOffset + 10f,
                        textPaint
                    )

                    val hoursInt = hoursTracked[index].toInt()
                    val minutes = ((hoursTracked[index] - hoursInt) * 60).toInt()
                    val timeText = String.format("%dh %02dm", hoursInt, minutes)
                    val textWidth = textPaint.measureText(timeText)
                    val textX = graphWidth - textWidth + 15f

                    drawContext.canvas.nativeCanvas.drawText(
                        timeText,
                        textX,
                        yOffset + 10f,
                        textPaint
                    )
                }

                drawRect(
                    color = gridColor,
                    topLeft = Offset(leftPadding, topPadding + 10.dp.toPx()),
                    size = Size(graphWidth - leftPadding - rightPadding, graphHeight - topPadding),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrackedHoursGraph() {
    Surface(color = MaterialTheme.colorScheme.background) {
        TrackedHoursGraph()
    }
}

//test
