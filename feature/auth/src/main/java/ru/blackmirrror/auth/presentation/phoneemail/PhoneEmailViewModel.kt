package ru.blackmirrror.auth.presentation.phoneemail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.state.ResultState
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.AuthEnterOtpDestination
import ru.blackmirrror.destinations.AuthPhoneEmailDestination.DATA_PARAM
import ru.blackmirrror.destinations.AuthPhoneEmailDestination.IS_PHONE_PARAM
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class PhoneEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
    private val travelerNavigator: TravelerNavigator,
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private var _state = MutableStateFlow<ScreenState<PhoneEmailUiModel>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<PhoneEmailUiModel>> = _state.asStateFlow()

    init {
        val isPhone = savedStateHandle.get<Boolean>(IS_PHONE_PARAM) ?: true
        val data = savedStateHandle.get<String>(DATA_PARAM)?.lowercase()
        _state.value = ScreenState.Success(data = PhoneEmailUiModel(
            data = data,
            isPhone = isPhone
        ))
    }

    fun processEvent(event: PhoneEmailEvent) {
        when (event) {
            is PhoneEmailEvent.LoadPhoneEmail -> loadPhoneEmail()
            is PhoneEmailEvent.SendCode -> sendOtp(event.data)
            is PhoneEmailEvent.Back -> popBackStack()
        }
    }

    private fun loadPhoneEmail() {

    }

    private fun sendOtp(data: String) {
        viewModelScope.launch {
            authRepository.sendPhoneOtp(data).collect { result ->
                when (result) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> navigate(
                        AuthEnterOtpDestination.createAuthEnterOtpRoute(
                            data = data,
                            isPhone = _state.value.data?.isPhone!!
                        )
                    )
                    is ResultState.Error -> {
                        _state.value = ScreenState.Error(
                            data = _state.value.data,
                            error = result.error
                        )
                    }
                }
            }
        }
    }
}
