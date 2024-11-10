package com.tibame.foodhunter.sharon.data

import com.tibame.foodhunter.global.serverUrl

class GroupRepository private constructor() {
    fun getGroups(currentMemberId: Int) {

    }

    companion object {
        private const val TAG = "GroupRepository"

        // 使用團隊統一的 serverUrl
        private const val BASE_URL = serverUrl
        private const val API_PATH = "/api/note"
        val instance = GroupRepository()
    }
}
