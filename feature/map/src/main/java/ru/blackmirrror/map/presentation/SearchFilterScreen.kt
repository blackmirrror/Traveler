package ru.blackmirrror.map.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.blackmirrror.component.R as CommonR

@Composable
fun SearchFilterScreen() {

    var selectedTypes by remember { mutableStateOf(setOf("Кемпинг")) }
    var radius by remember { mutableStateOf(100f) }
    var rating by remember { mutableStateOf(3) }

    FiltersContent(
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

@Composable
fun FiltersContent(
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
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(stringResource(CommonR.string.map_filters_the_types_of_places), fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(16.dp))

        TypeSelection(selectedTypes, onTypeSelected)

        Spacer(modifier = Modifier.height(16.dp))

        Text("${stringResource(CommonR.string.map_filters_search_radius)}: ${radius.toInt()} ${stringResource(CommonR.string.map_filters_km)}", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        RadiusSlider(radius, onRadiusChange)

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(CommonR.string.map_filters_minimum_rating), fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

        RatingStars(rating, onRatingChange)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onApplyFilters() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(CommonR.string.map_filters_btn_apply), fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun TypeSelection(selectedTypes: Set<String>, onTypeSelected: (String) -> Unit) {
    val types = listOf(
        "Кемпинг" to painterResource(CommonR.drawable.ic_camping),
        "Рыбалка" to painterResource(CommonR.drawable.ic_fishing),
        "Поход" to painterResource(CommonR.drawable.ic_hike),
        "Пляж" to painterResource(CommonR.drawable.ic_beach),
        "Байдарки" to painterResource(CommonR.drawable.ic_kayak),
        "Шашлык" to painterResource(CommonR.drawable.ic_barbecue),
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
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(if (selectedTypes.contains(name)) MaterialTheme.colorScheme.primary else Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = icon, contentDescription = name, tint = Color.White)
                }
                Text(name, fontSize = 12.sp)
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
                painter = painterResource(CommonR.drawable.ic_star),
                contentDescription = "Рейтинг $index",
                tint = if (index <= rating) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onPrimaryContainer,
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

    FiltersContent(
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