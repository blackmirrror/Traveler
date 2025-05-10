package ru.blackmirrror.account.data.api

import retrofit2.Response
import retrofit2.http.*

interface AccountApiService {

    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): Response<UserDto>

    @GET("/users/phone/{phone}")
    suspend fun getUserByPhone(@Path("phone") phone: String): Response<UserDto>

    @PUT("/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: UserDto): Response<UserDto>

    @DELETE("/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<Void>

    @GET("/users/query/{q}")
    suspend fun searchUsers(@Path("q") query: String): Response<List<UserDto>>
}
