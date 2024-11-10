package com.tibame.foodhunter.zoe

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
    private val repository = PostRepository.getInstance()
    // 現有的狀態



    // 搜尋和篩選相關狀態
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()
    private val _selectedPostId = MutableStateFlow<Int?>(null)
    val selectedPostId = _selectedPostId.asStateFlow()

    private val personalPostsCache = mutableMapOf<Int, StateFlow<List<Post>>>()

    init {
        viewModelScope.launch {
            repository.loadPosts()
        }
    }


    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }




    fun deletePost(context: Context, postId: Int) {
        viewModelScope.launch {
            try {
                val success = repository.deletePost(postId)
                if (success) {
                    repository.loadPosts()
                }
                Toast.makeText(context, "貼文已刪除", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "刪除失敗", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getPersonalPosts(publisherId: Int): StateFlow<List<Post>> {
        return personalPostsCache.getOrPut(publisherId) {
            repository.postList
                .map { posts ->
                    posts.filter { post ->
                        post.publisher.id == publisherId
                    }.sortedByDescending {
                        it.timestamp
                    }
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = emptyList()
                )
        }
    }

    fun getPostById(postId: Int): StateFlow<Post?> {
        return combine(
            repository.postList.map { posts -> posts.find { it.postId == postId } },
            _tempComments
        ) { post, tempComments ->
            post?.let {
                // 如果有暫存的評論，合併到貼文中
                if (tempComments.containsKey(postId)) {
                    post.copy(comments = tempComments[postId] ?: post.comments)
                } else {
                    post
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun setPostId(postId: Int) {
        _selectedPostId.value = postId
    }
    private val _tempComments = MutableStateFlow<Map<Int, List<Comment>>>(emptyMap())
    private val _currentComments = MutableStateFlow<List<Comment>>(emptyList())
    val currentComments = _currentComments.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    fun createComment(postId: Int, userId: Int, content: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val success = repository.createComment(postId, userId, content)
                if (!success) {
                    Log.e("PostViewModel", "Failed to create comment")
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error creating comment", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun getFilteredPosts(): StateFlow<List<Post>> {
        return combine(
            repository.postList,
            selectedFilters,
            searchQuery
        ) { posts, filters, query ->
            var filteredList = posts

            if (filters.isNotEmpty()) {
                filteredList = filteredList.filter { post ->
                    filters.contains(post.postTag)
                }
            }

            if (query.isNotEmpty()) {
                filteredList = filteredList.filter { post ->
                    post.content.contains(query, ignoreCase = true) ||
                            post.location.contains(query, ignoreCase = true) ||
                            post.publisher.name.contains(query, ignoreCase = true)
                }
            }

            filteredList.sortedByDescending { it.timestamp }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }
    fun updateFilters(filters: List<String>) {
        _selectedFilters.value = filters
    }

    val restRelatedPosts: StateFlow<List<Post>> = repository.restRelatedPost
    fun fetchRestRelatedPosts(restId: Int) {
        viewModelScope.launch {
            repository.loadRestRelatedPosts(restId)
        }
    }
}