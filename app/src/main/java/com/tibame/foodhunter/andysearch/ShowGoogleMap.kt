package com.tibame.foodhunter.andysearch

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowGoogleMap(
    modifier : Modifier = Modifier.fillMaxWidth().fillMaxHeight(.8f).padding(16.dp),
    restaurants: List<Restaurant>,
    restaurantVM: SearchScreenVM,
    onLocationUpdate: (LatLng?) -> Unit
){

    val choiceOneRest by restaurantVM.choiceOneRest.collectAsState()
    // 預設位子 - 台北車站
    val defaultPosition = LatLng(25.04776, 121.517059)


    val cameraPositionState = rememberCameraPositionState {
        // 移動地圖到指定位置
        this.position = CameraPosition.fromLatLngZoom(defaultPosition, 15f)
    }
    val locationPermission = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var newPosition by remember { mutableStateOf<LatLng?>(null) }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)

    Column (
        modifier = modifier
    ) {
        LaunchedEffect(Unit) {
            if (!locationPermission.status.isGranted) {
                locationPermission.launchPermissionRequest()
            }
        }

        LaunchedEffect(locationPermission.status.isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener{
                location -> currentLocation = LatLng(location.latitude, location.longitude)
                onLocationUpdate(currentLocation)
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

            // 定位的圖案
            currentLocation?.let { location ->
                Circle(
                    center = location,
                    radius = 1000.0,
                    strokeColor = Color.Red,
                    strokeWidth = 2f,
                    fillColor = Color(0x220000FF)
                )
            }

            // 如果有選餐廳的話就單一標點
            if (choiceOneRest != null){
                newPosition = LatLng(
                    choiceOneRest!!.latitude,
                    choiceOneRest!!.longitude)

                Marker(
                    state = MarkerState(position = newPosition ?: defaultPosition),
                    title = choiceOneRest!!.name,
                    snippet = choiceOneRest!!.address
                )
            } else {
                restaurants.forEach {
                    restaurant ->
                    Marker(
                        state = MarkerState(position = LatLng(restaurant.latitude
                            , restaurant.longitude)
                        ),
                        title = restaurant.name,
                        snippet = restaurant.address
                    )
                }
            }
        }

        LaunchedEffect(newPosition) {
            newPosition?.let {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(it, 10f)
                )
            }
        }
    }
}




fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
    // 將經緯度從度數轉換為弧度
    val earthRadiusKM = 6371.0 // 地球半徑
    val lat1Rad = Math.toRadians(lat1)
    val lon1Rad = Math.toRadians(lon1)
    val lat2Rad = Math.toRadians(lat2)
    val lon2Rad = Math.toRadians(lon2)

    // 緯度和經度的差值
    val dLat = lat2Rad - lat1Rad
    val dLon = lon2Rad - lon1Rad

    // Haversine公式
    val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))


    val distance = round(earthRadiusKM * c * 10.0) / 10.0
    return distance.toString()

}


@Preview(showBackground = true)
@Composable
fun GoogleMapPreview() {
//    ShowGoogleMap(Modifier.fillMaxWidth().fillMaxHeight(.4f).padding(16.dp))
}