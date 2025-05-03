package ru.blackmirrror.chats.domain

data class Chat(
    val avatar: String,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int
)
