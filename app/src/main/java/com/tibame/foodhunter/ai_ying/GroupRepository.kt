package com.tibame.foodhunter.ai_ying

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object GroupRepository {
    private val _groupChatList = MutableStateFlow(emptyList<GroupChat>())
    val groupChatList: StateFlow<List<GroupChat>> = _groupChatList.asStateFlow()

    init {
        _groupChatList.update { setTestData() }
    }
    private fun setTestData(): List<GroupChat> {
        return listOf(
            GroupChat(1,"進行中",99),
            GroupChat(1,"GroupChat1",1),
            GroupChat(2,"GroupChat2",1),
            GroupChat(3,"GroupChat3",1),
            GroupChat(4,"GroupChat4",1),
            GroupChat(5,"GroupChat5",1),
            GroupChat(6,"GroupChat6",1),
            GroupChat(7,"GroupChat7",1),
            GroupChat(8,"GroupChat8",1),
            GroupChat(9,"已完成",99),
            GroupChat(9,"GroupChat9",0),
            GroupChat(10,"GroupChat10",0),
            GroupChat(11,"GroupChat11",0),
            GroupChat(12,"GroupChat12",0),
            GroupChat(13,"GroupChat13",0),
            GroupChat(14,"GroupChat14",0),
            GroupChat(15,"GroupChat15",0),
            GroupChat(16,"GroupChat16",0),
        )
    }
}