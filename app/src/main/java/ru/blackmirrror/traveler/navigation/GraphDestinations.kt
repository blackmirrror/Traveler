package ru.blackmirrror.traveler.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.blackmirrror.account.presentation.edit.EditAccountScreen
import ru.blackmirrror.auth.presentation.otp.OtpVerificationScreen
import ru.blackmirrror.auth.presentation.phoneemail.PhoneEmailScreen
import ru.blackmirrror.chats.presentation.chat.ChatScreen
import ru.blackmirrror.destinations.AccountEditDestination
import ru.blackmirrror.destinations.AuthEnterOtpDestination
import ru.blackmirrror.destinations.AuthPhoneEmailDestination
import ru.blackmirrror.destinations.ChatDestination
import ru.blackmirrror.navigator.NavigationDestination

private val composableDestinations: Map<NavigationDestination, @Composable () -> Unit> = mapOf(
    AuthEnterOtpDestination to { OtpVerificationScreen() },
    AuthPhoneEmailDestination to { PhoneEmailScreen() },
    AccountEditDestination to { EditAccountScreen() },
    ChatDestination to { ChatScreen()}
)

fun NavGraphBuilder.addComposableDestinations(navController: NavHostController) {
    composableDestinations.forEach { entry ->
        val destination = entry.key
        composable(destination.route(), destination.arguments, destination.deepLinks) {
            entry.value()
        }
    }
}
