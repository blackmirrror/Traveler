package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import ru.blackmirrror.navigator.NavigationDestination
import ru.blackmirrror.navigator.hideBottomNamedArgument

object ChatDestination : NavigationDestination {

    override fun route(): String = CHAT_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            hideBottomNamedArgument
        )

    private const val CHAT_ROUTE = "chat"
    private const val CHAT_BOTTOM_NAV_ROUTE = CHAT_ROUTE

    fun createAccountEditRoute() = CHAT_ROUTE
}