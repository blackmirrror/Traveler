package ru.blackmirrror.map.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SearchFilterUI() {
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