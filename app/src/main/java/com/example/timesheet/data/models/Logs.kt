package com.example.timesheet.data.models

import com.google.gson.annotations.SerializedName

data class Logs(
    val status: String,
    val response: LogsResponse
)

data class LogsResponse(
    @SerializedName("Logs") val logs: List<LogEntry>
)

data class LogEntry(
    @SerializedName("User _id") val userId: String,
    @SerializedName("Attendance Status") val attendanceStatus: String,
    @SerializedName("Created Date") val createdDate: Long,
    @SerializedName("time-out") val timeOut: Long,
    @SerializedName("_id") val id: String,
    @SerializedName("Modified Date") val modifiedDate: Long,
    @SerializedName("time-in") val timeIn: Long,
    @SerializedName("toggle") val toggle: String,
    @SerializedName("Total Hours 100%") val totalHours100: Int,
    @SerializedName("Date") val date: Long,
    @SerializedName("Created By") val createdBy: String
)