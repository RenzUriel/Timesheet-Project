package com.example.timesheet.api


import com.example.timesheet.data.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): User

    @FormUrlEncoded
    @GET
    suspend fun logs(
        @Field("_id") id: Int,
        @Field("User_id") user_id: Int
    )
}