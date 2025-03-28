package com.example.timesheet.data

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
            ),
            AttendanceSheetData(
                date = "2025-02-24",
                timeIn = "08:45 AM",
                timeOut = "05:32 PM",
                location = "Cabantian"
            ),
            AttendanceSheetData(
                date = "2025-02-23",
                timeIn = "09:12 AM",
                timeOut = "05:02 PM",
                location = "Client Site"
            ),
            AttendanceSheetData(
                date = "2025-02-22",
                timeIn = "09:00 AM",
                timeOut = "05:17 PM",
                location = "Cabantian"
            ),
            AttendanceSheetData(
                date = "2025-02-21",
                timeIn = "08:30 AM",
                timeOut = "05:11 PM",
                location = "Buhangin"
            )
            // Add more attendance records as needed
        )
    }
}


