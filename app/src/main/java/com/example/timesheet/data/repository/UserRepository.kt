package com.example.timesheet.data.repository

import android.util.Log
import com.example.timesheet.api.RetrofitInstance
import com.example.timesheet.data.models.User


class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun loginUser (email: String, password: String): User {
        Log.d("Login UserRepository", "Logging in user: $email password:$password")

        return api.loginUser (email, password).also {
            Log.d("Login UserRepository", "Login response: $it")
        }
    }
}