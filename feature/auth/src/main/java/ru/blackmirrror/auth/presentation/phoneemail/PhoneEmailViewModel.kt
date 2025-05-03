package ru.blackmirrror.auth.presentation.phoneemail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.blackmirrror.auth.domain.AuthRepository
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.destinations.AuthEnterOtpDestination
import ru.blackmirrror.destinations.AuthEnterOtpDestination.DATA_OTP_PARAM
import ru.blackmirrror.destinations.AuthEnterOtpDestination.IS_PHONE_OTP_PARAM
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
        val isPhone = savedStateHandle.get<Boolean>(IS_PHONE_OTP_PARAM) ?: true
        val data = savedStateHandle.get<String>(DATA_OTP_PARAM)?.lowercase()
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
            val result = authRepository.sendPhoneOtp(data)
            if (result.isSuccess) {
                navigate(
                    AuthEnterOtpDestination.createAuthEnterOtpRoute(
                        // todo nullable
                        data = data,
                        isPhone = _state.value.data?.isPhone!!
                    )
                )
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
}