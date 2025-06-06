package ru.blackmirrror.chats.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.chats.data.ChatWebSocketRepository
import ru.blackmirrror.chats.data.SocketEvent
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.destinations.ChatDestination.CHAT_ID_PARAM
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val chatsRepository: ChatsRepository,
    private val chatWebSocketRepository: ChatWebSocketRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ResultState<ChatUiState>>(ResultState.Loading())
    val state: StateFlow<ResultState<ChatUiState>> = _state

    private val chatId = savedStateHandle.get<Long>(CHAT_ID_PARAM) ?: 0

    init {
        getMessages()
        observeMessages()
    }

    private fun getMessages() {
        viewModelScope.launch {
            chatsRepository.getMessages(chatId).collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val currentUiState = _state.value.data ?: ChatUiState(
                            messages = result.data,
                            userId = getUserId()
                        )
                        _state.value = ResultState.Success(
                            currentUiState.copy(
                                messages = result.data
                            )
                        )
                    }
                    is ResultState.Error -> {
                        _state.value = ResultState.Error(result.error)
                    }
                    is ResultState.Loading -> {
                        _state.value = ResultState.Loading()
                    }
                }
            }
        }
    }

    private fun observeMessages() {
        chatWebSocketRepository.connect()
        viewModelScope.launch {
            chatWebSocketRepository.messageFlow.collect { result ->
                when (result) {
                    is ResultState.Success -> {
                        val currentUiState = _state.value.data ?: ChatUiState(
                            socketEvent = result.data,
                            messages = emptyList(),
                            userId = getUserId()
                        )
                        _state.value = ResultState.Success(
                            currentUiState.copy(
                                socketEvent = result.data
                            )
                        )
                    }
                    is ResultState.Error -> {
                        _state.value = ResultState.Error(result.error)
                    }
                    is ResultState.Loading -> {
                        _state.value = ResultState.Loading()
                    }
                }
            }
        }
    }

    fun processEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> sendMessage(event.text)
            is ChatEvent.Back -> back()
            is ChatEvent.AddMessage -> TODO()
        }
    }

    private fun sendMessage(text: String) {
        val event = SocketEvent.SendMessage(chatId, text)
        chatWebSocketRepository.sendMessage(event)
    }

    fun getUserId(): Long {
        return chatsRepository.getUserId()
    }

    override fun onCleared() {
        chatWebSocketRepository.disconnect()
        super.onCleared()
    }

    private fun back() {
        travelerNavigator.sendResult(NavigatorResult.ChatUpdated)
        travelerNavigator.popBackStack()
    }
}
