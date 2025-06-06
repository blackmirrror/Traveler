package ru.blackmirrror.auth.presentation.otp

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
import ru.blackmirrror.destinations.AuthEnterOtpDestination.DATA_OTP_PARAM
import ru.blackmirrror.destinations.AuthEnterOtpDestination.IS_PHONE_OTP_PARAM
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
    private val travelerNavigator: TravelerNavigator,
) : ViewModel(), TravelerNavigator by travelerNavigator {

    private var _state = MutableStateFlow<ScreenState<OtpUiModel>>(ScreenState.Loading())
    val state: StateFlow<ScreenState<OtpUiModel>> = _state.asStateFlow()

    init {
        val data = savedStateHandle.get<String>(DATA_OTP_PARAM)?.lowercase()!!
        val isPhone = savedStateHandle.get<Boolean>(IS_PHONE_OTP_PARAM)!!

        _state.value = ScreenState.Success(
            data = OtpUiModel(
                data = data,
                isPhone = isPhone
            )
        )
    }

    fun processEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.VerifyOtp -> verifyOtp(event.code)
            is OtpEvent.Back -> travelerNavigator.popBackStack()
        }
    }

    private fun verifyOtp(code: String) {
        viewModelScope.launch {
            authRepository.verifyPhoneOtp(code).collect { result ->
                when (result) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        // todo refactor
                        popBackStack()
                    }
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
