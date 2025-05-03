package ru.blackmirrror.account.presentation.edit

sealed class EditAccountEvent {
    object LoadEdit: EditAccountEvent()
    object EditPhone : EditAccountEvent()
    object EditEmail : EditAccountEvent()
    object EditPhoto : EditAccountEvent()
    object SaveUser : EditAccountEvent()
    object Back : EditAccountEvent()
}