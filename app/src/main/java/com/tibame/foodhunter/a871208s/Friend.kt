package com.tibame.foodhunter.a871208s


data class Friend(var member_id: Int,var username: String, var nickname: String, val profileImageBase64: String?){
    override fun equals(other: Any?): Boolean {
        return this.username == (other as Friend).username
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }
}






data class PrivateChat(var roomId: String ="", var username: String ="", var nickname: String="", var profileImageBase64: String?="")

data class Message(var message_id: String ="", var receiver_id: String ="", var message: String="",var message_time: String="")