package ru.blackmirrror.account.presentation.account

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.blackmirrror.account.domain.model.User
import ru.blackmirrror.account.presentation.formatPhoneNumber
import ru.blackmirrror.account.presentation.getAccountUserName
import ru.blackmirrror.component.R
import ru.blackmirrror.component.screen.UnauthorizedScreen
import ru.blackmirrror.component.ui.PopupHost
import ru.blackmirrror.core.exception.NoAuthorized
import ru.blackmirrror.core.exception.NoInternet
import ru.blackmirrror.core.state.ScreenState

@Composable
fun Account() {

    val vm: AccountScreenViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    var popupMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val errorStr = stringResource(R.string.snackbar_no_internet)

    LaunchedEffect(state) {
        if (state is ScreenState.Error) {
            when ((state as ScreenState.Error<User>).error) {
                is NoInternet -> popupMessage = errorStr
            }
        }
    }

    when (state) {
        is ScreenState.Loading -> {}
        is ScreenState.Success -> AccountContent(
            state = state as ScreenState.Success,
            onIntent = { vm.processEvent(it) }
        )
        is ScreenState.Error -> {
            val error = (state as ScreenState.Error).error
            when (error) {
                is NoAuthorized -> UnauthorizedScreen {
                    vm.processEvent(AccountEvent.ToAuth)
                }
                is NoInternet -> AccountContent(
                    state = state as ScreenState.Error,
                    onIntent = { vm.processEvent(it) }
                )
            }
        }
    }

    PopupHost(
        message = popupMessage,
        onDismiss = {
            popupMessage = null
            vm.processEvent(AccountEvent.HideSnackbar)
        }
    )
}

@Composable
fun AccountContent(state: ScreenState<User>, onIntent: (AccountEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CitySection()
        Spacer(modifier = Modifier.height(16.dp))
        HeaderSection(
            state = state,
            onClick = { onIntent(AccountEvent.EditAccount) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (state.data != null && showSurveySection(state.data!!)) {
            SurveySection(
                onClick = { onIntent(AccountEvent.EditAccount) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        SettingsSection()
        Spacer(modifier = Modifier.height(16.dp))
        ActionButtons(
            onLogout = { onIntent(AccountEvent.Logout) }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CitySection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = "Location",
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "Москва",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "31 Мая, 2025",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun HeaderSection(
    state: ScreenState<User>,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = state.data?.photoUrl,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = getAccountUserName(state.data?.firstName, state.data?.lastName),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = formatPhoneNumber(state.data?.phone!!),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "EditProfile",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SurveySection(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.account_title_fill_profile),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.account_des_fill_profile),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onClick() },
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.account_btn_more),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

@Composable
fun SettingsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            listOf(
                stringResource(R.string.account_btn_favorite) to Icons.Outlined.FavoriteBorder,
                stringResource(R.string.account_btn_notifications) to Icons.Outlined.Notifications,
                stringResource(R.string.account_btn_theme) to Icons.Outlined.Info,
                stringResource(R.string.account_btn_language) to Icons.Outlined.Place,
                stringResource(R.string.account_btn_support) to Icons.Outlined.Build,
                stringResource(R.string.account_btn_police) to Icons.Outlined.DateRange,
                stringResource(R.string.account_btn_about) to Icons.Outlined.Info
            ).forEach { (title, icon) ->
                SettingsItem(title, icon)
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Обработчик нажатия */ }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun ActionButtons(
    onLogout: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.account_btn_invite),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Действие */ }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                ActionItem(
                    title = stringResource(R.string.account_btn_logout),
                    icon = Icons.Outlined.ExitToApp,
                    textColor = MaterialTheme.colorScheme.error,
                    onClick = onLogout
                )
                ActionItem(
                    title = stringResource(R.string.account_btn_delete_account),
                    icon = Icons.Outlined.Delete,
                    textColor = MaterialTheme.colorScheme.error,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun ActionItem(
    title: String,
    icon: ImageVector,
    textColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = textColor)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontSize = 16.sp, color = textColor)
    }
}

fun showSurveySection(user: User): Boolean {
    return user.firstName == null || user.email == null
}
