package ru.blackmirrror.map.presentation.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.map.data.MarkDto
import ru.blackmirrror.map.presentation.DragHandleBar

@Composable
fun MapShowMarkScreen() {

    val vm: ShowMarkViewModel = hiltViewModel()
    val state by vm.state.collectAsState()
    MapShowMarkContent(
        state
    )
}

@Composable
fun MapShowMarkContent(
    state: ScreenState<MarkDto>
    ) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        DragHandleBar()

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            state.data?.title ?: "",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${state.data?.latitude} ${state.data?.longitude}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f)
            )
            Text(state.data?.averageRating.toString(), fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }

        Spacer(modifier = Modifier.height(8.dp))

        state.data?.imageUrl?.let { ImagePickerCard(imageUrl = it) }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                state.data?.author?.firstName ?: "Unknown",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Text(
                state.data?.dateCreate.toString(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "Пляж",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "Шашлык",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun ImagePickerCard(
    imageUrl: String
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
