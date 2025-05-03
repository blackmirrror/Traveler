package ru.blackmirrror.chats.presentation.chats

sealed class ChatsEvent {
    object LoadChats: ChatsEvent()
    object ToChat: ChatsEvent()
    object ToAuth: ChatsEvent()
    object HideSnackbar: ChatsEvent()
}
