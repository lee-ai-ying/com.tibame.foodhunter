package com.tibame.foodhunter.andysearch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.tibame.foodhunter.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController
){

    val context = LocalContext.current
    val restaurants = parseRestaurantJson(context, "restaurants.json")
    val cities = remember { parseCityJson(context, "taiwan_districts.json") }

    Column(modifier = Modifier.fillMaxSize()){


        ShowSearchBar(cities)
        ShowGoogleMap(Modifier.fillMaxWidth().fillMaxHeight(.4f).padding(16.dp))

        ShowRestaurantLists(restaurants, true)
    }

}




@Composable
fun CityLists(
    cities: List<City>
) {
    var selectedCity by remember { mutableStateOf<String?>(null) }
    LazyColumn(
        modifier = Modifier
            .padding(start = 36.dp)
            .height(200.dp)
    ) {
        items(cities) { city ->
            var districtExpand by remember { mutableStateOf(false) }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        districtExpand = !districtExpand // 点击时切换展开/收起状态
                        selectedCity = if (selectedCity == city.name) null else city.name
                    }
            ) {
                val textColor = if (selectedCity == city.name) Color.Blue else Color.Black
                Text(text = city.name, color = textColor)
            }

            // 如果 districtExpand 为 true，则显示该城市的区列表
            if (districtExpand) {
                city.districts.forEach { district ->
                    Text(
                        text = district.name,
                        modifier = Modifier.padding(start = 16.dp) // 添加一些缩进使区的显示层级更明显
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSearchBar(cities: List<City>){
    var searchText by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var citySearchText by remember { mutableStateOf("選擇區域") }
    var cityExpand by remember { mutableStateOf(false) }
    var priceExpand by remember { mutableStateOf(false) }
    var foodLabelExpand by remember { mutableStateOf(false) }
    var arrowIcon by remember { mutableStateOf(R.drawable.arrow_right) }
    var arrowIcon2 by remember { mutableStateOf(R.drawable.arrow_right) }
    var arrowIcon3 by remember { mutableStateOf(R.drawable.arrow_right) }
    SearchBar(
        query = searchText,
        onQueryChange = {searchText = it},
        modifier = Modifier.fillMaxWidth(),
        onSearch = {isActive = false},
        active = isActive,
        onActiveChange = {
            isActive = it
        },
        placeholder = { Text(text = "請輸入地名, 食物, 店家") },
        leadingIcon = {
            if (isActive){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ArrowBack",
                    modifier = Modifier.clickable { isActive = false }
                )
            }else{
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search"
                )
            }

        },
        trailingIcon = {
            if (searchText != ""){
                Icon(
                    imageVector =Icons.Default.Clear,
                    contentDescription = "clear",
                    modifier = Modifier.clickable {
                        searchText = ""
                    }
                )
            }
        },

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(modifier = Modifier.clickable {
                cityExpand = !cityExpand
                arrowIcon = if (arrowIcon == R.drawable.arrow_right) {
                    R.drawable.arrow_drop_down
                } else {
                    R.drawable.arrow_right
                }
            }) {
                Icon(
                    painter = painterResource(arrowIcon),
                    contentDescription = "Arrow Icon",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = citySearchText,
                )
            }
            if (cityExpand) {
                CityLists(cities)
            }
        }
    }
}


@Composable
fun ShowRestaurantLists(restaurants: List<Restaurant>, state: Boolean){
    Log.d("Restaurant", "1")
    if (state){
        LazyColumn (
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ){
            items(restaurants){
                restaurant ->
                Card (modifier = Modifier.fillMaxWidth().padding(16.dp)){
                    ListItem(
                        headlineContent = {Text(restaurant.name)},
                        supportingContent = {Text(text = restaurant.address)},
                        leadingContent = { ImageScreen(restaurant) }
                    )
                    HorizontalDivider()
                }
            }
        }
    } else {
        LazyRow (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ){
            items(restaurants){
                    restaurant ->
                Card (
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardColors(
                        containerColor = colorResource(R.color.orange),
                        contentColor = Color.Black,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.Gray)
                ){
                    ListItem(
                        headlineContent = {Text(restaurant.name)},
                        supportingContent = {Text(text = restaurant.address)},
                        leadingContent = { ImageScreen(restaurant) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}


@Composable
fun DisplayImageFromUrl(imageUrl: String, modifier: Modifier = Modifier) {
    // 使用 Coil 的 rememberImagePainter 加载图片
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop // 根据需要设置内容比例
    )
}

@Composable
fun ImageScreen(restaurant: Restaurant) {
    DisplayImageFromUrl(
        imageUrl = restaurant.photo_url,
        modifier = Modifier.size(80.dp, 60.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    val context = LocalContext.current
    val restaurants = parseRestaurantJson(context, "restaurants.json")
    ShowRestaurantLists(restaurants, true)
}