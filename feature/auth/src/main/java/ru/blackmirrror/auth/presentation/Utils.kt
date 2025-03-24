package ru.blackmirrror.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.blackmirrror.component.R

@Composable
internal fun getPhoneEmailTitle(data: String, isPhone: Boolean): String {
    return if (data.isEmpty()) {
        if (isPhone)
            stringResource(R.string.auth_title_phone)
        else
            stringResource(R.string.auth_title_email)
    } else {
        if (isPhone)
            stringResource(R.string.auth_title_phone_edit)
        else
            stringResource(R.string.auth_title_email_edit)
    }
}

@Composable
internal fun getPhoneEmailDes(isPhone: Boolean): String {
    return if (isPhone)
        stringResource(R.string.auth_des_phone)
    else
        stringResource(R.string.auth_des_email)
}