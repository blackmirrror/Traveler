package ru.blackmirrror.account.presentation.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.account.domain.AccountRepository
import ru.blackmirrror.account.domain.model.User
import ru.blackmirrror.core.NULL_DATA_STRING
import ru.blackmirrror.core.image_storage.FileRepository
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.AuthPhoneEmailDestination
import ru.blackmirrror.navigator.NavigatorResult
import ru.blackmirrror.navigator.TravelerNavigator
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val accountRepository: AccountRepository,
    private val fileRepository: FileRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<User>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<User>> = _state.asStateFlow()

    init {
        processEvent(EditAccountEvent.LoadEdit)
    }

    fun processEvent(event: EditAccountEvent) {
        when (event) {
            is EditAccountEvent.LoadEdit -> loadEditAccount()
            is EditAccountEvent.EditPhone-> editPhone()
            is EditAccountEvent.EditEmail -> editEmail()
            is EditAccountEvent.EditPhoto -> editPhoto(event.file)
            is EditAccountEvent.SaveUser -> saveUser(event.user)
            is EditAccountEvent.Back -> popBackStack()
        }
    }

    private fun loadEditAccount() {
        viewModelScope.launch {
            accountRepository.getUser().collect { user ->
                when (user) {
                    is ResultState.Loading -> _state.value = ScreenState.Loading(user.data)
                    is ResultState.Success -> _state.value = ScreenState.Success(user.data)
                    is ResultState.Error -> _state.value = ScreenState.Error(user.error, user.data)
                }
            }
        }
    }

    private fun editPhone() {
        navigate(
            AuthPhoneEmailDestination.createAuthPhoneEmailRoute(
                data = state.value.data?.phone ?: NULL_DATA_STRING,
                isPhone = true
            )
        )
    }

    private fun editEmail() {
        navigate(
            AuthPhoneEmailDestination.createAuthPhoneEmailRoute(
                data = state.value.data?.email ?: NULL_DATA_STRING,
                isPhone = false
            )
        )
    }

    private fun editPhoto(file: File) {
        uploadImage(file)
    }

    private fun saveUser(user: User) {
        viewModelScope.launch {
            accountRepository.updateUser(user).collect { res ->
                when (res) {
                    is ResultState.Success -> {
                        travelerNavigator.sendResult(NavigatorResult.UserUpdated)
                        travelerNavigator.popBackStack()
                    }
                    is ResultState.Loading -> {}
                    is ResultState.Error -> {}
                }
            }
        }
    }

    private fun uploadImage(file: File) {
        viewModelScope.launch {
            fileRepository.uploadImage(file).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        Log.d("DDD", "uploadImage: l ${result.data}")
                    }
                    is ResultState.Success -> {
                        val imageUrl = fileRepository.getImageUrl(file.name)
                        saveUser(_state.value.data!!.copy(photoUrl = imageUrl))
                        Log.d("DDD", "uploadImage: s ${result.data}")
                    }
                    is ResultState.Error -> {
                        Log.d("DDD", "uploadImage: e ${result.error}")
                    }
                }
            }
        }
    }
}
