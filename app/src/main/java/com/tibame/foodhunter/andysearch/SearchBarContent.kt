package com.tibame.foodhunter.andysearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tibame.foodhunter.R

@Composable
fun SearchBerContent(
    cities: List<City>,
    searchTextVM: SearchScreenVM
){


    val foodVM: RandomFoodVM = viewModel()
    val context = LocalContext.current
    val labelTags by foodVM.labelTags.collectAsState()
    val selectedItems = remember { mutableStateListOf<String>() }
    var cityExpand by remember { mutableStateOf(false) }
    var priceExpand by remember { mutableStateOf(false) }
    var foodLabelExpand by remember { mutableStateOf(false) }
    var arrowIcon by remember { mutableStateOf(R.drawable.arrow_right) }
    var arrowIcon2 by remember { mutableStateOf(R.drawable.arrow_right) }
    var arrowIcon3 by remember { mutableStateOf(R.drawable.arrow_right) }
    var maxPrice by remember { mutableStateOf(100.0f) }
    var minPrice by remember { mutableStateOf(0.0f) }

    val choicePrice by searchTextVM.choicePrice.collectAsState()
    val otherConditions by searchTextVM.otherConditions.collectAsState()
    val showSearchText by searchTextVM.showSearchText.collectAsState()
    val cityText by searchTextVM.cityText.collectAsState()
    LaunchedEffect(Unit) {
        foodVM.loadLabelTags(context)
    }

    Text(text = "搜尋條件 : $showSearchText")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Row() {
            Icon(
                painter = painterResource(arrowIcon),
                contentDescription = "Arrow Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = cityText.ifBlank { "選擇區域" },
                modifier = Modifier.clickable {
                    cityExpand = !cityExpand
                    arrowIcon = if (arrowIcon == R.drawable.arrow_right) {
                        R.drawable.arrow_drop_down
                    } else {
                        R.drawable.arrow_right
                    }
                }
            )
            if (cityExpand) {
                CityLists(cities, searchTextVM)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(modifier = Modifier.clickable {
            priceExpand = !priceExpand
            arrowIcon2 = if (arrowIcon2 == R.drawable.arrow_right) {
                R.drawable.arrow_drop_down
            } else {
                R.drawable.arrow_right
            }
        }) {
            Icon(
                painter = painterResource(arrowIcon2),
                contentDescription = "Arrow Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "${minPrice.toInt()} ~ ${maxPrice.toInt()}",
            )
        }
        if (priceExpand) {
            Row(modifier = Modifier.fillMaxWidth()) {
                RangeSlider(
                    value = minPrice..maxPrice,
                    steps = 9,
                    valueRange = 0f..2000f,
                    onValueChange = { rangeValues ->
                        minPrice = adjustToStep(rangeValues.start, 200f)
                        maxPrice = adjustToStep(rangeValues.endInclusive, 200f)
                    }
                )
                searchTextVM.updateChoicePrice("$minPrice ~ $maxPrice")
                searchTextVM.loadShowSearchText()
            }
        }
    }
    Column (modifier = Modifier.fillMaxWidth().padding(start = 24.dp)){
        Row(modifier = Modifier.clickable {
            foodLabelExpand = !foodLabelExpand
            arrowIcon3 = if (arrowIcon3 == R.drawable.arrow_right) {
                R.drawable.arrow_drop_down
            } else {
                R.drawable.arrow_right
            }
        }) {
            Icon(
                painter = painterResource(arrowIcon3),
                contentDescription = "Arrow Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "美食種類/餐廳種類",
            )
        }

        var action by remember { mutableStateOf(0) }
        if (foodLabelExpand){
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 60.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(labelTags.take(7*action)) { filter ->
                    var selected by remember { mutableStateOf(false) }
                    FilterChip(
                        selected = selected,
                        onClick = {
                            selected = !selected
                            if (selected) {
                                selectedItems.add(filter)
                            } else {
                                selectedItems.remove(filter)
                            }
                            // 更新 ViewModel 中的設定隨機食物列表
                            searchTextVM.updateOtherConditions(selectedItems)
                            searchTextVM.loadShowSearchText()
//                            searchTextVM.updateSearchText()
                        },
                        label = { Text(filter) } ,
                        colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = (colorResource(R.color.orange_5th))),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            IconButton(onClick = { action+=1 }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reload Tags",
                    modifier = Modifier.size(16.dp)
                )
            }
        }

    }
}

private fun adjustToStep(value: Float, step: Float): Float {
    return (Math.round(value / step) * step)
}

@Composable
fun CityLists(
    cities: List<City>,
    searchTextVM: SearchScreenVM
) {
    var selectedCity by remember { mutableStateOf<String?>(null) }
    var selectedDist by remember { mutableStateOf<String?>(null) }
    LazyColumn(
        modifier = Modifier
            .padding(start = 36.dp)
            .height(200.dp)
    ) {
        items(cities) { city ->
            var districtExpand by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier
                    .clickable {
                        districtExpand = !districtExpand
                        selectedCity = if (selectedCity == city.name) null else city.name
                    }
                ) {
                    val textColor = if (selectedCity == city.name) Color.Blue else Color.Black
                    Text(text = city.name, color = textColor)
                }
                if (districtExpand) {
                    Column{
                        city.districts.forEach { district ->
                            Text(
                                text = district.name,
                                modifier = Modifier.padding(start = 8.dp).clickable {
                                    searchTextVM.updateCityText(city.name+district.name)
                                    searchTextVM.loadShowSearchText()
//                                    searchTextVM.updateSearchText()
                                    selectedDist = if(selectedDist == district.name) null else district.name
                                    districtExpand = false
                                },
                                color = if (selectedDist == district.name) Color.Blue else Color.Black

                            )
                        }
                    }
                }
            }
        }
    }
}