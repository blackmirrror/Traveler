package ru.blackmirrror.auth.presentation.phoneemail

sealed class PhoneEmailEvent {
    object LoadPhoneEmail: PhoneEmailEvent()
    data class SendCode(val data: String) : PhoneEmailEvent()
    object Back : PhoneEmailEvent()
}
