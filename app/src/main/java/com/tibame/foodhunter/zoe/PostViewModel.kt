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

class PostViewModel : ViewModel() {
    private val repository = PostRepository
    val postChatFlow = repository.postList

    private val _selectedFilters = MutableStateFlow<Set<String>>(emptySet())
    val selectedFilters: StateFlow<Set<String>> = _selectedFilters.asStateFlow()

    private val _nowPostId = MutableStateFlow(999)
    val nowPostId: StateFlow<Int> = _nowPostId.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 新增：用戶個人頁面相關狀態
    private val _userPosts = MutableStateFlow<List<Post>>(emptyList())
    val userPosts: StateFlow<List<Post>> = _userPosts.asStateFlow()

    private val _publisher = MutableStateFlow<Publisher?>(null)
    val publisher: StateFlow<Publisher?> = _publisher.asStateFlow()

    // 新增：加載狀態
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 新增：錯誤狀態
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun gotoPost(postId: Int) {
        _nowPostId.update { postId }
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // 獲取用戶發佈的帖子
                val userPosts = repository.postList.value.filter {
                    it.publisher.id == postId.toString()
                }
                _userPosts.value = userPosts

                // 獲取發布者信息
                val publisher = userPosts.firstOrNull()?.publisher
                _publisher.value = publisher

                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
                _isLoading.value = false
            }
        }
    }

    // 獲取過濾後的帖子
    fun getFilteredPosts(
        postListFlow: StateFlow<List<Post>>,
        selectedFiltersFlow: StateFlow<List<String>>
    ): StateFlow<List<Post>> {
        return combine(postListFlow, selectedFiltersFlow) { postList, filters ->
            if (filters.isEmpty()) postList
            else postList.filter { post -> filters.contains(post.postTag) }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }



    // 獲取用戶特定帖子
    fun getUserPosts(publisherId: String): StateFlow<List<Post>> {
        return postChatFlow.map { posts ->
            posts.filter { it.publisher.id == publisherId }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun updateFilters(filters: Set<String>) {
        _selectedFilters.value = filters
    }

    fun updateTabIndex(index: Int) {
        _selectedTabIndex.value = index
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        performSearch()
    }

    fun performSearch() {
        // 搜索邏輯已整合到 getFilteredPosts() 中
        viewModelScope.launch {
            // 可以在這裡添加額外的搜索相關操作
            // 例如：記錄搜索歷史、更新搜索建議等
        }
    }

    // 清除錯誤訊息
    fun clearError() {
        _error.value = null
    }

    // 清除當前用戶數據
    fun clearUserData() {
        _userPosts.value = emptyList()
        _publisher.value = null
    }
}