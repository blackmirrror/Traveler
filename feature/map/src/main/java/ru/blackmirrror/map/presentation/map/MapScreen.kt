package ru.blackmirrror.map.presentation.map

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import ru.blackmirrror.component.loading.LoadingUI
import ru.blackmirrror.core.state.ScreenState
import ru.blackmirrror.map.data.MarkLatLngDto
import ru.blackmirrror.map.presentation.LocationPermissionScreen
import ru.blackmirrror.map.utils.checkForPermission
import ru.blackmirrror.map.utils.getCurrentLocation

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
    state: ScreenState<List<MarkLatLngDto>>,
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
    state: ScreenState<List<MarkLatLngDto>>,
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
            properties = mapProperties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
            onMapClick = { latLng ->
                onIntent(MapEvent.ToCreateMark(latLng.latitude, latLng.longitude))
            }
        ) {
            Marker(
                state = MarkerState(position = latLng),
//                title = "Current location",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
            state.data?.forEach { mark ->
                Marker(
                    state = MarkerState(position = LatLng(mark.lat, mark.lon)),
                    title = "Location",
                    snippet = "Marker in current location",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                    onClick = {
                        onIntent(MapEvent.ToShowMark(mark.id))
                        true
                    }
                )
            }
        }

        IconButton(
            onClick = { onIntent(MapEvent.ToSearchFilters(latLng.latitude, latLng.longitude)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = Color.White
            )
        }

    }
}
