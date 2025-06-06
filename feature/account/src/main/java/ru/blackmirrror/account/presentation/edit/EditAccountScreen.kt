package ru.blackmirrror.account.presentation.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.blackmirrror.account.domain.model.User
import ru.blackmirrror.account.presentation.dateStringToLong
import ru.blackmirrror.account.presentation.longToDateString
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.TextFieldOneLine
import ru.blackmirrror.component.ui.TextFieldWithEdit
import ru.blackmirrror.component.ui.TextFieldWithMask
import ru.blackmirrror.core.uriToFile
import ru.blackmirrror.destinations.AuthPhoneEmailDestination

@Composable
fun EditAccountScreen() {

    val vm: EditAccountViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    var firstName by rememberSaveable { mutableStateOf(state.data?.firstName ?: "") }
    var lastName by rememberSaveable { mutableStateOf(state.data?.lastName ?: "") }
    var phoneNumber by rememberSaveable { mutableStateOf(state.data?.phone ?: "") }
    var email by rememberSaveable { mutableStateOf(state.data?.email ?: "") }
    var birthDate by rememberSaveable { mutableStateOf(longToDateString(state.data?.birthDate)) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val file = uriToFile(context, it)
            if (file != null) {
                vm.processEvent(EditAccountEvent.EditPhoto(file))
            }
        }
    }

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
                        vm.processEvent(EditAccountEvent.Back)
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
            AsyncImage(
                model = state.data?.photoUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { launcher.launch("image/*") },
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

        TextFieldOneLine(
            value = firstName,
            onValueChange = { firstName = it },
            label = stringResource(R.string.account_edit_hint_first_name),
            enabled = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextFieldOneLine(
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
                    AuthPhoneEmailDestination.createAuthPhoneEmailRoute(
                        data = phoneNumber,
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
                    AuthPhoneEmailDestination.createAuthPhoneEmailRoute(
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
            onClick = {
                vm.processEvent(EditAccountEvent.SaveUser(
                User(
                    phone = "",
                    firstName = firstName,
                    lastName = lastName,
                    birthDate = dateStringToLong(birthDate)
                )
            )) },
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
