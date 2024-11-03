package com.tibame.foodhunter.zoe

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.R
import com.tibame.foodhunter.global.CommonPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostRepository {
    private val serverUrl = "http://10.0.2.2:8080/com.tibame.foodhunter_server"
    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList.asStateFlow()
    private val gson = Gson()




    // 獲取貼文列表
    private suspend fun fetchPosts(): List<PostResponse> {
        val url = "${serverUrl}/post/preLoad"
        val result = CommonPost(url, "")
        val type = object : TypeToken<List<PostResponse>>() {}.type
        return gson.fromJson(result, type)
    }

    // 獲取餐廳資訊
    private suspend fun fetchRestaurant(restaurantId: Int): RestaurantResponse? {
        val url = "${serverUrl}/restaurant/$restaurantId"
        val result = CommonPost(url, "")
        return try {
            gson.fromJson(result, RestaurantResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // 獲取用戶資訊
    private suspend fun fetchUser(userId: Int): UserResponse? {
        val url = "${serverUrl}/user/$userId"
        val result = CommonPost(url, "")
        return try {
            gson.fromJson(result, UserResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // 將 PostResponse 轉換為 Post
    private suspend fun PostResponse.toPost(): Post {
        val restaurant = fetchRestaurant(this.restaurantId)
        val user = fetchUser(this.publisher)

        return Post(
            postId = this.postId,
            publisher = Publisher(
                id = this.publisher.toString(),
                name = this.publisherNickname ?: "Unknown User",
                avatarImage = R.drawable.user1,
                joinDate = user?.joinDate ?: ""
            ),
            content = this.content,
            location = this.restaurantName ?: "Unknown Location",
            timestamp = this.postTime,
            postTag = this.postTag,
            carouselItems = emptyList(),
            comments = emptyList(),
            isFavorited = false
        )
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
            _postList.value = posts
        } catch (e: Exception) {
            Log.e("PostRepository", "Error loading posts", e)
        }
    }

    // 創建新貼文
    suspend fun createPost(postData: PostCreateData): Boolean {
        val url = "${serverUrl}/post/create"
        val postRequest = CreatePostRequest(
            publisher = postData.publisher.toInt(),
            content = postData.content,
            postTag = postData.postTag,
            restaurantId = 0,  // 需要從 location 獲取或由使用者選擇
            visibility = 0
        )

        return try {
            val json = gson.toJson(postRequest)
            val result = CommonPost(url, json)
            true
        } catch (e: Exception) {
            Log.e("PostRepository", "Error creating post", e)
            false
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
    val restaurantName: String? // 新增餐廳名稱欄位
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



