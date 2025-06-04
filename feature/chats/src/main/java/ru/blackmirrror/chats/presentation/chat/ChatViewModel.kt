package ru.blackmirrror.chats.presentation.chat

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.blackmirrror.chats.domain.ChatsRepository
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val chatsRepository: ChatsRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    fun back() {
        travelerNavigator.sendResult(
            NavigatorResult.UpdMess
        )
        travelerNavigator.popBackStack()
    }
}