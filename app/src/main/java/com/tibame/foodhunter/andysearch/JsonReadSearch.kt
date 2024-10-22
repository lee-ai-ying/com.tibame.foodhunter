package com.tibame.foodhunter.andysearch

import android.content.Context

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class District(
    val zip: String,
    val name: String
)

data class City(
    val name: String,
    val districts: List<District>
)


data class Restaurant(
    val restaurant_id: String,
    val name: String,
    val address: String,
    val rating: String,
    val total_review: String,
    val latitude: String,
    val longitude: String,
    val restaurant_label: String,
    val opening_hours: String,
    val home_phone: String,
    val email: String,
    val price_range_max: String,
    val price_range_min: String,
    val service_charge: String,
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


fun parseRestaurantJson(context: Context, fileName: String):List<Restaurant>{
    val jsonString = readJson(context, fileName)
    val gson = Gson()
    val inputType = object : TypeToken<List<Restaurant>>() {}.type
    return gson.fromJson(jsonString, inputType)
}

