package com.example.timesheet.data.repository


import android.util.Log
import com.example.timesheet.api.RetrofitInstance
import com.example.timesheet.api.RetrofitInstance.api
import com.example.timesheet.data.User
class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun loginUser(email: String, password: String): User {
        Log.d("UserRepository", "Logging in user: $email")

        return api.loginUser(email, password).also {
            Log.d("UserRepository", "Login response: $it")
        }
    }
}
