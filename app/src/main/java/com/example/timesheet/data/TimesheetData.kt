package com.example.timesheet.data

import java.util.Locale
import java.text.SimpleDateFormat

object TimesheetData {
    data class Entry(
        val date: String,
        val timeIn: String,
        val timeOut: String,
        val location: String
    )

    val sampleData = listOf(
        Entry("24/03/2025", "08:00", "16:00", "Cabantian"),
        Entry("25/03/2025", "09:30", "18:00", "Matina"),
        Entry("26/03/2025", "07:45", "15:30", "Cabantian"),
        Entry("27/03/2025", "07:49", "17:05", "Buhangin"),
        Entry("28/03/2025", "07:53", "17:15", "Cabantian"),
        Entry("29/03/2025", "00:00", "00:00", "---"),
        Entry("30/03/2025", "00:00", "00:00", "---"),
    )

    fun calculateHours(timeIn: String, timeOut: String): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val inTime = formatter.parse(timeIn) ?: return "0h 0m"
        val outTime = formatter.parse(timeOut) ?: return "0h 0m"
        val duration = (outTime.time - inTime.time) / 60000
        val hours = duration / 60
        val minutes = duration % 60

        return String.format("%dh %02dm", hours, minutes)
    }

    fun getTrackedHours(): List<Float> {
        return sampleData.map { entry ->
            val calculatedTime = calculateHours(entry.timeIn, entry.timeOut)
            val parts = calculatedTime.split("h", "m").map { it.trim() }
            val hours = parts[0].toFloat()
            val minutes = parts[1].toFloat() / 60
            hours + minutes
        }
    }

    fun calculateTotalWeeklyHours(): String {
        val totalMinutes = sampleData.sumOf { entry ->
            val calculatedTime = calculateHours(entry.timeIn, entry.timeOut)
            val parts = calculatedTime.split("h", "m").map { it.trim() }

            val hours = parts.getOrNull(0)?.toIntOrNull() ?: 0
            val minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0

            (hours * 60) + minutes
        }

        val totalHours = totalMinutes / 60
        val remainingMinutes = totalMinutes % 60

        return String.format("%dh %02dm", totalHours, remainingMinutes)
    }


}