package com.example.timesheet.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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

