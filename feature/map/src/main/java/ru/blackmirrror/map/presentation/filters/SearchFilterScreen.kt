package ru.blackmirrror.map.presentation.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.blackmirrror.component.R
import ru.blackmirrror.map.domain.Category
import ru.blackmirrror.map.presentation.DragHandleBar
import ru.blackmirrror.map.presentation.RatingStars
import ru.blackmirrror.map.presentation.TypeSelection
import ru.blackmirrror.component.R as CommonR

@Composable
fun SearchFilterScreen() {

    val vm: FilterViewModel = hiltViewModel()

    var selectedCategories by remember { mutableStateOf(setOf(Category.CAMPING)) }
    var radius by remember { mutableFloatStateOf(100f) }
    var minRating by remember { mutableIntStateOf(3) }

    FiltersContent(
        selectedTypes = selectedCategories,
        onTypeSelected = { type ->
            selectedCategories = if (selectedCategories.contains(type)) {
                selectedCategories - type
            } else {
                selectedCategories + type
            }
        },
        radius = radius,
        onRadiusChange = { radius = it },
        rating = minRating,
        onRatingChange = { minRating = it },
        onApplyFilters = { vm.processEvent(FilterEvent.ApplyFilters(
            categories = selectedCategories.toList(),
            radius = radius.toDouble(),
            minRating = minRating.toDouble()
        )) }
    )
}

@Composable
fun FiltersContent(
    selectedTypes: Set<Category>,
    onTypeSelected: (Category) -> Unit,
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
        DragHandleBar()

        Spacer(modifier = Modifier.height(8.dp))

        Text(stringResource(R.string.map_filters_title), fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)

        Text(stringResource(CommonR.string.map_filters_the_categories_of_places), fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)

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
fun RadiusSlider(radius: Float, onRadiusChange: (Float) -> Unit) {
    Slider(
        value = radius,
        onValueChange = onRadiusChange,
        valueRange = 50f..500f,
        steps = 9
    )
}
