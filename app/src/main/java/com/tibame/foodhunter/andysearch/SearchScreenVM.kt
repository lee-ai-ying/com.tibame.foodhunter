package com.tibame.foodhunter.andysearch

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.global.CommonPost
import com.tibame.foodhunter.global.serverUrl
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
    private val _preRestaurantList = MutableStateFlow(emptyList<Restaurant>())
    val preRestaurantList: StateFlow<List<Restaurant>> = _preRestaurantList.asStateFlow()

    // 找尋餐廳的response
    private val _selectRestList = MutableStateFlow(emptyList<Restaurant>())
    val selectRestList: StateFlow<List<Restaurant>> = _selectRestList.asStateFlow()

    // 選擇單一餐廳
    private val _choiceOneRest = MutableStateFlow<Restaurant?>(null)
    val choiceOneRest: StateFlow<Restaurant?> = _choiceOneRest.asStateFlow()

    init {
        // 初始化時設置預設值
        _searchText.update { "" }

    }


    fun updateSearchText(newText: String){
        _searchText.update {newText}
    }

    // 選擇餐廳
    fun updateChoiceOneRest(choiceRest: Restaurant){
        _choiceOneRest.update { choiceRest }
    }
    // 清空單一餐廳
    fun clearChoiceRest(){
        _choiceOneRest.update { null }
    }

    // 預先載入10家餐廳
    fun preloadRestaurants(){
        CoroutineScope(Dispatchers.IO).launch {
            val initialRestaurants = fetchInitRestaurant()
            _preRestaurantList.update { initialRestaurants }
        }
    }

    // 從資料庫中獲取10家餐廳
    private suspend fun fetchInitRestaurant(): List<Restaurant>{
        val url = "${serverUrl}/PreLoadRest"
        val gson = Gson()
        val result = CommonPost(url, "")
        val type = object : TypeToken<List<Restaurant>>() {}.type
        return gson.fromJson(result, type)
    }

    suspend fun updateSearchRest(searchText: String){
        _selectRestList.update { fetchRestaurant(searchText) }
    }

    // 搜尋餐廳從database
    private suspend fun fetchRestaurant(searchText: String): List<Restaurant>{
        val url = "${serverUrl}/SelectRestController"
        val gson = Gson()
        val jsonObject = JsonObject()
        jsonObject.addProperty("searchText", searchText)
        val result = CommonPost(url, jsonObject.toString())
        val type = object : TypeToken<List<Restaurant>>() {}.type
        return gson.fromJson(result, type)
    }
}