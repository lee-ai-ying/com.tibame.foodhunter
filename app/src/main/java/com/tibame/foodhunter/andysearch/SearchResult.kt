package com.tibame.foodhunter.andysearch

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
    restaurantID: String = "",
    searchTextVM: SearchScreenVM = viewModel()
){
    val context = (LocalContext.current)
    val cities = parseCityJson(context, "taiwan_districts.json")
    val restaurants by searchTextVM.preRestaurantList.collectAsState()
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    Column(modifier = Modifier.fillMaxSize()){
        ShowSearchBar(cities, searchTextVM, navController)
        ShowGoogleMap(
            restaurants = restaurants,
            restaurantID = restaurantID,
            onLocationUpdate = {location -> currentLocation = location})
        ShowRestaurantLists(restaurants, false, navController, currentLocation, searchTextVM)
    }
}


