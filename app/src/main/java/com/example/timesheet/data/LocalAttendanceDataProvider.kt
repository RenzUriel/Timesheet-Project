package com.example.timesheet.data

import com.example.timesheet.data.AttendanceSheetData

object LocalAttendanceDataProvider {
    val defaultAttendance = getAttendanceData()[0]

    fun getAttendanceData(): List<AttendanceSheetData> {
        return listOf(
            AttendanceSheetData(
                date = "2025-02-27",
                timeIn = "09:00 AM",
                timeOut = "05:00 PM",
                location = "Office"
            ),
            AttendanceSheetData(
                date = "2025-02-26",
                timeIn = "09:15 AM",
                timeOut = "05:15 PM",
                location = "Home Office"
            ),
            AttendanceSheetData(
                date = "2025-02-25",
                timeIn = "09:30 AM",
                timeOut = "05:00 PM",
                location = "Client Site"
            )
            // Add more attendance records as needed
        )
    }
}