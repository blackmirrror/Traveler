package ru.blackmirrror.navigator

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface TravelerNavigator {

    val destinations: Flow<NavigatorEvent>
    val results: Flow<NavigatorResult>

    fun sendResult(result: NavigatorResult)

    fun navigateUp(): Boolean

    fun popBackStack()

    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }): Boolean

    fun navigateToMain()
}
