package com.android.urfruits.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class User(
    val name: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class ApiResponse(
    val status: String,
    val message: String,
    val user: UserResponse,
)

data class UserResponse(
    @SerializedName("id") val id: String,
    val email: String,
    val name: String,
    val token: String
)

interface ApiService {
    @POST("register")
    fun registerUser(@Body user: User): Call<ApiResponse>

    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<ApiResponse>

    @GET("users/{id}")
    fun getUser(@Header("Authorization") token: String, @Path("id") userId: String): Call<ApiResponse>
}