package com.example.timesheet.data

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TrackedHoursGraph() {
    val hoursTracked = listOf(0f, 7.25f, 6.75f, 8.0f, 4.5f, 9.0f, 0f)
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val maxHours = 10f
    val barColor = Color(0xFF4C60A9)
    val gridColor = Color.Gray.copy(alpha = 0.3f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val graphWidth = size.width
                val graphHeight = size.height - 40.dp.toPx()
                val leftPadding = 50.dp.toPx()
                val rightPadding = 80.dp.toPx()
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
                        hour.toString(),
                        xPos,
                        topPadding,
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

                hoursTracked.forEachIndexed { index, hours ->
                    val barWidth = hours * xScale
                    val yOffset = ((index + 1) * yStep) + topPadding

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(leftPadding, yOffset - yStep / 4),
                        size = Size(barWidth, yStep / 2),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        daysOfWeek[index],
                        5f,
                        yOffset + 10f,
                        textPaint
                    )

                    val hoursInt = hours.toInt()
                    val minutes = ((hours - hoursInt) * 60).toInt()
                    val timeText = String.format("%dh %02dm", hoursInt, minutes)
                    val textWidth = textPaint.measureText(timeText)
                    val textX = graphWidth - textWidth - 50f

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
