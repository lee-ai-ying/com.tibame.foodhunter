package com.tibame.foodhunter.ai_ying

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GroupChat(
    @SerializedName("groupId") var id: Int = 0,
    @SerializedName("groupName") var name: String = "defaultName",
    var state: Int = 1,
    var location: String = "",
    var time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var price: String = "1-2000+",
    var joinMember: String = "",
    var public: String = "public",
    var describe: String = ""
)

data class GroupCreateData(
    var name: String = "",
    var location: String = "",
    var time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var priceMin: Int = 1,
    var priceMax: Int = 2000,
    var joinMember: String = "",
    var isPublic: Int = 0,
    var describe: String = ""
)

data class GroupSearchData(
    var name: String = "",
    var location: String = "",
    var time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var priceMin: Int = 1,
    var priceMax: Int = 2000,
    var describe: String = ""
)

data class GroupSearchResult(
    @SerializedName("id") var groupId:Int = 0,
    @SerializedName("name") var groupName:String = ""
)