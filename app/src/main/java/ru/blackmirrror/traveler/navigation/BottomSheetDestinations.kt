package ru.blackmirrror.traveler.navigation

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.blackmirrror.destinations.SearchFilterDestination
import ru.blackmirrror.map.presentation.SearchFilterScreen
import ru.blackmirrror.navigator.NavigationDestination

private val bottomSheetDestinations: Map<NavigationDestination, @Composable (NavBackStackEntry, NavHostController) -> Unit>
    get() = mapOf(
        SearchFilterDestination to { _, _ -> SearchFilterScreen() }
    )

fun NavGraphBuilder.addBottomSheetDestinations(navController: NavHostController) {
    bottomSheetDestinations.forEach { entry ->
        val destination = entry.key
        bottomSheet(destination.route(), destination.arguments, destination.deepLinks) { navEntry ->
            entry.value(navEntry, navController)
        }
    }
}
