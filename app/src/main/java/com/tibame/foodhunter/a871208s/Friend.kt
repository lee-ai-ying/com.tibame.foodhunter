package com.tibame.foodhunter.a871208s


data class Friend(var username: String, var nickname: String, val profileImageBase64: String?){
    override fun equals(other: Any?): Boolean {
        return this.username == (other as Friend).username
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }
}






data class PrivateChat(var roomid: String ="", var username: String ="", var nickname: String="", var profileImageBase64: String?="")