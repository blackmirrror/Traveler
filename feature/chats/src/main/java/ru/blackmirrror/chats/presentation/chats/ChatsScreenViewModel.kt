package ru.blackmirrror.chats.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.chats.data.ChatDto
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.NULL_DATA_STRING
import ru.blackmirrror.core.exception.NoAuthorized
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.destinations.AuthPhoneEmailDestination
import ru.blackmirrror.destinations.ChatDestination
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class ChatsScreenViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val chatsRepository: ChatsRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ResultState<List<ChatDto>>>(ResultState.Loading())
    val state: StateFlow<ResultState<List<ChatDto>>> = _state.asStateFlow()

    init {
        observeNavigationResults()
        processEvent(ChatsEvent.LoadChats)
    }

    private fun observeNavigationResults() {
        viewModelScope.launch {
            results.collect { result ->
                when (result) {
                    is NavigatorResult.ChatUpdated -> loadChats()
                    else -> Unit
                }
            }
        }
    }

    fun processEvent(event: ChatsEvent) {
        when (event) {
            is ChatsEvent.LoadChats -> loadChats()
            is ChatsEvent.ToChat -> toChat(event.chatId)
            is ChatsEvent.ToAuth -> toAuth()
            is ChatsEvent.HideSnackbar -> hideSnackbar()
        }
    }

    private fun loadChats() {
        if (!chatsRepository.isAuthenticated()) {
            _state.value = ResultState.Error(NoAuthorized)
            return
        }
        viewModelScope.launch {
            chatsRepository.getChats().collect { result ->
                when (result) {
                    is ResultState.Success -> _state.value = ResultState.Success(result.data)
                    else -> Unit
                }
            }
        }
    }

    private fun toChat(chatId: Long) {
        navigate(ChatDestination.createChatRoute(chatId))
    }

    private fun toAuth() {
        navigate(
            AuthPhoneEmailDestination.createAuthPhoneEmailRoute(
                data = NULL_DATA_STRING,
                isPhone = true
            )
        )
    }

    private fun hideSnackbar() {
        _state.value = ResultState.Success(
            data = _state.value.data!!
        )
    }
}
