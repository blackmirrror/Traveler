package ru.blackmirrror.traveler.navigation

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.blackmirrror.destinations.MapCreateMarkDestination
import ru.blackmirrror.destinations.MapShowMarkDestination
import ru.blackmirrror.destinations.SearchFilterDestination
import ru.blackmirrror.map.presentation.create.MapCreateMarkScreen
import ru.blackmirrror.map.presentation.filters.SearchFilterScreen
import ru.blackmirrror.map.presentation.show.MapShowMarkScreen
import ru.blackmirrror.navigator.NavigationDestination

private val bottomSheetDestinations: Map<NavigationDestination, @Composable (NavBackStackEntry, NavHostController) -> Unit>
    get() = mapOf(
        SearchFilterDestination to { _, _ -> SearchFilterScreen() },
        MapCreateMarkDestination to {_, _ -> MapCreateMarkScreen() },
        MapShowMarkDestination to {_, _ -> MapShowMarkScreen()}
    )

fun NavGraphBuilder.addBottomSheetDestinations(navController: NavHostController) {
    bottomSheetDestinations.forEach { entry ->
        val destination = entry.key
        bottomSheet(destination.route(), destination.arguments, destination.deepLinks) { navEntry ->
            entry.value(navEntry, navController)
        }
    }
}
