package com.tibame.foodhunter.andysearch

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng

@Composable
fun SearchResult(
    navController: NavHostController,
    restaurantID: Int = -1,
    searchTextVM: SearchScreenVM
){
    Log.d("restaurant_id", restaurantID.toString())
    val context = (LocalContext.current)
    val cities = parseCityJson(context, "taiwan_districts.json")
    val selectRest by searchTextVM.selectRestList.collectAsState()
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    if (restaurantID != -1) {
        Column(modifier = Modifier.fillMaxSize()){
            ShowSearchBar(cities, searchTextVM, navController)
            ShowGoogleMap(
                restaurants = selectRest,
                restaurantID = restaurantID,
                onLocationUpdate = {location -> currentLocation = location})
            ShowRestaurantLists(selectRest, false, navController, currentLocation, searchTextVM)
        }
    } else {
//        Log.d("selectRest",selectRest[0].toString())
        Column(modifier = Modifier.fillMaxSize()){
            ShowSearchBar(cities, searchTextVM, navController)
            ShowGoogleMap(
                restaurants = selectRest,
                onLocationUpdate = {location -> currentLocation = location})
            ShowRestaurantLists(selectRest, false, navController, currentLocation, searchTextVM)
        }
    }

}


