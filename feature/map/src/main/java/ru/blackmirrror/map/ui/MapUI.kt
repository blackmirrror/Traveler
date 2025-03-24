package ru.blackmirrror.map.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ru.blackmirrror.component.loading.LoadingUI
import ru.blackmirrror.map.utils.checkForPermission
import ru.blackmirrror.map.utils.getCurrentLocation
import ru.blackmirrror.navigator.TravelerNavigatorViewModel

@Composable
fun Map() {
    val navigatorViewModel: TravelerNavigatorViewModel = hiltViewModel()
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(checkForPermission(context))
    }

    if (hasLocationPermission) {
        MapScreen(context, navigatorViewModel)
    } else {
        LocationPermissionScreen {
            hasLocationPermission = true
        }
    }
}

@Composable
fun MapScreen(context: Context, navigatorViewModel: TravelerNavigatorViewModel) {
    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val mapProperties by remember { mutableStateOf(MapProperties()) }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        LoadMap(
            latLng = location,
            mapProperties = mapProperties,
            navigatorViewModel
        )
    } else {
        LoadingUI()
    }
}

@Composable
fun LoadMap(
    latLng: LatLng,
    mapProperties: MapProperties = MapProperties(),
    navigatorViewModel: TravelerNavigatorViewModel
) {
    val latlangList = remember {
        mutableStateListOf(latLng)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties
        ) {
            latlangList.toList().forEach {
                Marker(
                    state = MarkerState(position = it),
                    title = "Location",
                    snippet = "Marker in current location",
                    icon = null,
                    onClick = {
                        navigatorViewModel.navigate(SearchFilterDestination.route())
                    }
                )
            }
        }
    }
}