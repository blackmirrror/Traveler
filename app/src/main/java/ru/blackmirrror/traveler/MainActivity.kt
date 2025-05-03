package ru.blackmirrror.traveler


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.navigation.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import ru.blackmirrror.bottom_navigation.AccountRoute
import ru.blackmirrror.bottom_navigation.BottomNavigationEntry.Companion.ACCOUNT
import ru.blackmirrror.bottom_navigation.TravelerBottomNavigation
import ru.blackmirrror.bottom_navigation.MapRoute
import ru.blackmirrror.map.ui.Map
import ru.blackmirrror.navigator.TravelerNavigator
import ru.blackmirrror.navigator.NavigatorEvent
import ru.blackmirrror.style.shape.BottomSheetShapes
import ru.blackmirrror.style.ui.TravelerTheme
import ru.blackmirrror.traveler.navigation.addBottomNavigationDestinations
import ru.blackmirrror.traveler.navigation.addBottomSheetDestinations
import ru.blackmirrror.traveler.navigation.addComposableDestinations
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var travelerNavigator: TravelerNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TravelerTheme {
                Surface {
                    TravelerScaffold(travelerNavigator)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Swoe() {
    MaterialTheme {
        Map()
    }
}



@Composable
fun TravelerScaffold(travelerNavigator: TravelerNavigator) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController()
    navController.navigatorProvider += bottomSheetNavigator

    LaunchedEffect(navController) {
        travelerNavigator.destinations.collect {
            when (val event = it) {
                is NavigatorEvent.NavigateUp -> {
                    navController.navigateUp()
                }
                is NavigatorEvent.Directions -> {
                    navController.navigate(
                        event.destination,
                        event.builder
                    )
                }
                is NavigatorEvent.PopBackStack -> {
                    navController.popBackStack()
                }
                is NavigatorEvent.NavigateToMain -> {
                    navController.navigate(ACCOUNT) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = BottomSheetShapes.medium,
    ) {
        Scaffold(
            bottomBar = {
                TravelerBottomNavigation(navController)
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = MapRoute.route,
                enterTransition = { fadeIn(animationSpec = tween(200)) },
                exitTransition = { fadeOut(animationSpec = tween(200)) },
            ) {
                addBottomNavigationDestinations(navController)
                addBottomSheetDestinations(navController)
                addComposableDestinations(navController)
            }
        }
    }
}