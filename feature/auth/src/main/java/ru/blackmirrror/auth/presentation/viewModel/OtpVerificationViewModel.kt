package ru.blackmirrror.auth.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.blackmirrror.destinations.AuthEnterOtpDestination.DATA_OTP_PARAM
import ru.blackmirrror.destinations.AuthEnterOtpDestination.IS_PHONE_OTP_PARAM
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class OtpVerificationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    application: Application,
    private val travelerNavigator: TravelerNavigator,
) : AndroidViewModel(application), TravelerNavigator by travelerNavigator {
    val data
        get() = savedStateHandle.get<String>(DATA_OTP_PARAM)?.lowercase()
            ?: throw IllegalStateException("Parameter data must not be null!")

    val isPhone
        get() = savedStateHandle.get<Boolean>(IS_PHONE_OTP_PARAM)
            ?: throw IllegalStateException("Parameter isPhone must not be null!")
}