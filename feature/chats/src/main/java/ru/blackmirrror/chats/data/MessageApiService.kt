package ru.blackmirrror.chats.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MessageApiService {

    @GET("/messages")
    suspend fun getMessages(@Query("chatId") chatId: Long): Response<List<MessageDto>>
}
