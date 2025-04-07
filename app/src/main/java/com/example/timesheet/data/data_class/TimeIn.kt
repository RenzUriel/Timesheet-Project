package com.example.timesheet.data.data_class

import com.google.gson.annotations.SerializedName

data class TimeIn(
    @SerializedName("time-in") val timeIn: Long,
    @SerializedName("Date") val date: Long,
    @SerializedName("attendance_status") val attendanceStatus: String,
    @SerializedName("time-in_mins") val timeInMins: Int,
    @SerializedName("total_late_mins") val totalLateMins: Int,
    @SerializedName("User _id") val userId: String
)


