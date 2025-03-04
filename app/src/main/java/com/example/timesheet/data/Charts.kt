package com.example.timesheet.data

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Ensures horizontal centering
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .align(Alignment.CenterHorizontally) // Center the graph horizontally
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val graphWidth = size.width
                val graphHeight = size.height - 40.dp.toPx()
                val leftPadding = 40.dp.toPx()  // Reduced left padding to allow more grid space
                val rightPadding = 40.dp.toPx() // Reduced right padding for a wider graph
                val xScale = (graphWidth - leftPadding - rightPadding) / maxHours
                val topPadding = 40f

                val textPaint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 30f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }

                // Draw hour labels and vertical gridlines
                for (hour in 0..10 step 2) { // Step increased for more evenly spaced gridlines
                    val xPos = leftPadding + (hour * xScale)

                    // Draw hour labels at the top
                    drawContext.canvas.nativeCanvas.drawText(
                        "${hour}h",
                        xPos - textPaint.measureText("${hour}h") / 2,
                        topPadding - 10f,
                        textPaint
                    )

                    // Draw vertical gridlines (wider grid effect)
                    drawLine(
                        color = gridColor,
                        start = Offset(xPos, topPadding + 10.dp.toPx()),
                        end = Offset(xPos, graphHeight),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                val yStep = (graphHeight - topPadding) / (hoursTracked.size + 1)

                // Draw bars and labels
                hoursTracked.forEachIndexed { index, hours ->
                    val barWidth = hours * xScale
                    val yOffset = ((index + 1) * yStep) + topPadding

                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(leftPadding, yOffset - yStep / 4),
                        size = Size(barWidth, yStep / 2),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Draw day labels on the left
                    drawContext.canvas.nativeCanvas.drawText(
                        daysOfWeek[index],
                        + 25f,
                        yOffset + 10f,
                        textPaint
                    )

                    // Format and draw hours worked text
                    val hoursInt = hours.toInt()
                    val minutes = ((hours - hoursInt) * 60).toInt()
                    val timeText = String.format("%dh %02dm", hoursInt, minutes)
                    val textWidth = textPaint.measureText(timeText)
                    val textX = graphWidth - textWidth + 15f  // Adjusted to fit wider grid

                    drawContext.canvas.nativeCanvas.drawText(
                        timeText,
                        textX,
                        yOffset + 10f,
                        textPaint
                    )
                }

                // Draw graph boundary (now wider)
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
