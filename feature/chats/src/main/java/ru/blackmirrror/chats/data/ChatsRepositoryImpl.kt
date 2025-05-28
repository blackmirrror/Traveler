package ru.blackmirrror.chats.data

import ru.blackmirrror.chats.domain.Chat
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.provider.AuthProvider
import ru.blackmirrror.core.provider.NetworkProvider
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val networkProvider: NetworkProvider
): ChatsRepository {

    override fun getChats(): List<Chat> {
        return listOf(
            Chat(
                "https://randomuser.me/api/portraits/women/17.jpg",
                "Анастасия",
                "Можно еще туда съездить в след раз",
                "14:32",
                3
            ),
            Chat(
                "https://randomuser.me/api/portraits/men/44.jpg",
                "Dima",
                "Завтра встречаемся в 10? Женя сказал можно пораньше в 10Завтра встречаемся в 10Завтра встречаемся в 10Завтра встречаемся в 10",
                "13:45",
                1
            ),
            Chat(
                "https://randomuser.me/api/portraits/lego/6.jpg",
                "Penguin",
                "Это на границе с Грузией",
                "Вчера",
                0
            ),
            Chat(
                "https://randomuser.me/api/portraits/men/23.jpg",
                "Алексей",
                "Понял, спасибо!",
                "15.03.25",
                0
            ),
            Chat(
                "https://randomuser.me/api/portraits/men/2.jpg",
                "Misha",
                "Только если после 18, работа",
                "12.03.25",
                0
            )
        )
    }

    override fun isAuthenticated(): Boolean {
        return authProvider.isUserAuthenticated()
    }
}
