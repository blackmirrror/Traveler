package ru.blackmirrror.account.presentation.edit

import ru.blackmirrror.account.domain.model.User
import java.io.File

sealed class EditAccountEvent {

    object LoadEdit: EditAccountEvent()

    object EditPhone : EditAccountEvent()

    object EditEmail : EditAccountEvent()

    data class EditPhoto(val file: File) : EditAccountEvent()

    data class SaveUser(val user: User) : EditAccountEvent()

    object Back : EditAccountEvent()
}
