package ru.blackmirrror.map.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.blackmirrror.component.R
import ru.blackmirrror.map.domain.Category

@Composable
fun DragHandleBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .size(width = 50.dp, height = 4.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Composable
fun TypeSelection(selectedTypes: Set<Category>, onTypeSelected: (Category) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Category.entries.forEach {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onTypeSelected(it) }
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (selectedTypes.contains(it)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(it.painterId), contentDescription = it.title, tint = Color.White)
                }
                Text(it.title, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun RatingStars(rating: Int, onRatingChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        (1..5).forEach { index ->
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = "Рейтинг $index",
                tint = if (index <= rating) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(32.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .clickable { onRatingChange(index) }
            )
        }
    }
}
