package com.example.timesheet.data.repository


import android.util.Log
import com.example.timesheet.api.RetrofitInstance
import com.example.timesheet.api.RetrofitInstance.api
import com.example.timesheet.data.User
import com.example.timesheet.data.UserLogs

class UserRepository {

    private val api = RetrofitInstance.api


    suspend fun loginUser(email: String, password: String): User {
        Log.d("UserRepository", "Logging in user: $email")
        return api.loginUser(email, password).also {
            Log.d("UserRepository", "Login response: $it")
        }
    }
}


class AnalyticsRepository {

    suspend fun getLogs(token: String): List<UserLogs> {
        Log.d("AnalyticsRepository", "Fetching logs with token: $token")

        val response = api.getLogs("Bearer $token")

        Log.d("AnalyticsRepository", "Raw response status: ${response.status}")
        Log.d("AnalyticsRepository", "Logs count: ${response.response.logs.size}")

        response.response.logs.forEachIndexed { index, log ->
            Log.d("AnalyticsRepository", "Log #$index: $log")
        }

        return response.response.logs
    }


}