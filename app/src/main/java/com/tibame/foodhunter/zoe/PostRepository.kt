
package com.tibame.foodhunter.zoe

// 原有的導入
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.R
import com.tibame.foodhunter.global.CommonPost
import com.tibame.foodhunter.global.serverUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class PostRepository {

    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList.asStateFlow()
    private val gson = Gson()

    private suspend fun fetchComments(postId: Int): List<CommentResponse> {
        val url = "${serverUrl}/comment/byPost?postId=$postId"
        val result = CommonPost(url, "")
        val type = object : TypeToken<List<CommentResponse>>() {}.type
        return try {
            gson.fromJson(result, type)
        } catch (e: Exception) {
            Log.e("PostRepository", "Error fetching comments", e)
            emptyList()
        }
    }

    // 獲取貼文列表
    private suspend fun fetchPosts(): List<PostResponse> {
        val url = "${serverUrl}/post/preLoad"
        val result = CommonPost(url, "")
        val type = object : TypeToken<List<PostResponse>>() {}.type
        return gson.fromJson(result, type)
    }

    private fun processBase64Image(photoData: String?, photoId: Int): ImageBitmap? {
        try {
            // 檢查 photoData
            if (photoData == null) {
                Log.e("PostRepository", "photoData is null for photo $photoId")
                return null
            }
            Log.d("PostRepository", "photoData found for photo $photoId")

            // 清理 base64 數據
            val cleanBase64 = photoData.let { base64Data ->
                Log.d(
                    "PostRepository",
                    "Original base64 length for photo $photoId: ${base64Data.length}"
                )
                val cleaned = base64Data
                    .replace("data:image/\\w+;base64,".toRegex(), "")
                    .replace("\\s".toRegex(), "")
                Log.d(
                    "PostRepository",
                    "Cleaned base64 length for photo $photoId: ${cleaned.length}"
                )
                cleaned
            }

            // Base64 解碼
            val decodedBytes = try {
                Log.d("PostRepository", "Attempting Base64 decode for photo $photoId")
                Base64.decode(cleanBase64, Base64.DEFAULT)
            } catch (e: IllegalArgumentException) {
                Log.e("PostRepository", "Base64 decode failed for photo $photoId", e)
                return null
            }
            Log.d(
                "PostRepository",
                "Base64 decode successful for photo $photoId, bytes length: ${decodedBytes.size}"
            )

            // 位圖轉換
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            if (bitmap == null) {
                Log.e("PostRepository", "Bitmap decode failed for photo $photoId")
                return null
            }
            Log.d(
                "PostRepository",
                "Bitmap created successfully for photo $photoId, size: ${bitmap.width}x${bitmap.height}"
            )

            return bitmap.asImageBitmap()

        } catch (e: Exception) {
            Log.e("PostRepository", "Error processing photo $photoId", e)
            e.printStackTrace()
            return null
        }
    }

    private suspend fun PostResponse.toPost(): Post {
        // 圖片處理
        val carouselItems = photos?.mapNotNull { photo ->
            val imageBitmap = processBase64Image(photo.photoFile, photo.postPhotoId)
            if (imageBitmap != null) {
                CarouselItem(
                    id = photo.postPhotoId,
                    imageData = imageBitmap,
                    order = 0
                )
            } else {
                null
            }
        } ?: emptyList()

        // 評論處理
        val comments = fetchComments(this.postId).map { commentResponse ->
            Comment(
                id = commentResponse.messageId,
                commenter = Commenter(
                    id = commentResponse.memberId,
                    name = commentResponse.memberNickname,
                    avatarImage = R.drawable.user1
                ),
                content = commentResponse.content,
                timestamp = commentResponse.messageTime
            )
        }

        return Post(
            postId = this.postId,
            publisher = Publisher(
                id = this.publisher,
                name = this.publisherNickname ?: "Unknown User",
                avatarImage = R.drawable.user1
            ),
            content = this.content,
            location = this.restaurantName ?: "Unknown Location",
            timestamp = this.postTime,
            postTag = this.postTag,
            carouselItems = carouselItems,
            comments = comments,  // 加入評論列表
            isFavorited = false
        )
    }
    fun Context.uriToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return if (bytes != null) {
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } else {
            ""
        }
    }




    suspend fun createPost(postData: PostCreateData): Boolean {
        val url = "${serverUrl}/post/create"

        return try {
            val json = gson.toJson(postData)
            Log.d("setPostCreateData", "Data: $json")
            val result = CommonPost(url, json)
            loadPosts()
            true
        } catch (e: Exception) {
            Log.e("PostRepository", "Error creating post", e)
            false
        }
    }
    suspend fun createComment(postId: Int, userId: Int, content: String): Boolean {
        val url = "${serverUrl}/comment/create"
        val commentRequest = mapOf(
            "postId" to postId,
            "memberId" to userId,
            "content" to content
        )
        return try {
            val json = gson.toJson(commentRequest)
            val result = CommonPost(url, json)
            loadPosts()
            true
        } catch (e: Exception) {
            Log.e("PostRepository", "Error creating comment", e)
            false
        }
    }

    // 載入所有貼文
    suspend fun loadPosts() {
        try {
            val postResponses = fetchPosts()
            val posts = postResponses.mapNotNull { response ->
                try {
                    response.toPost()
                } catch (e: Exception) {
                    null
                }
            }
            _postList.update { posts }

        } catch (e: Exception) {
            Log.e("PostRepository", "Error loading posts", e)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PostRepository? = null

        fun getInstance(): PostRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PostRepository().also { INSTANCE = it }
            }
        }
    }


    suspend fun deletePost(postId: Int): Boolean {
        val url = "${serverUrl}/post/delete"

        return try {
            val requestMap = mapOf("postId" to postId)
            val jsonString = gson.toJson(requestMap)

            val result = CommonPost(url, jsonString)
            val response = gson.fromJson(result, DeleteResponse::class.java)
            if (response.success) {
                true
            } else {
                Log.e("DeletePost", "Delete failed: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Log.e("DeletePost", "Error deleting post", e)
            false
        }
    }
}


    // 數據類別
    data class PostResponse(
        val postId: Int,
        val postTag: String,
        val publisher: Int,
        val content: String,
        val postTime: String,
        val visibility: Int,
        val restaurantId: Int,
        val likeCount: Int,
        val publisherNickname: String?,
        val restaurantName: String?,
        val photos: List<PhotoResponse>? = null  // 修改這裡來匹配後端返回的格式
    )


    data class PhotoResponse(
        val postPhotoId: Int,
        val postId: Int,
        val photoFile: String? // 用於Base64圖片數據
    )

    // 圖片數據類別
    data class CarouselItem(
        val id: Int,
        val imageData: ImageBitmap?, // 改為可為空的 ImageBitmap
        val order: Int,
        val contentDescription: String = "" // 可選的內容描述
    )




    data class RestaurantResponse(
        val restaurantId: Int,
        val name: String,
        val address: String
    )

    data class UserResponse(
        val userId: Int,
        val name: String,
        val avatar: String?,
        val joinDate: String
    )

    data class CreatePostRequest(
        val publisher: Int,
        val content: String,
        val postTag: String,
        val restaurantId: Int,
        val visibility: Int
    )

    data class ApiResponse<T>(
        val success: Boolean,
        val message: String,
        val data: T
    )
    data class DeleteResponse(
        val success: Boolean,
        val message: String
    )

    data class CommentResponse(
        val messageId: Int,
        val postId: Int,
        val memberId: Int,
        val content: String,
        val messageTime: String,
        val memberNickname: String
    )