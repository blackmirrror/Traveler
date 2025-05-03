package ru.blackmirrror.chats.data

import ru.blackmirrror.chats.domain.Chat
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.api.AuthProvider
import ru.blackmirrror.core.api.NetworkProvider
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val networkProvider: NetworkProvider
): ChatsRepository {

    override fun getChats(): List<Chat> {
        return listOf(
            Chat(
                "https://randomuser.me/api/portraits/women/1.jpg",
                "Анастасия",
                "Привет! Как дела?",
                "14:32",
                20
            ),
            Chat(
                "https://randomuser.me/api/portraits/men/2.jpg",
                "Дмитрий",
                "Завтра встречаемся в 10?Завтра встречаемся в 10Завтра встречаемся в 10Завтра встречаемся в 10Завтра встречаемся в 10Завтра встречаемся в 10",
                "13:45",
                0
            ),
            Chat(
                "https://randomuser.me/api/portraits/women/3.jpg",
                "Мария",
                "Отлично! Спасибо :)",
                "12:15",
                1
            ),
            Chat(
                "https://randomuser.me/api/portraits/men/4.jpg",
                "Алексей",
                "Понял, спасибо!",
                "Вчера",
                0
            )
        )
    }

    override fun isAuthenticated(): Boolean {
        return authProvider.isUserAuthenticated()
    }
}
