package ru.blackmirrror.chats.presentation.chats

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.blackmirrror.chats.domain.Chat
import ru.blackmirrror.component.R
import ru.blackmirrror.component.screen.UnauthorizedScreen
import ru.blackmirrror.component.ui.TextFieldOneLine
import ru.blackmirrror.core.exception.NoAuthorized
import ru.blackmirrror.core.state.ScreenState

@Composable
fun ChatsScreen() {

    val vm: ChatsScreenViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    when (state) {
        is ScreenState.Loading -> {}
        is ScreenState.Success -> ChatsContent(
            state = state as ScreenState.Success,
            onIntent = { vm.processEvent(it) }
        )
        is ScreenState.Error -> {
            val error = (state as ScreenState.Error).error
            when (error) {
                is NoAuthorized -> UnauthorizedScreen {
                    vm.processEvent(ChatsEvent.ToAuth)
                }
            }
        }
    }
}

@Composable
fun ChatsContent(
    state: ScreenState<List<Chat>>,
    onIntent: (ChatsEvent) -> Unit
) {

    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        Text(
            text = stringResource(R.string.chats_title_chats),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        TextFieldOneLine(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = stringResource(R.string.chats_hint_search),
            isSearch = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.data?.size ?: 0) { position ->
                ChatItem(
                    state.data!![position],
                    onIntent
                )
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    onIntent: (ChatsEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onIntent(ChatsEvent.ToChat) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Image(
//            painter = painterResource(id = ChatsR.drawable.ic_face),
//            contentDescription = "Avatar",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(50.dp)
//                .clip(CircleShape)
//        )
        AsyncImage(
            model = chat.avatar,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
//                .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = chat.time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
