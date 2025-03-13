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
        Entry("3", "08:00", "16:00", "Cabantian"),
        Entry("4", "09:30", "18:00", "Matina"),
        Entry("5", "07:45", "15:30", "Cabantian"),
        Entry("6", "07:49", "17:05", "Buhangin"),
        Entry("7", "07:53", "17:15", "Cabantian"),
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

}
