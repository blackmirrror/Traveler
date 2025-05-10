package ru.blackmirrror.account.presentation.account

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
import ru.blackmirrror.core.exception.NoAuthorized
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.AccountEditDestination
import ru.blackmirrror.destinations.AuthPhoneEmailDestination
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class AccountScreenViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
    private val accountRepository: AccountRepository
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private val _state = MutableStateFlow<ScreenState<User>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<User>> = _state.asStateFlow()

    init {
        processEvent(AccountEvent.LoadAccount)
    }

    fun processEvent(intent: AccountEvent) {
        when (intent) {
            is AccountEvent.LoadAccount -> loadAccount()
            is AccountEvent.Logout -> logout()
            is AccountEvent.DeleteAccount -> deleteAccount()
            is AccountEvent.EditAccount -> editAccount()
            is AccountEvent.ToAuth -> toAuth()
            is AccountEvent.HideSnackbar -> hideSnackbar()
        }
    }

    private fun loadAccount() {
        if (!accountRepository.isAuthenticated()) {
            _state.value = ScreenState.Error(NoAuthorized)
            return
        }
        viewModelScope.launch {
            accountRepository.getUser().collect { u ->
                when (u) {
                    is ResultState.Loading -> _state.value = ScreenState.Loading(u.data)
                    is ResultState.Success -> _state.value = ScreenState.Success(u.data)
                    is ResultState.Error -> _state.value = ScreenState.Error(u.error, u.data)
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            val result = accountRepository.logout()
            if (result.isSuccess) {
                AuthPhoneEmailDestination.createAuthEnterOtpRoute(
                    data = NULL_DATA_STRING,
                    isPhone = true
                )
                _state.value = ScreenState.Error(NoAuthorized)
            }
            else {
                when (result.exceptionOrNull()) {
                    is NoInternet -> {
                        _state.value = ScreenState.Error(
                            data = _state.value.data,
                            error = NoInternet
                        )
                    }
                }
            }
        }
    }

    private fun deleteAccount() {}

    private fun editAccount() {
        if (accountRepository.isInternetConnection()) {
            navigate(AccountEditDestination.createAccountEditRoute())
        }
        else {
            _state.value = ScreenState.Error(
                data = _state.value.data,
                error = NoInternet
            )
        }

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
