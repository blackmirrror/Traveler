package ru.blackmirrror.auth.presentation.phoneemail

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.PopupHost
import ru.blackmirrror.component.ui.TextFieldCustom
import ru.blackmirrror.component.ui.TextFieldWithMask
import ru.blackmirrror.core.NULL_DATA_STRING
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.state.ScreenState

@Composable
fun PhoneEmailScreen() {

    val vm: PhoneEmailViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    var popupMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val errorStr = stringResource(R.string.snackbar_no_internet)

    LaunchedEffect(state) {
        if (state is ScreenState.Error) {
            when ((state as ScreenState.Error<PhoneEmailUiModel>).error) {
                is NoInternet -> popupMessage = errorStr
            }
        }
    }

    when (state) {
        is ScreenState.Loading -> {}
        is ScreenState.Success -> PhoneEmailContent(
            state = state as ScreenState.Success,
            onIntent = { vm.processEvent(it) }
        )
        is ScreenState.Error -> PhoneEmailContent(
            state = state as ScreenState.Error,
            onIntent = { vm.processEvent(it) }
        )
    }

    PopupHost(
        message = popupMessage,
        onDismiss = { popupMessage = null }
    )
}

@Composable
fun PhoneEmailContent(state: ScreenState<PhoneEmailUiModel>, onIntent: (PhoneEmailEvent) -> Unit) {
    var data by rememberSaveable { mutableStateOf(NULL_DATA_STRING) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getPhoneEmailTitle(state.data?.data ?: NULL_DATA_STRING, state.data?.isPhone ?: true),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = getPhoneEmailDes(state.data?.isPhone ?: true),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.data?.isPhone != false) {
            TextFieldWithMask(
                data = data,
                label = stringResource(R.string.auth_hint_phone),
                mask = "+7 (000) 000-00-00",
                maskNumber = '0',
                onDataChanged = { data = it }
            )
        } else {
            TextFieldCustom(
                value = data,
                onValueChange = { data = it },
                label = stringResource(R.string.auth_hint_email)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onIntent(PhoneEmailEvent.SendCode("+7$data"))
            },
            enabled = !state.data?.isPhone!! || data.length == 10,
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
