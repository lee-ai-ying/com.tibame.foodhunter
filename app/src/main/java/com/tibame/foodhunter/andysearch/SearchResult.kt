package com.tibame.foodhunter.andysearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchResult(){
    val context = (LocalContext.current)
    val cities = parseCityJson(context, "taiwan_districts.json")
    val restaurants = parseRestaurantJson(context, "restaurants.json")
    Column(modifier = Modifier.fillMaxSize()){
        ShowSearchBar(cities)
        ShowGoogleMap(modifier = Modifier.fillMaxWidth().fillMaxHeight(.8f).padding(16.dp))
        ShowRestaurantLists(restaurants, false)
    }
}


@Preview(showBackground = true)
@Composable
fun SearchResultPreview() {
    SearchResult()
}