package com.example.timesheet.api


import android.media.session.MediaSession.Token
import com.example.timesheet.data.models.Logs
import com.example.timesheet.data.models.User
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
        @Field("password") password: String
    ): User

    @FormUrlEncoded
    @GET("logs")
    suspend fun logsUser(
        @Header("token")token: String
    ): Logs
}