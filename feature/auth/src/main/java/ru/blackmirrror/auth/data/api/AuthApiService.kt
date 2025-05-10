package ru.blackmirrror.auth.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApiService {

    @POST("/users/register")
    suspend fun registerUser(@Body user: UserDto): Response<UserDto>

    @PUT("/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: UserDto): Response<UserDto>

    @GET("/users/phone/{phone}")
    suspend fun getUserByPhone(@Path("phone") phone: String): Response<UserDto>
}