package ru.blackmirrror.bottom_navigation

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.blackmirrror.navigator.hideBottomNavigation
import ru.blackmirrror.style.shape.BottomSheetShapes

@SuppressLint("UnrememberedMutableState")
@Composable
fun TravelerBottomNavigation(
    navController: NavHostController
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val hideBottomNav by derivedStateOf { navBackStackEntry.hideBottomNavigation }

    val size = if (hideBottomNav) {
        Modifier.size(animateDpAsState(targetValue = 0.dp, animationSpec = tween()).value)
    } else {
        Modifier
    }

    NavigationBar(
        modifier = size
            .clip(BottomSheetShapes.large)
            .height(110.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        bottomNavigationEntries.forEach { bottomEntry ->
            NavigationBarItem(
                selected = currentRoute == bottomEntry.screen.route,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(bottomEntry.screen.route) {
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = bottomEntry.icon,
                        contentDescription = null
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = Color.Transparent
                ),
            )
        }
    }
}
