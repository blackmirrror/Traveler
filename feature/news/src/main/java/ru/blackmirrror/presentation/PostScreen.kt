package ru.blackmirrror.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.TextFieldOneLine
import ru.blackmirrror.data.PostDto

@Composable
fun PostScreen() {
    PostContent()
}

@Composable
fun PostContent() {

    val vm: PostScreenViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = stringResource(R.string.news_title_news),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
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
                state.data?.get(position)?.let { NewsItem(it) }
            }
        }
    }
}

@Composable
fun NewsItem(
    post: PostDto
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = post.author?.photoUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "${post.author?.firstName} ${post.author?.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 0.dp),
                    lineHeight = 10.sp
                )
                Text(
                    text = "${post.latitude}, ${post.longitude}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 0.dp),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

//            Surface(
//                modifier = Modifier
//                    .clickable { /* Действие при нажатии */ },
//                shape = RoundedCornerShape(4.dp),
//                color = MaterialTheme.colorScheme.primaryContainer
//            ) {
//                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
//                    Text(
//                        text = "Подписаться",
//                        fontSize = 12.sp,
//                        color = MaterialTheme.colorScheme.onBackground
//                    )
//                }
//            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Some",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        AsyncImage(
            model = post.imageUrl,
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Text(
            text = post.title ?: "",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = post.description ?: "",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            maxLines = 2,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Some",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Some",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Some",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = post.dateCreate.toString(), fontSize = 12.sp, modifier = Modifier.padding(end = 16.dp))
        }
    }
}
