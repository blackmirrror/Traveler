package ru.blackmirrror.chats.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.chats.domain.Chat
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.NULL_DATA_STRING
import ru.blackmirrror.core.exception.NoAuthorized
import ru.blackmirrror.core.exception.NoData
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.AuthPhoneEmailDestination
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class ChatsScreenViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val chatsRepository: ChatsRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<List<Chat>>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<List<Chat>>> = _state.asStateFlow()

    init {
        processEvent(ChatsEvent.LoadChats)
    }

    fun processEvent(intent: ChatsEvent) {
        when (intent) {
            is ChatsEvent.LoadChats -> loadChats()
            is ChatsEvent.ToChat -> toChat()
            is ChatsEvent.ToAuth -> toAuth()
            is ChatsEvent.HideSnackbar -> hideSnackbar()
        }
    }

    private fun loadChats() {
        if (!chatsRepository.isAuthenticated()) {
            _state.value = ScreenState.Error(NoAuthorized)
            return
        }
        viewModelScope.launch {
            try {
                val chats = chatsRepository.getChats()
                _state.value = ScreenState.Success(chats)
            } catch (e: Exception) {
                _state.value = ScreenState.Error(NoData)
            }
        }
    }

    private fun toChat() {
//        viewModelScope.launch {
//            val result = chatsRepository.logout()
//            if (result.isSuccess) {
//                AuthPhoneEmailDestination.createAuthEnterOtpRoute(
//                    data = NULL_DATA_STRING,
//                    isPhone = true
//                )
//                _state.value = ScreenState.Error(NoAuthorized)
//            }
//            else {
//                when (result.exceptionOrNull()) {
//                    is NoInternet -> {
//                        _state.value = ScreenState.Error(
//                            data = _state.value.data,
//                            error = NoInternet
//                        )
//                    }
//                }
//            }
//        }
    }

    private fun toAuth() {
        navigate(
            AuthPhoneEmailDestination.createAuthEnterOtpRoute(
                data = NULL_DATA_STRING,
                isPhone = true
            )
        )
    }

    private fun hideSnackbar() {
        _state.value = ScreenState.Success(
            data = _state.value.data!!
        )
    }
}