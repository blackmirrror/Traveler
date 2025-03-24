package ru.blackmirrror.account.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.blackmirrror.account.presentation.viewModel.EditAccountViewModel
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.TextFieldCustom
import ru.blackmirrror.component.ui.TextFieldWithEdit
import ru.blackmirrror.component.ui.TextFieldWithMask
import ru.blackmirrror.destinations.AuthPhoneEmailDestination
import ru.blackmirrror.account.R as AccountR

@Composable
fun EditAccountScreen() {

    val vm: EditAccountViewModel = hiltViewModel()

    var firstName by rememberSaveable { mutableStateOf("Анастасия") }
    var lastName by rememberSaveable { mutableStateOf("Иванова") }
    var phoneNumber by rememberSaveable { mutableStateOf("+7 (900) 999-99-99") }
    var email by rememberSaveable { mutableStateOf("anastasiaivanova@mail.ru") }
    var birthDate by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        vm.popBackStack()
                    }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.account_edit_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.size(120.dp)
        ) {
            Image(
                painter = painterResource(id = AccountR.drawable.ic_face),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape)
            )
            IconButton(
                onClick = { /* Change photo logic */ },
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextFieldCustom(
            value = firstName,
            onValueChange = { firstName = it },
            label = stringResource(R.string.account_edit_hint_first_name),
            enabled = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldCustom(
            value = lastName,
            onValueChange = { lastName = it },
            label = stringResource(R.string.account_edit_hint_last_name),
            enabled = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldWithEdit(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = stringResource(R.string.account_edit_hint_phone),
            onEditClick = {
                vm.navigate(
                    AuthPhoneEmailDestination.createAuthEnterOtpRoute(
                        data = "79536443782",
                        isPhone = true
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldWithEdit(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.account_edit_hint_email),
            onEditClick = {
                vm.navigate(
                    AuthPhoneEmailDestination.createAuthEnterOtpRoute(
                        data = "ekfk@gmail.com",
                        isPhone = false
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldWithMask(
            data = birthDate,
            label = stringResource(R.string.account_edit_hint_birth_date),
            mask = "00.00.0000",
            maskNumber = '0',
            onDataChanged = { birthDate = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { vm.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.account_edit_btn_save),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}
