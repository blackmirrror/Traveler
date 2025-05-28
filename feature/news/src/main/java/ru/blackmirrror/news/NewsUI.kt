package ru.blackmirrror.news

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.blackmirrror.component.R
import ru.blackmirrror.component.ui.TextFieldCustom
import ru.blackmirrror.news.R as NewsR

@Composable
fun News() {
    HikingScreen()
}

@Composable
fun HikingScreen() {

    val news = rememberSaveable { getSampleNews() }

    var searchQuery by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = stringResource(R.string.news_title_news),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )

        TextFieldCustom(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = stringResource(R.string.chats_hint_sarch),
            isSearch = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(news.size) { position ->
                NewsItem(news[position])
            }
        }

//        FilterButtons()
//        Spacer(modifier = Modifier.height(8.dp))
//        HikingTrailCard()
    }
}

@Composable
fun NewsItem(news: News) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Image(
//                painter = news.avatar,
//                contentDescription = "Avatar",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape)
//            )
            AsyncImage(
                model = news.avatar,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
//                    .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = news.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 0.dp),
                    lineHeight = 10.sp
                )
                Text(
                    text = "${news.lat}, ${news.long}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 0.dp),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                modifier = Modifier
                    .clickable { /* Действие при нажатии */ },
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "Подписаться",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "Some",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Image(
            painter = if (news.time == "12:23") painterResource(NewsR.drawable.i) else painterResource(NewsR.drawable.novosib),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
            //.size(40.dp)
        )

        Text(
            text = news.title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = news.description,
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
            Text(text = news.time, fontSize = 12.sp, modifier = Modifier.padding(end = 16.dp))
        }
    }
}


data class News(
    val avatar: String,
    val name: String,
    val lat: String,
    val long: String,
    val unreadCount: Int,
    val time: String = "12:23",
    val title: String = "Самое лучшее место всем советую",
    val description: String = "Ездили вчера очень понравилось, обязательно посетите. Все останутся под большим впечатлением. Большой склон, вкусная кафешка при курортном городке"
)

fun getSampleNews(): List<News> {
    return listOf(
        News(
            "https://randomuser.me/api/portraits/men/67.jpg",
            "Max Vargin",
            "44.9243434",
            "42.062257",
            20,
            time = "12:23",
            title = "Гора бударка",
            description = "Много подъездов, надо осторожнее выбирать в плохую погоду. Чуть не застряли. А место очень красивое"
        ),
        News(
            "https://randomuser.me/api/portraits/women/11.jpg",
            "Bulka bulochka",
            "45.2343434",
            "45.2343434",
            0,
            time = "Вчера",
            title = "Новосибирское водохранилище",
            description = "Шикарное место"
        ),
        News(
            "https://randomuser.me/api/portraits/women/3.jpg",
            "Мария Посадова",
            "45.2343434",
            "45.2343434",
            1
        ),
        News(
            "https://randomuser.me/api/portraits/men/4.jpg",
            "Алексей Венгерский",
            "45.2343434",
            "45.2343434",
            0
        )
    )
}
