package com.tibame.foodhunter.andysearch

import android.content.Context
import androidx.compose.material3.Label
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RandomFoodVM: ViewModel(){

    private val _foodLabel = MutableStateFlow(emptyMap<String, List<String>>())
    val foodLabel: StateFlow<Map<String, List<String>>> = _foodLabel.asStateFlow()

    private val _randomFood = MutableStateFlow(emptyList<String>())
    val settingRandomFood: StateFlow<List<String>> = _randomFood.asStateFlow()

    private val _choiceCity = MutableStateFlow("台北市中正區")
    val choiceCity: StateFlow<String> = _choiceCity.asStateFlow()
    // searchbar 用的
    private val _labelTags = MutableStateFlow(emptyList<String>())
    val labelTags:StateFlow<List<String>> = _labelTags.asStateFlow()

    init {
        // 初始化時設置預設值
        _randomFood.update { settingRandomFoodList(emptyList()) }

    }
    fun updateSettingRandomFood(item: List<String>){
        _randomFood.update{settingRandomFoodList(item)}
    }
    fun updateChoiceCity(newCity: String){
        _choiceCity.update{newCity}
    }

    fun loadFoodLabel(context: Context){
        _foodLabel.update { loadLabelsFromJson(context) }
    }

    fun loadLabelTags(context: Context){
        _labelTags.update{shuffleLabel(context)}
    }


    private fun loadLabelsFromJson(context: Context): Map<String, List<String>> {
        val jsonString = readJson(context, "restLabel.json")
        val gson = Gson()
        val type = object : TypeToken<Map<String, List<String>>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    private fun settingRandomFoodList(settingList: List<String>):List<String>{
        val options = settingList.ifEmpty { listOf("早午餐", "午餐", "晚餐", "韓式", "日式", "義式") }
        return options
    }

    // action 按幾次按鈕
    private fun shuffleLabel(context: Context): List<String> {
        val labelMap = loadLabelsFromJson(context)
        val restaurantTags = labelMap["restaurant_tags"] ?: emptyList()
        val foodTags = labelMap["food_tags"] ?: emptyList()
        val labelList = restaurantTags + foodTags

        return labelList.shuffled()

    }

}