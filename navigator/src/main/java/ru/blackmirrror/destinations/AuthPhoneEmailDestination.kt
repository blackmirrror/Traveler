package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.blackmirrror.navigator.NavigationDestination
import ru.blackmirrror.navigator.hideBottomNamedArgument

object AuthPhoneEmailDestination : NavigationDestination {

    override fun route(): String = AUTH_PHONE_EMAIL_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(DATA_PARAM) { type = NavType.StringType },
            navArgument(IS_PHONE_PARAM) { type = NavType.BoolType },
            hideBottomNamedArgument
        )

    const val DATA_PARAM = "data"
    const val IS_PHONE_PARAM = "is_phone"

    private const val AUTH_PHONE_EMAIL_ROUTE = "auth_phone_email"
    private const val AUTH_PHONE_EMAIL_BOTTOM_NAV_ROUTE = "$AUTH_PHONE_EMAIL_ROUTE/{$DATA_PARAM}&{$IS_PHONE_PARAM}"

    fun createAuthEnterOtpRoute(data: String, isPhone: Boolean) = "$AUTH_PHONE_EMAIL_ROUTE/${data.lowercase()}&${isPhone}"

}