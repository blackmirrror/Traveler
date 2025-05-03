package ru.blackmirrror.auth.presentation.otp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.blackmirrror.auth.presentation.phoneemail.PhoneEmailContent
import ru.blackmirrror.auth.presentation.phoneemail.PhoneEmailEvent
import ru.blackmirrror.auth.presentation.phoneemail.PhoneEmailUiModel
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.PopupHost
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.state.ScreenState

@Composable
fun OtpVerificationScreen() {

    val vm: OtpVerificationViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    var popupMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val errorStr = stringResource(R.string.snackbar_no_internet)

    LaunchedEffect(state) {
        if (state is ScreenState.Error) {
            when ((state as ScreenState.Error<OtpUiModel>).error) {
                is NoInternet -> popupMessage = errorStr
            }
        }
    }

    when (state) {
        is ScreenState.Loading -> {}
        is ScreenState.Success -> OtpContent(
            state = state as ScreenState.Success,
            onIntent = { vm.processEvent(it) }
        )

        is ScreenState.Error -> OtpContent(
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
fun OtpContent(
    state: ScreenState<OtpUiModel>,
    onIntent: (OtpEvent) -> Unit
) {
    var otpValue by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.auth_title_code),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text =
            """
                ${stringResource(if (state.data?.isPhone!!) R.string.auth_des_code_phone else R.string.auth_des_code_email)}
                ${state.data?.data!!}
            """.trimIndent(),

            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(24.dp))

        OtpTextField(
            otpText = otpValue,
            onOtpTextChange = { value, otpInputFilled ->
                otpValue = value
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onIntent(OtpEvent.VerifyOtp(otpValue))
            },
            enabled = !state.data?.isPhone!! || otpValue.length == 6,
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
            text = stringResource(R.string.auth_btn_send_otp_again),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { /* Повторная отправка кода */ }
        )
    }
}

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int = 6,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpCount) {
            throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
        }
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
        onValueChange = {
            if (it.text.length <= otpCount) {
                onOtpTextChange.invoke(it.text, it.text.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}

@Composable
private fun CharView(
    index: Int,
    text: String
) {
    val isFocused = text.length == index
    val char = when {
        index == text.length -> ""
        index > text.length -> ""
        else -> text[index].toString()
    }
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            fontSize = 24.sp,
            color = if (isFocused) Color.Gray else Color.Black,
            textAlign = TextAlign.Center
        )
    }
}
