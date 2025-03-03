package ru.blackmirrror.navigator

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface TravelerNavigator {

    fun navigateUp(): Boolean
    fun popBackStack()
    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }): Boolean
    val destinations: Flow<NavigatorEvent>
}