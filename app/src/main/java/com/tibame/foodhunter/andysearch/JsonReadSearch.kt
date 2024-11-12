package com.tibame.foodhunter.andysearch

import android.content.Context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.zoe.PostPhoto

data class District(
    val zip: String,
    val name: String
)

data class City(
    val name: String,
    val districts: List<District>
)


data class Restaurant(
    val restaurant_id: Int,
    val name: String,
    val address: String,
    val total_scores: Int,
    val total_review: Int,
    val latitude: Double,
    val longitude: Double,
    val restaurant_label: String,
    val opening_hours: String,
    val home_phone: String,
    val email: String,
    val price_range_max: Int,
    val price_range_min: Int,
    val service_charge: String,
)

data class RestaurantUrl(
    val restaurant_id: Int,
    val photo_url: String
)


fun readJson(context: Context, fileName: String): String{
    return context.assets.open(fileName).bufferedReader().use { it.readText() }
}

fun parseCityJson(context: Context, fileName: String):List<City>{
    val jsonString = readJson(context, fileName)
    val gson = Gson()
    val inputType = object : TypeToken<List<City>>() {}.type
    return gson.fromJson(jsonString, inputType)
}



fun parsePhotoUrlJson(context: Context, fileName: String): List<RestaurantUrl>{
    val jsonString = readJson(context, fileName)
    val gson = Gson()
    val inputType = object : TypeToken<List<RestaurantUrl>>() {}.type
    return gson.fromJson(jsonString, inputType)
}