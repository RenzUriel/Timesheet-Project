package com.example.timesheet.data.data_class
import com.google.gson.annotations.SerializedName

data class User(
    val status: String,
    val response: UserResponse
) {
    val token: String get() = response.token
}

data class UserResponse(
    val token: String,
    val user_id: String,
    val expires: Long
)
data class UserLogs(
    @SerializedName("User_id") val userId: String,
    @SerializedName("Attendance Status") val attendanceStatus: String,
    @SerializedName("Created Date") val createdDate: Long,
    @SerializedName("time-out") val timeOut: Long,
    @SerializedName("_id") val id: String,
    @SerializedName("Modified Date") val modifiedDate: Long,
    @SerializedName("time-in") val timeIn: Long,
    @SerializedName("toggle") val toggle: String,
    @SerializedName("Total Hours 100%") val totalHours100: Int?,
    @SerializedName("Date") val date: Long,
    @SerializedName("Created By") val createdBy: String
)


data class LogsResponse(
    val status: String,
    val response: LogsList
)

data class LogsList(
    @SerializedName("Logs")
    val logs: List<UserLogs>
)

data class ErrorResponse(
    val statusCode: Int?,
    val reason: String?,
    val message: String?
)
