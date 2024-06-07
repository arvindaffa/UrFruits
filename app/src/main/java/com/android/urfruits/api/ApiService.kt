package com.android.urfruits.api

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class User(
    val name: String,
    val email: String,
    val password: String
)

data class ApiResponse(
    val status: String,
    val message: String,
    val user: UserResponse
)

data class UserResponse(
    @SerializedName("id") val Id: String,
    val email: String,
    val name: String
)

interface ApiService {
    @POST("register")
    fun registerUser(@Body user: User): Call<ApiResponse>
}
