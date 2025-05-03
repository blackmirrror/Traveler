package ru.blackmirrror.account.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.account.domain.AccountRepository
import ru.blackmirrror.account.domain.model.NoData
import ru.blackmirrror.account.domain.model.User
import ru.blackmirrror.core.NULL_DATA_STRING
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.AuthPhoneEmailDestination
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val accountRepository: AccountRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<User>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<User>> = _state.asStateFlow()

    init {
        processEvent(EditAccountEvent.LoadEdit)
    }

    fun processEvent(intent: EditAccountEvent) {
        when (intent) {
            is EditAccountEvent.LoadEdit -> loadEditAccount()
            is EditAccountEvent.EditPhone-> editPhone()
            is EditAccountEvent.EditEmail -> editEmail()
            is EditAccountEvent.EditPhoto -> editPhoto()
            is EditAccountEvent.SaveUser -> saveUser()
            is EditAccountEvent.Back -> popBackStack()
        }
    }

    private fun loadEditAccount() {
        viewModelScope.launch {
            try {
                val user = accountRepository.getUser()
                _state.value = ScreenState.Success(user)
            } catch (e: Exception) {
                _state.value = ScreenState.Error(NoData)
            }
        }
    }

    private fun editPhone() {
        navigate(
            AuthPhoneEmailDestination.createAuthEnterOtpRoute(
                data = state.value.data?.phone ?: NULL_DATA_STRING,
                isPhone = true
            )
        )
    }

    private fun editEmail() {
        navigate(
            AuthPhoneEmailDestination.createAuthEnterOtpRoute(
                data = state.value.data?.email ?: NULL_DATA_STRING,
                isPhone = false
            )
        )
    }

    private fun editPhoto() {

    }

    private fun saveUser() {
        viewModelScope.launch {
            accountRepository.updateUser()
        }
    }
}