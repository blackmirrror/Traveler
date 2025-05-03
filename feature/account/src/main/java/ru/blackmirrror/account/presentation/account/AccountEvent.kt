package ru.blackmirrror.account.presentation.account

sealed class AccountEvent {
    object LoadAccount : AccountEvent()
    object Logout : AccountEvent()
    object DeleteAccount : AccountEvent()
    object EditAccount : AccountEvent()
    object ToAuth: AccountEvent()
}
