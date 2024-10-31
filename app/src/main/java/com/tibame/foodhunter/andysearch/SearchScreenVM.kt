package com.tibame.foodhunter.andysearch

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenVM: ViewModel(){
    // 搜尋文字
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    // 預先載入的餐廳
    private val _restaurantList = MutableStateFlow(emptyList<Restaurant>())
    val restaurantList: StateFlow<List<Restaurant>> = _restaurantList.asStateFlow()

    // 找尋餐廳的response
    private val _restListFromDataBase = MutableStateFlow(emptyList<Restaurant>())
    val restListFromDataBase: StateFlow<List<Restaurant>> = _restListFromDataBase.asStateFlow()

    init {
        // 初始化時設置預設值
        _searchText.update { "" }
        preloadRestaurants()
    }


    fun updateSearchText(newText: String){
        _searchText.update {newText}
    }

    // 預先載入10家餐廳
    private fun preloadRestaurants(){
        CoroutineScope(Dispatchers.IO).launch {
            val initialRestaurants = fetchInitRestaurant()
            _restaurantList.update { initialRestaurants }
        }
    }

    // 從資料庫中獲取10家餐廳
    private suspend fun fetchInitRestaurant(): List<Restaurant>{
        val url = "${serverUrl}/PreLoadRest"
        val gson = Gson()
        val result = selectRestPost(url, "")
        val type = object : TypeToken<List<Restaurant>>() {}.type
        return gson.fromJson(result, type)
    }

    suspend fun updateSearchRest(searchText: String){
        _restListFromDataBase.update { fetchRestaurant(searchText) }
    }

    // 搜尋餐廳從database
    private suspend fun fetchRestaurant(searchText: String): List<Restaurant>{
        val url = "${serverUrl}/SelectRestController"
        val gson = Gson()
        val jsonObject = JsonObject()
        jsonObject.addProperty("restaurantName", searchText)
        jsonObject.addProperty("restaurantLabel", searchText)
        val result = selectRestPost(url, jsonObject.toString())
        val type = object : TypeToken<List<Restaurant>>() {}.type
        return gson.fromJson(result, type)
    }
}