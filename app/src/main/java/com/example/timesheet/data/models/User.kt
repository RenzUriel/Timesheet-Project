package com.example.timesheet.data.models

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

