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
    private val personalPostsCache = mutableMapOf<Int, StateFlow<List<Post>>>()
    private val repository = PostRepository.getInstance()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    init {
        viewModelScope.launch {
            repository.loadPosts()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateFilters(filters: List<String>) {
        _selectedFilters.value = filters
    }


    // 刪除貼文的方法
    suspend fun deletePost(postId: Int): Boolean {
        return try {
            val success = repository.deletePost(postId)
            if (success) {
                repository.loadPosts()
            }
            success
        } catch (e: Exception) {
            false
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


    private val _postCreateData = MutableStateFlow(PostCreateData())
    val postCreateData: StateFlow<PostCreateData> = _postCreateData.asStateFlow()

    fun setPostCreateData(data: PostCreateData) {
        _postCreateData.update { data }
    }

    fun getPostById(postId: Int): StateFlow<Post?> {
        return repository.postList.map { posts ->
            posts.find { it.postId == postId }
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    private val _selectedPostId = MutableStateFlow<Int?>(null)
    val selectedPostId = _selectedPostId.asStateFlow()

    fun setPostId(postId: Int) {
        _selectedPostId.value = postId
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

            filteredList.sortedByDescending { post ->
                post.timestamp
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun createPost(postData: PostCreateData) {
        viewModelScope.launch {
            val success = repository.createPost(postData)
            if (success) {
                repository.loadPosts()
            }
        }
    }


        fun createComment(postId: Int, userId: Int, content: String) {
            viewModelScope.launch {
                try {
                    val success = repository.createComment(postId, userId, content)
                    if (success) {
                        // 留言成功後重新載入貼文
                        repository.loadPosts()
                    }
                } catch (e: Exception) {
                    // 處理錯誤
                    e.printStackTrace()
                }
            }
        }
    }







