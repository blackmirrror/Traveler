package ru.blackmirrror.chats.domain

import kotlinx.coroutines.flow.Flow
import ru.blackmirrror.chats.data.ChatDto
import ru.blackmirrror.chats.data.MessageDto
import ru.blackmirrror.core.state.ResultState

interface ChatsRepository {

    fun isAuthenticated(): Boolean

    suspend fun getChats(): Flow<ResultState<List<ChatDto>>>

    suspend fun getMessages(chatId: Long): Flow<ResultState<List<MessageDto>>>

    fun getUserId(): Long
}
