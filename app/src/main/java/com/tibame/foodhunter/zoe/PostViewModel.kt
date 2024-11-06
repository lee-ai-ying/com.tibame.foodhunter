package com.tibame.foodhunter.zoe

import android.content.Context
import android.net.Uri
import android.util.Base64
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
 private val _inputData = MutableStateFlow(PostCreateData(
     publisher = 0,
     content = "",
     postTag = "",
     restaurantId = 0,
     photos = emptyList()
 ))
    val inputData: StateFlow<PostCreateData> = _inputData.asStateFlow()

    // 輔助狀態
    private val _selectedLocation = MutableStateFlow("")
    val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

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

    // PostCreateData 相關更新方法
    fun updateInputData(
        content: String? = null,
        postTag: String? = null,
        publisher: Int? = null,
        restaurantId: Int? = null
    ) {
        _inputData.update { currentData ->
            currentData.copy(
                content = content ?: currentData.content,
                postTag = postTag ?: currentData.postTag,
                publisher = publisher ?: currentData.publisher,
                restaurantId = restaurantId ?: currentData.restaurantId
            )
        }
    }
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun updateTags(selectedTags: Set<String>) {
        _inputData.update { it.copy(postTag = selectedTags.firstOrNull() ?: "") }
    }

    fun updateLocation(restaurantId: Int, restaurantName: String) {
        _inputData.update { it.copy(restaurantId = restaurantId) }
        _selectedLocation.value = restaurantName
    }

    fun updatePublisher(publisherId: Int) {
        _inputData.update { it.copy(publisher = publisherId) }
    }

    fun updatePhotos(context: Context, uris: List<Uri>) {
        viewModelScope.launch(Dispatchers.IO) {
            val base64Photos = uris.map { uri ->
                PostPhoto(imgBase64Str = context.uriToBase64(uri))
            }
            _inputData.update { it.copy(photos = base64Photos) }
        }
    }

    private fun Context.uriToBase64(uri: Uri): String {
        return contentResolver.openInputStream(uri)?.use { inputStream ->
            val bytes = inputStream.readBytes()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } ?: ""
    }

    // 發布貼文
    fun createPost(context: Context) {
        viewModelScope.launch {
            try {
                val success = repository.createPost(_inputData.value)
                if (success) { // 直接使用 boolean 回傳值
                    Toast.makeText(context, "貼文發布成功", Toast.LENGTH_SHORT).show()
                    // 重置輸入資料
                    _inputData.value = PostCreateData()
                    // 重新載入貼文列表
                    repository.loadPosts()
                } else {
                    Toast.makeText(context, "貼文發布失敗", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "發生錯誤：${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace() // 印出錯誤堆疊以便偵錯
            }
        }
    }





    init {
        viewModelScope.launch {
            repository.loadPosts()
        }
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
        return repository.postList.map { posts ->
            posts.find { it.postId == postId }
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun setPostId(postId: Int) {
        _selectedPostId.value = postId
    }


    fun createComment(postId: Int, userId: Int, content: String) {
        viewModelScope.launch {
            try {
                val success = repository.createComment(postId, userId, content)
                if (success) {
                    repository.loadPosts()
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
}




