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
    return "+7 (${phoneNumber.substring(2, 5)}) ${phoneNumber.substring(5, 8)}-${phoneNumber.substring(8, 10)}-${phoneNumber.substring(10, 12)}"
}

fun Long.toDate(): Date? {
    if (this == 0L)
        return null
    return Date(this)
}
