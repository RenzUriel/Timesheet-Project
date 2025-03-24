package com.example.timesheet.api


import com.example.timesheet.data.LogsResponse
import com.example.timesheet.data.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String): User

    @GET("logs")
    suspend fun getLogs(
        @Header("Authorization") token: String
    ): LogsResponse


}