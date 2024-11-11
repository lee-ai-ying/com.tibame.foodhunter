package com.tibame.foodhunter.ai_ying

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object GroupRepository {
    var gChatVM: GroupViewModel? = null
    private val _groupChatList = MutableStateFlow(emptyList<GroupChat>())
    val groupChatList = _groupChatList.asStateFlow()
    fun updateGroupChatList(input: List<GroupChat>) {
        _groupChatList.update { input }
    }

    private val _selectSearchResult = MutableStateFlow(GroupSearchResult())
    val selectSearchResult = _selectSearchResult.asStateFlow()
    fun updateSelectSearchResult(input: GroupSearchResult) {
        _selectSearchResult.update { input }
    }

    private val _searchCache = MutableStateFlow(GroupSearchData())
    val searchCache = _searchCache.asStateFlow()
    fun updateGroupSearchCache(input: GroupSearchData) {
        _searchCache.update { input }
    }

    private val _groupSearchResult = MutableStateFlow(emptyList<GroupSearchResult>())
    val groupSearchResult = _groupSearchResult.asStateFlow()
    fun updateSearchGroupResult(result: List<GroupSearchResult>) {
        _groupSearchResult.update { result }
    }

    private val _groupChatHistory = MutableStateFlow(emptyList<GroupChatHistory>())
    val groupChatHistory = _groupChatHistory.asStateFlow()
    fun updateGroupChatHistory(result: List<GroupChatHistory>) {
        _groupChatHistory.update { result }
    }
    private val _groupChatAvatar = MutableStateFlow(emptyList<GroupChatImage>())
    val groupChatAvatar = _groupChatAvatar.asStateFlow()
    fun updateGroupChatAvatar(result: List<GroupChatImage>) {
        _groupChatAvatar.update { result }
    }

    private val _restaurantList = MutableStateFlow(emptyList<GroupRestaurantData>())
    val restaurantList = _restaurantList.asStateFlow()
    fun updateRestaurantList(result: List<GroupRestaurantData>) {
        _restaurantList.update { result }
    }

    private val _restaurantReview = MutableStateFlow(GroupReviewData())
    val restaurantReview = _restaurantReview.asStateFlow()
    fun updateRestaurantList(result: GroupReviewData) {
        _restaurantReview.update { result }
    }

}