package com.tibame.foodhunter.ai_ying

data class GroupChat(
    var groupId: Int = 0,
    var groupName: String = "defaultName",
    var groupState: Int = 1
)

data class CreateNewGroupData(
    var name: String = "",
    var location: String = "",
    var time: String = "",
    var price: String = "",
    var joinMember: String = "",
    var public: String = "",
    var describe: String
)