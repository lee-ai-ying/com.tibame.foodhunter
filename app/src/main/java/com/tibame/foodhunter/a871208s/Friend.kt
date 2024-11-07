package com.tibame.foodhunter.a871208s


data class Friend(var username: String, var name: String, var image: Int){
    override fun equals(other: Any?): Boolean {
        return this.username == (other as Friend).username
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }
}






data class PrivateChat(var roomid: String ="",var id: String ="", var name: String="6", var image: Int=0,var status: Int=0)