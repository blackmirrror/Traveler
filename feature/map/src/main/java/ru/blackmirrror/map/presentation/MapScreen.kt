package ru.blackmirrror.map.presentation

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ru.blackmirrror.component.loading.LoadingUI
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.map.domain.model.Mark
import ru.blackmirrror.map.utils.checkForPermission
import ru.blackmirrror.map.utils.getCurrentLocation
import ru.blackmirrror.map.utils.toListLatLng

@Composable
fun Map() {
    val vm: MapScreenViewModel = hiltViewModel()
    val state by vm.state.collectAsState()

    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(checkForPermission(context))
    }

    if (!hasLocationPermission) {
        LocationPermissionScreen {
            hasLocationPermission = true
        }
    }

    when (state) {
        is ScreenState.Loading -> {}
        is ScreenState.Success -> MapContent(
            state = state as ScreenState.Success,
            onIntent = { vm.processEvent(it) },
            context
        )
        is ScreenState.Error -> {
            MapContent(
                state = state as ScreenState.Error,
                onIntent = { vm.processEvent(it) },
                context
            )
        }
    }
}

@Composable
fun MapContent(
    state: ScreenState<List<Mark>>,
    onIntent: (MapEvent) -> Unit,
    context: Context
) {

    var showMap by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val mapProperties by remember { mutableStateOf(MapProperties()) }

    getCurrentLocation(context) {
        location = it
        showMap = true
    }

    if (showMap) {
        LoadMap(
            state = state,
            onIntent = onIntent,
            latLng = location,
            mapProperties = mapProperties
        )
    } else {
        LoadingUI()
    }
}

@Composable
fun LoadMap(
    state: ScreenState<List<Mark>>,
    onIntent: (MapEvent) -> Unit,
    latLng: LatLng,
    mapProperties: MapProperties = MapProperties(),
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties
        ) {
            Marker(
                state = MarkerState(position = latLng),
                title = "Current location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
            state.data.toListLatLng()?.forEach {
                Marker(
                    state = MarkerState(position = it),
                    title = "Location",
                    snippet = "Marker in current location",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                    onClick = {
                        onIntent(MapEvent.ToSearchFilters)
                        true
                    }
                )
            }
        }
    }
}