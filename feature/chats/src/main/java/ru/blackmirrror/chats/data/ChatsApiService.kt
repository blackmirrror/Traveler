package ru.blackmirrror.chats.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatsApiService {

    @GET("/chats")
    suspend fun getChats(@Query("userId") userId: Long): Response<List<ChatResponseDto>>

    @POST("/chats")
    suspend fun createChat(@Body userIds: List<Long>): Response<ChatDto>

    @GET("/chats/{id}")
    suspend fun getChat(@Path("id") id: Long): Response<ChatDto>

    @DELETE("/chats/{id}")
    suspend fun deleteChat(@Path("id") id: Long): Response<Unit>
}
