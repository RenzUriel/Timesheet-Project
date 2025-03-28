package com.example.timesheet.data.repository


import android.util.Log
import com.example.timesheet.networks.RetrofitInstance
import com.example.timesheet.networks.RetrofitInstance.api
import com.example.timesheet.data.data_class.User
import com.example.timesheet.data.data_class.UserLogs

class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun loginUser(email: String, password: String): User {
        Log.d("UserRepository", "Logging in user: $email")
        return api.loginUser(email, password).also {
            Log.d("UserRepository", "Login response: $it")
        }
    }
}
class LogsRepository {

    suspend fun getLogs(token: String): List<UserLogs> {
        Log.d("LogsRepository", "Fetching logs with token: $token")

        val response = api.getLogs("Bearer $token")

        Log.d("LogsRepository", "Raw response status: ${response.status}")
        Log.d("LogsRepository", "Logs count: ${response.response.logs.size}")

        response.response.logs.forEachIndexed { index, log ->
            Log.d("LogsRepository", "Log #$index: $log")
        }

        return response.response.logs
    }


}
