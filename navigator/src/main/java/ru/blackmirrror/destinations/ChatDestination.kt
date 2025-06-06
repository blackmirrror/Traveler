package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.blackmirrror.navigator.NavigationDestination
import ru.blackmirrror.navigator.hideBottomNamedArgument

object ChatDestination : NavigationDestination {

    override fun route(): String = CHAT_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(CHAT_ID_PARAM) { type = NavType.LongType },
            hideBottomNamedArgument
        )

    const val CHAT_ID_PARAM = "chat_id"

    private const val CHAT_ROUTE = "chat"
    private const val CHAT_BOTTOM_NAV_ROUTE = "$CHAT_ROUTE/{$CHAT_ID_PARAM}"

    fun createChatRoute(chatId: Long = 0) = "$CHAT_ROUTE/{$chatId}"
}
