package ru.blackmirrror.auth.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.blackmirrror.auth.presentation.getPhoneEmailDes
import ru.blackmirrror.auth.presentation.getPhoneEmailTitle
import ru.blackmirrror.auth.presentation.viewModel.PhoneEmailViewModel
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.TextFieldCustom
import ru.blackmirrror.component.ui.TextFieldWithMask
import ru.blackmirrror.destinations.AuthEnterOtpDestination

@Composable
fun PhoneEmailScreen() {

    val vm: PhoneEmailViewModel = hiltViewModel()

    var data by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getPhoneEmailTitle(vm.data, vm.isPhone),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = getPhoneEmailDes(vm.isPhone),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (vm.isPhone) {
            TextFieldWithMask(
                data = data,
                label = stringResource(R.string.auth_hint_phone),
                mask = "+7 (000) 000-00-00",
                maskNumber = '0',
                onDataChanged = { data = it }
            )
        }

        else {
            TextFieldCustom(
                value = data,
                onValueChange = { data = it },
                label = stringResource(R.string.auth_hint_email)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vm.navigate(
                    AuthEnterOtpDestination.createAuthEnterOtpRoute(
                        data = data,
                        isPhone = vm.isPhone
                    )
                )
            },
            enabled = !vm.isPhone || data.length == 10,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.auth_btn_send_otp),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.background
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.auth_des_registration),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = stringResource(R.string.auth_des_policy),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { /* Открыть политику */ }
        )
    }
}
