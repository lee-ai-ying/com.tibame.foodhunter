package com.tibame.foodhunter.zoe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
//
//    // 獲取用戶特定帖子
//    fun getUserPosts(publisherId: String): StateFlow<List<Post>> {
//        return postChatFlow.map { posts ->
//            posts.filter { it.publisher.id == publisherId }
//        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
//    }
//
//    fun updateFilters(filters: Set<String>) {
//        _selectedFilters.value = filters
//    }
//
//    fun updateTabIndex(index: Int) {
//        _selectedTabIndex.value = index
//    }
//
//    fun updateSearchQuery(query: String) {
//        _searchQuery.value = query
//        performSearch()
//    }
//
//    fun performSearch() {
//        // 搜索邏輯已整合到 getFilteredPosts() 中
//        viewModelScope.launch {
//            // 可以在這裡添加額外的搜索相關操作
//            // 例如：記錄搜索歷史、更新搜索建議等
//        }
//    }
//
//    // 清除錯誤訊息
//    fun clearError() {
//        _error.value = null
//    }
//
//    // 清除當前用戶數據
//    fun clearUserData() {
//        _userPosts.value = emptyList()
//        _publisher.value = null
//    }
//}
class PostViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    // 直接使用 PostRepository 的 postList
    val postList = PostRepository.postList

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilters(filters: List<String>) {
        _selectedFilters.value = filters
    }

    fun getFilteredPosts(): StateFlow<List<Post>> {
        return combine(
            postList,
            selectedFilters,
            searchQuery
        ) { posts, filters, query ->
            var filteredList = posts

            // 應用標籤過濾
            if (filters.isNotEmpty()) {
                filteredList = filteredList.filter { post ->
                    filters.contains(post.postTag)
                }
            }

            // 應用搜尋過濾
            if (query.isNotEmpty()) {
                filteredList = filteredList.filter { post ->
                    post.content.contains(query, ignoreCase = true) ||
                            post.location.contains(query, ignoreCase = true) ||
                            post.publisher.name.contains(query, ignoreCase = true)
                }
            }

            // 根據時間排序（最新的優先）
            filteredList.sortedByDescending { post ->
                post.timestamp
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }
}