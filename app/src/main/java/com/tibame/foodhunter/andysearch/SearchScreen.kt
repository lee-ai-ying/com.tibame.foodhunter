package com.tibame.foodhunter.andysearch

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.LatLng
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ui.theme.FColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ControllerScreen(){

}


@Composable
fun SearchScreen(
    navController: NavHostController,
    searchTextVM: SearchScreenVM
) {
    val context = LocalContext.current
    val preRestaurants by searchTextVM.preRestaurantList.collectAsState()
    val cities = remember { parseCityJson(context, "taiwan_districts.json") }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var delayScreen by remember { mutableStateOf(true) }
    val isInitialized = remember { mutableStateOf(false) }

    // 使用 LaunchedEffect 做延遲控制
    LaunchedEffect(Unit) {
        delay(1000) // 延遲 2 秒鐘
        delayScreen = false // 2 秒後切換到主頁面顯示
    }

    if (delayScreen) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("載入中") },
            text = { Text("正在準備餐廳資訊，請稍候...") },
            confirmButton = {}
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            if (!isInitialized.value) {
                LaunchedEffect(Unit) {
                    searchTextVM.preloadRestaurants()
                    isInitialized.value = true
                }
            }

            Log.d("preload", "preRestaurants")
            ShowSearchBar(
                cities = cities,
                searchTextVM = searchTextVM,
                state = true, navController = navController
            ) // state 如果true 導覽頁面到搜尋結果


            ShowGoogleMap(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.4f)
                    .padding(16.dp),
                preRestaurants,
                restaurantVM = searchTextVM,
                onLocationUpdate = { location -> currentLocation = location }
            )
            ShowRestaurantLists(
                preRestaurants, true, navController,
                currentLocation, searchTextVM
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSearchBar(
    cities: List<City>,
    searchTextVM: SearchScreenVM,
    state: Boolean = false,
    navController: NavHostController = rememberNavController()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isActive by remember { mutableStateOf(false) }

    val choicePrice by searchTextVM.choicePrice.collectAsState()
    val otherConditions by searchTextVM.otherConditions.collectAsState()
    val cityText by searchTextVM.cityText.collectAsState()
    val inputSearch by searchTextVM.searchText.collectAsState()

    val showSearchText by searchTextVM.showSearchText.collectAsState()
    val finalSearchText by searchTextVM.finalSearchText.collectAsState()
    SearchBar(
        query = showSearchText,
        onQueryChange = {
            searchTextVM.updateSearchText(it)
            searchTextVM.loadShowSearchText()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
            .heightIn(min = 40.dp)
            .background(
                color = if (showSearchText.isNotEmpty()) Color.White else FColor.Gary_20,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = if (showSearchText.isNotEmpty()) FColor.Orange_1st else FColor.Gary,
                shape = RoundedCornerShape(12.dp)
            ),
//        windowInsets =if (isActive) SearchBarDefaults.windowInsets else WindowInsets(left = 0.dp, top = 0.dp, right = 0.dp, bottom = 0.dp),
        windowInsets = WindowInsets(top = (-8).dp, left = 0.dp, right = 0.dp),
        onSearch = {
            isActive = false
            searchTextVM.updateSearchText(it)
            searchTextVM.buildSearch()
            if (finalSearchText.isNotBlank()) {
                coroutineScope.launch {
                    searchTextVM.updateSearchRest(finalSearchText)
                }
            }
            searchTextVM.clearChoiceRest() // 清空選擇餐廳
            if (state) {
                navController.navigate(context.getString(R.string.SearchToGoogleMap))
            }
        },
        active = isActive,
        onActiveChange = { isActive = it },
        placeholder = {
            Text(
                text = "搜尋想吃的食物, 店家",
                color = Color.Gray.copy(alpha = 0.5f)
            )
        },
        leadingIcon = {
            if (isActive) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ArrowBack",
                    modifier = Modifier
                        .clickable { isActive = false }
                        .size(20.dp),
                    tint = FColor.Dark_80
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    modifier = Modifier.size(20.dp),
                    tint = FColor.Dark_80
                )
            }

        },
        trailingIcon = {
            if (inputSearch != "" || cityText != "" || choicePrice != "" || otherConditions != "") {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear",
                    modifier = Modifier
                        .clickable {
                            searchTextVM.clearSearchText()
                            searchTextVM.loadShowSearchText()
                        }
                        .size(24.dp),
                    tint = FColor.Dark_80,
                )
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = if (showSearchText.isNotEmpty()) Color.White else FColor.Gary_20
        ),
        content = { SearchBerContent(cities, searchTextVM) }
    )
}

