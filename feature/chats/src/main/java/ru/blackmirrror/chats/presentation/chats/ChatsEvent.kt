package ru.blackmirrror.chats.presentation.chats

sealed class ChatsEvent {

    object LoadChats: ChatsEvent()

    data class ToChat(val chatId: Long): ChatsEvent()

    object ToAuth: ChatsEvent()

    object HideSnackbar: ChatsEvent()
}
