package ru.blackmirrror.chats.presentation.chat

import ru.blackmirrror.chats.data.ChatDto
import ru.blackmirrror.chats.data.MessageDto
import ru.blackmirrror.chats.data.SocketEvent

data class ChatUiState(
    val socketEvent: SocketEvent? = null,
    val messages: List<MessageDto>? = null,
    val userId: Long,
//    val chat: ChatDto
)
