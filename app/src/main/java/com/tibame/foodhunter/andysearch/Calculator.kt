package com.tibame.foodhunter.andysearch

import android.util.Log
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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









