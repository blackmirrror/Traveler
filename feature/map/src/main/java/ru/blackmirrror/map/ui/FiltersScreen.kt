package ru.blackmirrror.map.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.filled.Email

@Composable
fun FiltersScreen(
    selectedTypes: Set<String>,
    onTypeSelected: (String) -> Unit,
    radius: Float,
    onRadiusChange: (Float) -> Unit,
    rating: Int,
    onRatingChange: (Int) -> Unit,
    onApplyFilters: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Выберите типы мест", fontSize = 18.sp, color = Color.Black)

        TypeSelection(selectedTypes, onTypeSelected)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Радиус поиска: ${radius.toInt()} км", fontSize = 18.sp, color = Color.Black)

        RadiusSlider(radius, onRadiusChange)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Минимальный рейтинг", fontSize = 18.sp, color = Color.Black)

        RatingStars(rating, onRatingChange)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onApplyFilters() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Применить фильтры", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun TypeSelection(selectedTypes: Set<String>, onTypeSelected: (String) -> Unit) {
    val types = listOf(
        "Кемпинг" to Icons.Default.Email,
        "Рыбалка" to Icons.Default.Email,
        "Поход" to Icons.Default.Email,
        "Пляж" to Icons.Default.Email
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        types.forEach { (name, icon) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onTypeSelected(name) }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (selectedTypes.contains(name)) Color.Blue else Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = name, tint = Color.White)
                }
                Text(name, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun RadiusSlider(radius: Float, onRadiusChange: (Float) -> Unit) {
    Slider(
        value = radius,
        onValueChange = onRadiusChange,
        valueRange = 50f..500f,
        steps = 9
    )
}

@Composable
fun RatingStars(rating: Int, onRatingChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        (1..5).forEach { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Рейтинг $index",
                tint = if (index <= rating) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChange(index) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFilters() {
    var selectedTypes by remember { mutableStateOf(setOf("Кемпинг")) }
    var radius by remember { mutableStateOf(100f) }
    var rating by remember { mutableStateOf(3) }

    FiltersScreen(
        selectedTypes = selectedTypes,
        onTypeSelected = { type ->
            selectedTypes = if (selectedTypes.contains(type)) {
                selectedTypes - type
            } else {
                selectedTypes + type
            }
        },
        radius = radius,
        onRadiusChange = { radius = it },
        rating = rating,
        onRatingChange = { rating = it },
        onApplyFilters = { /* Здесь можно обработать результат */ }
    )
}

