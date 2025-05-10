package ru.blackmirrror.account.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.blackmirrror.account.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun dateStringToLong(dateStr: String): Long {
    if (dateStr.isEmpty())
        return 0L
    val formatter = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    val date = formatter.parse(dateStr)
    return date?.time ?: 0L
}

fun longToDateString(timestamp: Long?): String {
    if (timestamp == null || timestamp == 0L)
        return ""
    val formatter = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
