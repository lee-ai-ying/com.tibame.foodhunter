package com.tibame.foodhunter.zoe

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch




class PostEditorViewModel : ViewModel() {
    private val repository = PostRepository.getInstance()
    private val _inputData = MutableStateFlow(PostCreateData(
        publisher = 0,
        content = "",
        postTag = "",
        restaurantId = 0,
        photos = emptyList()
    ))
    val inputData: StateFlow<PostCreateData> = _inputData.asStateFlow()

    // 新增：編輯模式狀態
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    private val _editPostId = MutableStateFlow<Int?>(null)
    val editPostId: StateFlow<Int?> = _editPostId.asStateFlow()

    // 初始化編輯模式
    fun initializeEditor(postId: Int?) {
        viewModelScope.launch {
            if (postId != null) {
                _isEditMode.value = true
                _editPostId.value = postId
                // 載入現有貼文資料
                repository.getPost(postId)?.let { post ->
                    _inputData.update { currentData ->
                        currentData.copy(
                            content = post.content,
                            postTag = post.postTag,
                            restaurantId = post.location.toIntOrNull() ?: 0,
                            // 需要將現有照片轉換為 Base64 格式
                            photos = post.carouselItems.mapNotNull {
                                it.imageData?.let { bitmap ->
                                    PostPhoto(imgBase64Str = "") // 這裡需要實作 Bitmap 到 Base64 的轉換
                                }
                            }
                        )
                    }
                    _selectedLocation.value = post.location
                }
            } else {
                _isEditMode.value = false
                _editPostId.value = null
                // 重置為空白狀態
                resetInputData()
            }
        }
    }

    private fun resetInputData() {
        _inputData.value = PostCreateData(
            publisher = 0,
            content = "",
            postTag = "",
            restaurantId = 0,
            photos = emptyList()
        )
        _selectedLocation.value = ""
    }

    // 原有的方法保持不變
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

    fun updateTags(selectedTags: Set<String>) {
        _inputData.update { it.copy(postTag = selectedTags.firstOrNull() ?: "") }
    }

    fun updateLocation(restaurantId: Int, restaurantName: String) {
        _inputData.update { it.copy(restaurantId = restaurantId) }
        _selectedLocation.value = restaurantName
    }

    private val _selectedLocation = MutableStateFlow("")
    val selectedLocation: StateFlow<String> = _selectedLocation.asStateFlow()

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

    // 修改：統一的提交方法
    fun submitPost(context: Context) {
        viewModelScope.launch {
            try {
                val success = if (_isEditMode.value) {
                    // 編輯模式
                    _editPostId.value?.let { postId ->
                        repository.updatePost(postId, _inputData.value)
                    } ?: false
                } else {
                    // 新增模式
                    repository.createPost(_inputData.value)
                }

                if (success) {
                    val message = if (_isEditMode.value) "貼文更新成功" else "貼文發布成功"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    resetInputData()
                    repository.loadPosts()
                } else {
                    val message = if (_isEditMode.value) "貼文更新失敗" else "貼文發布成功"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "發生錯誤：${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}