package ru.blackmirrror.auth.presentation.otp

sealed class OtpEvent {
    data class VerifyOtp(val code: String): OtpEvent()
    object Back: OtpEvent()
}
