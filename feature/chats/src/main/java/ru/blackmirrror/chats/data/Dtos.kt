package ru.blackmirrror.chats.data

data class ChatDto(
    var chatId: Long,
    var title : String = "",
    var imageUrl : String? = null,
    var lastMessage: String? = null,
    var lastMessageTime: Long? = null,
    var unreadCount: Int = 0,
    var isOnline: Boolean = false,
)

data class ChatResponseDto(
    val id: Long,
    val participantIds: List<Long>
)

data class MessageDto(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val text: String,
    val timestamp: String,
    val readBy: List<Long> = emptyList()
)

data class WebSocketEventDto(
    val type: String,
    val chatId: Long? = null,
    val senderId: Long? = null,
    val message: String? = null,
    val messageId: Long? = null,
    val userId: Long? = null
)
