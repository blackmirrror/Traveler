package ru.blackmirrror.chats.domain

interface ChatsRepository {

    fun getChats(): List<Chat>

    fun isAuthenticated(): Boolean
}
