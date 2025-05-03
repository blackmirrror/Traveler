package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import ru.blackmirrror.navigator.NavigationDestination
import ru.blackmirrror.navigator.hideBottomNamedArgument

object AccountEditDestination : NavigationDestination {

    override fun route(): String = ACCOUNT_EDIT_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            hideBottomNamedArgument
        )

    private const val ACCOUNT_EDIT_ROUTE = "account_edit"
    private const val ACCOUNT_EDIT_BOTTOM_NAV_ROUTE = ACCOUNT_EDIT_ROUTE

    fun createAccountEditRoute() = ACCOUNT_EDIT_ROUTE
}
