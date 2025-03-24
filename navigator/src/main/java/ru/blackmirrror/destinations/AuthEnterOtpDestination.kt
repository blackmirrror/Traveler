package ru.blackmirrror.destinations

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.blackmirrror.navigator.NavigationDestination
import ru.blackmirrror.navigator.hideBottomNamedArgument

object AuthEnterOtpDestination : NavigationDestination {

    override fun route(): String = AUTH_ENTER_OTP_BOTTOM_NAV_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(DATA_OTP_PARAM) { type = NavType.StringType },
            navArgument(IS_PHONE_OTP_PARAM) { type = NavType.BoolType },
            hideBottomNamedArgument
        )

    const val DATA_OTP_PARAM = "data"
    const val IS_PHONE_OTP_PARAM = "is_phone"

    private const val AUTH_ENTER_OTP_ROUTE = "auth_enter_otp"
    private const val AUTH_ENTER_OTP_BOTTOM_NAV_ROUTE = "$AUTH_ENTER_OTP_ROUTE/{$DATA_OTP_PARAM}&{$IS_PHONE_OTP_PARAM}"

    fun createAuthEnterOtpRoute(data: String, isPhone: Boolean) = "$AUTH_ENTER_OTP_ROUTE/${data.lowercase()}&${isPhone}"

}