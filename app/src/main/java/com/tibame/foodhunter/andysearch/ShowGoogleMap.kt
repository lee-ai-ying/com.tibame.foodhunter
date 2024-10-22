package com.tibame.foodhunter.andysearch

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowGoogleMap(modifier : Modifier){
    Column (
        modifier = modifier
    ) {

        var target = LatLng(25.092713, 121.54)
        val cameraPositionState = rememberCameraPositionState {
            // 移動地圖到指定位置
            this.position = CameraPosition.fromLatLngZoom(target, 15f)
        }

        var searchText by remember { mutableStateOf("") }
        val locationPermission = rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val currentLocation by remember { mutableStateOf<LatLng?>(null) }
        var newPosition by remember { mutableStateOf<LatLng?>(null) }

        LaunchedEffect(Unit) {
            if (!locationPermission.status.isGranted) {
                locationPermission.launchPermissionRequest()
            }
        }
        var selectedPOI by remember { mutableStateOf<Pair<String, LatLng>?>(null) }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onPOIClick = { poi ->
                selectedPOI = poi.name to poi.latLng
            },
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),

            properties = MapProperties(
                isMyLocationEnabled = locationPermission.status.isGranted,
                latLngBoundsForCameraTarget = LatLngBounds(
                    LatLng(22.045858, 119.426224),
                    LatLng(25.161124, 122.343094)
                ),
                // 設定地圖種類：NORMAL(一般圖，預設)、HYBRID(混合圖)、SATELLITE(衛星圖)、TERRAIN(地形圖)
                mapType = MapType.NORMAL,
                // 設定放大上限
                maxZoomPreference = 20f,
                // 設定縮小下限
                minZoomPreference = 5f
            )
        ) {

            currentLocation?.let { location ->
                Circle(
                    center = location,
                    radius = 1000.0,
                    strokeColor = Color.Red,
                    strokeWidth = 2f,
                    fillColor = Color(0x220000FF)
                )
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
fun GoogleMapPreview() {
    ShowGoogleMap(Modifier.fillMaxWidth().fillMaxHeight(.4f).padding(16.dp))
}