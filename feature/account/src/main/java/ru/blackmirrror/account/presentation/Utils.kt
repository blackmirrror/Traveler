package ru.blackmirrror.account.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.blackmirrror.account.R
import java.util.Date

@Composable
fun getAccountUserName(firstName: String?, lastName: String?): String {
    return if (firstName == null && lastName == null)
        stringResource(R.string.unknown_quest)
    else
        "${firstName?:""} ${lastName?:""}"
}

fun formatPhoneNumber(phoneNumber: String): String {
    return "+7 (${phoneNumber.substring(0, 3)}) ${phoneNumber.substring(3, 6)}-${phoneNumber.substring(6, 8)}"
}

fun Long.toDate(): Date? {
    if (this == 0L)
        return null
    return Date(this)
}
