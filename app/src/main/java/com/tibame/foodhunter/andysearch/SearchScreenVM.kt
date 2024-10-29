package com.tibame.foodhunter.andysearch

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchScreenVM: ViewModel(){
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _restaurantList = MutableStateFlow(emptyList<Restaurant>())
    val restaurantList: StateFlow<List<Restaurant>> = _restaurantList.asStateFlow()

    init {
        // 初始化時設置預設值
        _searchText.update { "" }
    }


    fun updateSearchText(newText: String){
        _searchText.update {newText}
    }

    // 更新餐廳
    fun updateRestaurantList(context: Context){
        _restaurantList.update{loadRestaurantFromJson(context)}
    }

    private fun loadRestaurantFromJson(context: Context): List<Restaurant> {
        val jsonString = readJson(context, "restaurants.json")
        val gson = Gson()
        val type = object : TypeToken<List<Restaurant>>() {}.type
        return gson.fromJson(jsonString, type)
    }




}