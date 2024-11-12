package com.tibame.foodhunter.andysearch

import android.util.Log
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

fun IsOpenNow(openingHours: String): Boolean {
    val now = LocalDateTime.now()
    val currentDay = when(now.dayOfWeek.name.lowercase(Locale.getDefault())
    ){
        "monday" -> "星期一"
        "tuesday" -> "星期二"
        "wednesday" -> "星期三"
        "thursday" -> "星期四"
        "friday" -> "星期五"
        "saturday" -> "星期六"
        "sunday" -> "星期日"
        else -> ""
    }
    val currentTime = now.toLocalTime()
    val dayOfHoursList = openingHours.split(";").map{it.trim()}

    for (dayHours in dayOfHoursList){
        if (dayHours.startsWith(currentDay)){
            val timeRanges = dayHours.substringAfter(":").split(",").map { it.trim() }
            for (timeRange in timeRanges){
                val times = timeRange.replace("–", "-").split("-").map { it.trim() }
                if (times.size == 2 ) {
                    val openingTime =
                        LocalTime.parse(times[0], DateTimeFormatter.ofPattern("HH:mm"))
                    var closeTime = LocalTime.parse(times[1], DateTimeFormatter.ofPattern("HH:mm"))
                    if (times[1] == "00:00" || times[0] == "00:00") {
                        closeTime = LocalTime.MAX // 將 "00:00" 視為當天的結束時間，設置為 23:59:59.999999999
                    }
                    Log.d("openTime", "Open: $openingTime")
                    Log.d("openTime", "Close: $closeTime")
                    // 檢查當前時間是否在營業時間內
                    if (closeTime.isBefore(openingTime)) {
                        // 跨天情況，如 22:00 - 02:00
                        if (currentTime.isAfter(openingTime) || currentTime.isBefore(closeTime)) {
                            return true
                        }
                    } else {
                        // 正常情況，如 06:00 - 23:00
                        if (currentTime.isAfter(openingTime) && currentTime.isBefore(closeTime)) {
                            return true
                        }
                    }
                }
            }

        }
    }
    return false
}

fun extractAddress(address: String, regexState: Int): String? {
    val regex = when (regexState) {
        0 -> """台灣(\w+市)(\w+區)(\w+[路街])""".toRegex()
        else -> """.*?市(.*)""".toRegex()
    }

    val matchResult = regex.find(address)

    return matchResult?.let {
        when (regexState) {
            0 -> {
                val city = it.groupValues.getOrNull(1) ?: ""
                val district = it.groupValues.getOrNull(2) ?: ""
                val road = it.groupValues.getOrNull(3) ?: ""
                Log.d("Address", "road: $road")
                "$city, $district, $road"
            }
            else -> {
                val restOfAddress = it.groupValues.getOrNull(1) ?: ""
                Log.d("Address", "rest of address: $restOfAddress")
                restOfAddress
            }
        }
    }
}


fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
    // 將經緯度從度數轉換為弧度
    val earthRadiusKM = 6371.0 // 地球半徑
    val lat1Rad = Math.toRadians(lat1)
    val lon1Rad = Math.toRadians(lon1)
    val lat2Rad = Math.toRadians(lat2)
    val lon2Rad = Math.toRadians(lon2)

    // 緯度和經度的差值
    val dLat = lat2Rad - lat1Rad
    val dLon = lon2Rad - lon1Rad

    // Haversine公式
    val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))


    val distance = round(earthRadiusKM * c * 10.0) / 10.0
    return distance.toString()

}




