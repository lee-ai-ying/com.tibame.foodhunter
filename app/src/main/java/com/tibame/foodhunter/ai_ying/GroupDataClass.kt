package com.tibame.foodhunter.ai_ying

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class GroupChat(
    var groupId: Int = 0,
    var groupName: String = "defaultName",
    var groupState: Int = 1
)

data class GroupCreateData(
    var name: String = "",
    var location: String = "",
    var time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var price: String = "1-2000+",
    var joinMember: String = "",
    var public: String = "public",
    var describe: String = ""
)

data class GroupSearchData(
    var name: String = "",
    var location: String = "",
    var time: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
    var price: String = "1-2000+",
    var tags: List<String> = emptyList()
)