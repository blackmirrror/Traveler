package ru.blackmirrror.chats.data

sealed class SocketEvent {

    data class NewMessage(val chatId: Long, val senderId: Long, val text: String): SocketEvent()

    data class Typing(val chatId: Long, val userId: Long): SocketEvent()

    data class MessageRead(val messageId: Long, val userId: Long): SocketEvent()

    data class SendMessage(val chatId: Long, val text: String): SocketEvent()
}
