package com.tibame.foodhunter.zoe

import android.content.Context
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.zoe.PostRepository.postList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.InputStream

class PostViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()
    private val repository = PostRepository
    private val postFlow = repository.postList

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun updateFilters(filters: List<String>) {
        _selectedFilters.value = filters
    }
    fun getPersonalPosts(userId: String): StateFlow<List<Post>> {
        return postList.map { posts ->
            posts.filter { post -> post.publisher.id == userId }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun uriToBase64(context: Context, uri: Uri): String? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return inputStream?.readBytes()?.let {
            Base64.encodeToString(it, Base64.DEFAULT)
        }
    }

    private val _postCreateData = MutableStateFlow(PostCreateData())
    val postCreateData: StateFlow<PostCreateData> = _postCreateData.asStateFlow()
    fun setPostCreateData(data: PostCreateData){
        _postCreateData.update {
            data
        }
    }


    fun getPostById(postId: Int): StateFlow<Post?> {
        return postFlow.map { posts ->
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
            postFlow,
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