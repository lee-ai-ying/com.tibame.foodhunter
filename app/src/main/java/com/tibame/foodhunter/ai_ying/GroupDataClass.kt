package com.tibame.foodhunter.ai_ying

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GroupChat(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String = "defaultName",
    var state: Int = 1,
    @SerializedName("location") var location: String = "",
    @SerializedName("time") var time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    @SerializedName("priceMin") var priceMin: Int = 1,
    @SerializedName("priceMax") var priceMax: Int = 2000,
    var joinMember: String = "",
    @SerializedName("isPublic") var public: Int = 0,
    @SerializedName("describe") var describe: String = ""
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
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("location") var location: String = "",
    @SerializedName("time") var time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    @SerializedName("priceMin") var priceMin: Int = 1,
    @SerializedName("priceMax") var priceMax: Int = 2000,
    @SerializedName("describe") var describe: String = ""
)

data class GroupChatHistory(
    @SerializedName("memberId") var senderId: Int = 0,
    @SerializedName("memberName") var senderName: String = "",
    @SerializedName("message") var message: String = "",
    @SerializedName("sendTime") var sendTime: String = ""
)