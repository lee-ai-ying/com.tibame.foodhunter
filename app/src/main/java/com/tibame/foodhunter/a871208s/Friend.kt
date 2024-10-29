package com.tibame.foodhunter.a871208s

import com.tibame.foodhunter.R


class Friend(var id: String, var name: String, var image: Int){
    override fun equals(other: Any?): Boolean {
        return this.id == (other as Friend).id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class PrivateChat(var roomid: String ="",var id: String ="", var name: String="", var image: Int=0,var status: Int=0){


}