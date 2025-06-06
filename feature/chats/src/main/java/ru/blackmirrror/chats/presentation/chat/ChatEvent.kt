package ru.blackmirrror.chats.presentation.chat

sealed class ChatEvent {

    data class SendMessage(val text: String): ChatEvent()

    data class AddMessage(val chatId: Long, val senderId: Long, val text: String): ChatEvent()

    object Back: ChatEvent()
}
