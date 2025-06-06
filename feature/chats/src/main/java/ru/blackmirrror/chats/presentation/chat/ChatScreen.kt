package ru.blackmirrror.chats.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.blackmirrror.chats.data.MessageDto
import ru.blackmirrror.chats.data.SocketEvent
import ru.blackmirrror.component.R
import ru.blackmirrror.core.state.ResultState

@Composable
fun ChatScreen() {
    val vm: ChatViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    when (state) {
        is ResultState.Success -> {
            val event = state.data?.socketEvent
            when (event) {
                is SocketEvent.NewMessage -> {
                    vm.processEvent(ChatEvent.AddMessage(
                        text = event.text,
                        chatId = event.chatId,
                        senderId = event.senderId
                    ))
                }
                is SocketEvent.Typing -> {
                    status = "typing"
                }
                is SocketEvent.MessageRead -> {
                    status = "online"
                }
                is SocketEvent.SendMessage -> {}
                null -> Unit
            }
        }
        else -> Unit
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { vm.processEvent(ChatEvent.Back) },
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Отправить", tint = MaterialTheme.colorScheme.onBackground)
            }
            AsyncImage(
                model = "https://randomuser.me/api/portraits/women/17.jpg",
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Анастасия", fontWeight = FontWeight.Bold)
                Text(status, fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true,
        ) {
            val messages = state.data?.messages
            if (messages != null) {
                items(messages.size) { idx ->
                    val message = messages[messages.size - idx - 1]
                    MessageBubble(message, message.senderId == vm.getUserId())
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

        }

        Spacer(Modifier.height(8.dp))

        Divider()

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                placeholder = { Text(stringResource(R.string.chats_hint_message)) },
                singleLine = true,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        vm.processEvent(ChatEvent.SendMessage(inputText.trim()))
                    }
                },
            ) {
                Icon(Icons.Default.Send, contentDescription = "Отправить", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun MessageBubble(message: MessageDto, isFromCurrentUser: Boolean) {
    val alignment = if (isFromCurrentUser) Alignment.End else Alignment.Start
    val bubbleColor = if (isFromCurrentUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primaryContainer
    val textColor = if (isFromCurrentUser) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(message.text, color = textColor)
        }
    }
}
