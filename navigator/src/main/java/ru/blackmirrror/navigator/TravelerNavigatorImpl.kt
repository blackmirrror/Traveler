package ru.blackmirrror.navigator

import android.util.Log
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TravelerNavigatorImpl @Inject constructor() : TravelerNavigator {

    private val navigationEvents = Channel<NavigatorEvent>()
    override val destinations = navigationEvents.receiveAsFlow()

    private val _results = MutableSharedFlow<NavigatorResult>(replay = 64)
    override val results: Flow<NavigatorResult> = _results.asSharedFlow()

    override fun sendResult(result: NavigatorResult) {
        _results.tryEmit(result)
    }
    override fun navigateUp(): Boolean = navigationEvents.trySend(NavigatorEvent.NavigateUp).isSuccess

    override fun popBackStack() {
        navigationEvents.trySend(NavigatorEvent.PopBackStack)
    }

    override fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit): Boolean = navigationEvents.trySend(
        NavigatorEvent.Directions(route, builder)
    ).isSuccess

    override fun navigateToMain() {
        navigationEvents.trySend(NavigatorEvent.NavigateToMain)
    }
}
