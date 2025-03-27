package com.example.timesheet.data.repository

import android.util.Log
import com.example.timesheet.api.RetrofitInstance
import com.example.timesheet.data.models.LogEntry
import com.example.timesheet.data.models.Logs

class LogsRepository {
    suspend fun getLogsUser (authToken: String): List<LogEntry> {
        Log.d("Logs Repository", "Retrieving user logs with auth token: $authToken")
        val logsResponse: Logs = RetrofitInstance.api.logsUser ("Bearer $authToken") // Call the API to get the logs response

        Log.d("Logs Repository", "Number of logs retrieved: ${logsResponse.response.logs.size}")

        // Optionally log each log entry
        logsResponse.response.logs.forEachIndexed { index, logEntry ->
            Log.d("Logs Repository", "Log Entry #$index: $logEntry")
        }

        return logsResponse.response.logs // list of log entries
    }
}