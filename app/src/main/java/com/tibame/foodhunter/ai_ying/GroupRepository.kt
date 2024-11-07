package com.tibame.foodhunter.ai_ying

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object GroupRepository {
    private val _groupChatList = MutableStateFlow(emptyList<GroupChat>())
    val groupChatList = _groupChatList.asStateFlow()
    fun updateGroupChatList(input: List<GroupChat>){
        _groupChatList.update { input }
    }
    private val _selectSearchResult = MutableStateFlow(GroupSearchResult())
    val selectSearchResult = _selectSearchResult.asStateFlow()
    fun updateSelectSearchResult(input: GroupSearchResult){
        _selectSearchResult.update { input }
    }
    private val _searchCache = MutableStateFlow(GroupSearchData())
    val searchCache = _searchCache.asStateFlow()
    fun updateGroupSearchCache(input: GroupSearchData){
        _searchCache.update { input }
    }
    private val _groupSearchResult = MutableStateFlow(emptyList<GroupSearchResult>())
    val groupSearchResult = _groupSearchResult.asStateFlow()
    fun updateSearchGroupResult(result: List<GroupSearchResult>) {
        _groupSearchResult.update { result }
    }
    private val _groupChatHistory = MutableStateFlow(emptyList<GroupChatHistory>())
    val groupChatHistory = _groupChatHistory.asStateFlow()
    fun updateGroupChatHistory(result: List<GroupChatHistory>){
        _groupChatHistory.update { result }
    }

    private fun searchFakeData(): List<GroupSearchResult> {
        return listOf(
            GroupSearchResult(1),
            GroupSearchResult(2),
            GroupSearchResult(3),
            GroupSearchResult(4),
            GroupSearchResult(5),
            GroupSearchResult(6),
            GroupSearchResult(7),
            GroupSearchResult(8),
            GroupSearchResult(9),
            GroupSearchResult(10),
            GroupSearchResult(11),
            GroupSearchResult(12),
            GroupSearchResult(13),
            GroupSearchResult(14),
            GroupSearchResult(15),
        )
    }

    private fun setTestData(): List<GroupChat> {
        return listOf(
            GroupChat(1, "進行中", 99),
            GroupChat(1, "GroupChat1", 1),
            GroupChat(2, "GroupChat2", 1),
            GroupChat(3, "GroupChat3", 1),
            GroupChat(4, "GroupChat4", 1),
            GroupChat(5, "GroupChat5", 1),
            GroupChat(6, "GroupChat6", 1),
            GroupChat(7, "GroupChat7", 1),
            GroupChat(8, "GroupChat8", 1),
            GroupChat(9, "已完成", 99),
            GroupChat(9, "GroupChat9", 0),
            GroupChat(10, "GroupChat10", 0),
            GroupChat(11, "GroupChat11", 0),
            GroupChat(12, "GroupChat12", 0),
            GroupChat(13, "GroupChat13", 0),
            GroupChat(14, "GroupChat14", 0),
            GroupChat(15, "GroupChat15", 0),
            GroupChat(16, "GroupChat16", 0),
        )
    }

}