@Composable
fun ShowRestaurantLists(
    restaurants: List<Restaurant>,
    state: Boolean,
    navController: NavHostController = rememberNavController(),
    currentLocation: LatLng?,
    searchTextVM: SearchScreenVM,
    cardClick: ((Restaurant?) -> Unit)? = null
) {
    val context = LocalContext.current
    val sortedRestaurants = restaurants.sortedBy { restaurant ->
        currentLocation?.let { location ->
            haversine(
                location.latitude, location.longitude,
                restaurant.latitude, restaurant.longitude
            )
        } ?: restaurant.restaurant_id.toString()
    }
    Log.d("Restaurant", "1")
    if (state) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {

            Text(
                text = "", style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.weight(0.6f))
            Row(
                modifier = Modifier
                    .clickable {
                        navController.navigate(route = context.getString(R.string.randomFood))
                    }
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(R.color.orange_3rd)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.random_food_icon),
                    contentDescription = "Go to Map",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = context.getString(R.string.randomFood))
                        }
                        .size(40.dp, 40.dp)
                        .padding(start = 8.dp, end = 4.dp)
                )
                Text(
                    text = "美食轉盤", style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W300
                    )
                )
            }


        }
        // 垂直互動
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            items(sortedRestaurants) { restaurant ->
                RestCard(
                    searchTextVM = searchTextVM,
                    restaurant = restaurant,
                    navController = navController,
                    currentLocation = currentLocation,
                    cardClick = null,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.arrow_right),
                            contentDescription = "Go to Map",
                            modifier = Modifier.clickable {
                                searchTextVM.updateChoiceOneRest(restaurant)
                                navController.navigate(route = context.getString(R.string.SearchToGoogleMap))
                            })
                    }
                )
            }
        }
    } else {
        // 橫向滑動
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            items(sortedRestaurants) { restaurant ->
                RestCard(
                    searchTextVM = searchTextVM,
                    restaurant = restaurant,
                    navController = navController,
                    currentLocation = currentLocation,
                    cardClick = cardClick,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.arrow_right),
                            contentDescription = "Go to Map",
                            modifier = Modifier.clickable {
                                searchTextVM.updateChoiceOneRest(restaurant)
                            })
                    },
                    cardPadding = 8.dp
                )
            }
        }
    }
}


@Composable
fun RestCard(
    searchTextVM: SearchScreenVM, // 帶入餐廳相關的VM
    restaurant: Restaurant,
    navController: NavHostController,
    currentLocation: LatLng?,  //傳入當前位子
    cardClick: ((Restaurant) -> Unit)?, // 對這個Card點擊要做的動作 沒有的話會到餐廳詳細的頁面
    trailingIcon: @Composable () -> Unit = {},
    cardPadding: Dp = 16.dp
) {
    val distance = currentLocation?.let { location ->
        haversine(
            location.latitude, location.longitude,
            restaurant.latitude, restaurant.longitude
        )
    } ?: "尚未開啟定位"
    val context = LocalContext.current
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(cardPadding)
        .shadow(elevation = 8.dp)
        .clickable {
            searchTextVM.updateChoiceOneRest(restaurant)
            if (cardClick != null) {
                cardClick(restaurant)
            } else {
                navController.navigate(context.getString(R.string.restaurantDetail))
            }
        }) {
        ListItem(
            headlineContent = {
                Text(
                    text = restaurant.name,
                    modifier = Modifier.widthIn(min = 100.dp, max = 120.dp), // 限制宽度
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )
            },
            supportingContent = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "$distance 公里",
                        )
                        Icon(
                            painter = painterResource(R.drawable.baseline_location_pin_24),
                            contentDescription = "calculator KM"
                        )
                    }
                    Text(
                        text = extractCityArea(restaurant.address) ?: "",
                        modifier = Modifier.widthIn(min = 100.dp, max = 150.dp), // 限制宽度
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            leadingContent = { ImageScreen() }, // 預計放的預覽圖片
            trailingContent = {
                Row {
                    Image(
                        painter = painterResource(R.drawable.googlemap),
                        contentDescription = "open google map",
                        modifier = Modifier.clickable {
                            openNavigationMap(
                                context = context,
                                latitude = restaurant.latitude,
                                longitude = restaurant.longitude
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    if (IsOpenNow(restaurant.opening_hours)) {
                        Text(
                            text = "營業中", style = TextStyle(
                                color = colorResource(R.color.teal_700), fontSize = 16.sp
                            )
                        )
                    } else {
                        Text(
                            text = "休息中", style = TextStyle(
                                color = Color.Gray, fontSize = 16.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    trailingIcon()
                }
            },
            modifier = Modifier.height(100.dp),
            colors = ListItemColors(
                containerColor = colorResource(R.color.orange_5th),
                headlineColor = colorResource(R.color.black),
                leadingIconColor = colorResource(R.color.black),
                overlineColor = colorResource(R.color.orange_4th),
                supportingTextColor = colorResource(R.color.black),
                trailingIconColor = colorResource(R.color.gary),
                colorResource(R.color.orange_4th),
                colorResource(R.color.orange_4th),
                colorResource(R.color.orange_4th)
            )
        )
        HorizontalDivider()
    }
}


// 照片顯示 url 版
@Composable
fun DisplayImage(modifier: Modifier = Modifier) {
    // 使用 Coil 的 rememberImagePainter 加载图片
    Image(
        painter = painterResource(R.drawable.steak_image),
//        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop // 根据需要设置内容比例
    )
}

@Composable
fun ImageScreen() {
    DisplayImage(
        modifier = Modifier.size(80.dp, 60.dp)
    )
}
