package com.tibame.foodhunter.wei

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.global.CommonPost
import com.tibame.foodhunter.global.serverUrl
import com.tibame.foodhunter.zoe.DeleteResponse
import com.tibame.foodhunter.zoe.PostResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReviewRepository {


    private val _reviewList = MutableStateFlow<List<Review>>(emptyList())
    val reviewList: StateFlow<List<Review>> = _reviewList.asStateFlow()

    private val _replyList = MutableStateFlow<List<Reply>>(emptyList())
    val replyList: StateFlow<List<Reply>> = _replyList.asStateFlow()

    private val gson = Gson()

    // 根據評論 ID 獲取該評論的回覆列表
    private suspend fun fetchReplies(reviewId: Int): List<ReplyResponse> {
        val url = "${serverUrl}/reply/byReview?reviewId=$reviewId"
        val result = CommonPost(url, "")
        val type = object : TypeToken<List<ReplyResponse>>() {}.type
        return try {
            gson.fromJson(result, type)
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Error fetching replies", e)
            emptyList()
        }
    }

    // 將回覆轉換為應用程式內的 Reply 物件
    private suspend fun ReplyResponse.toReply(): Reply {
        return Reply(
            id = this.replyId,
            replier = Replier(
                id = this.memberId,
                name = this.memberNickname,
                avatarImage = null // 若有頭像處理方式，將其放入這裡
            ),
            content = this.content,
            timestamp = this.replyTime
        )
    }

    // 根據餐廳ID獲取評論列表
    suspend fun fetchReviewByRestId(restaurantId: Int): List<ReviewResponse?> {
        val url = "${serverUrl}/review/preLoadController"
        // 加入請求前的日誌
        Log.d("ReviewRepository", "Fetching reviews for restaurant ID: $restaurantId")
        Log.d("ReviewRepository", "Request URL: $url")
        val jsonObject = JsonObject()
        jsonObject.addProperty("restaurantId", restaurantId)
        val result = CommonPost(url, jsonObject.toString())
        // 加入接收到回應的日誌
        Log.d("ReviewRepository", "Received response: $result")
        val type = object : TypeToken<List<ReviewResponse>>() {}.type
        return try {
            val reviews = gson.fromJson<List<ReviewResponse>>(result, type)
            Log.d("Repository", "Parsed reviews: ${reviews?.size}")
            reviews?.forEach {
                Log.d("Repository", "Review: $it")
            }
            reviews ?: emptyList()
            gson.fromJson(result, type)
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Error fetching reviews for restaurant $restaurantId", e)
            emptyList() // 若發生錯誤，返回空列表
        }
    }


    // 根據評論ID 獲取評論的詳細資料
    private suspend fun fetchReviewById(reviewId: Int): ReviewResponse? {
        val url = "${serverUrl}/review/get?reviewId=$reviewId"
        Log.d("ReviewRepository", "Fetching reviews for review ID: $reviewId")
        Log.d("ReviewRepository", "Request URL: $url")
        val result = CommonPost(url, "")

        return try {
            val response = gson.fromJson(result, ApiResponse::class.java)
            if (response.success) {
                gson.fromJson(gson.toJson(response.data), ReviewResponse::class.java)
            } else {
                Log.e("ReviewRepository", "Error fetching review: ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Error fetching review", e)
            null
        }
    }

    // 創建新回覆
    suspend fun createReply(reviewId: Int, userId: Int, content: String): Boolean {
        val url = "${serverUrl}/reply/create"
        val replyRequest = mapOf(
            "reviewId" to reviewId,
            "memberId" to userId,
            "content" to content
        )

        return try {
            val json = gson.toJson(replyRequest)
            val result = CommonPost(url, json)
            loadReplies(reviewId) // 重新載入回覆列表
            true
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Error creating reply", e)
            false
        }
    }


 //載入所有回覆
    suspend fun loadReplies(reviewId: Int) {
        try {
            val replyResponses = fetchReplies(reviewId)
            val replies = replyResponses.mapNotNull { response ->
                try {
                    response.toReply()
                } catch (e: Exception) {
                    null
                }
            }

            // 更新回覆列表
            _replyList.update { replies }
        } catch (e: Exception) {
            Log.e("ReviewRepository", "Error loading replies", e)
        }
    }

//    同時載入回覆及評論
//    suspend fun loadReviewsWithReplies(restaurantId: Int): List<Review> {
//        // 1. 獲取餐廳的評論列表
//        val reviews = fetchReviewByRestId(restaurantId).mapNotNull { it?.toReview() }
//
//        // 2. 並行載入每則評論的回覆
//        val reviewsWithReplies = reviews.map { review ->
//            val replies = fetchReplies(review.reviewId).map { it.toReply() }
//            review.copy(replies = replies)  // 更新評論物件中的回覆列表
//        }
//
//        // 更新 _reviewList 狀態，供 ViewModel 監聽使用
//        _reviewList.update { reviewsWithReplies }
//
//        return reviewsWithReplies
//    }


//    // 刪除回覆
//    suspend fun deleteReply(replyId: Int): Boolean {
//        val url = "${serverUrl}/reply/delete/$replyId"
//        return try {
//            val result = CommonPost(url, "")
//            val response = gson.fromJson(result, DeleteResponse::class.java)
//            if (response.success) {
//                // 刪除成功後更新本地回覆列表
//                _reviewList.value = _reviewList.value.filter { it.id != replyId }
//                true
//            } else {
//                Log.e("ReviewRepository", "Delete failed: ${response.message}")
//                false
//            }
//        } catch (e: Exception) {
//            Log.e("ReviewRepository", "Error deleting reply", e)
//            false
//        }
//    }

    companion object {
        @Volatile
        private var INSTANCE: ReviewRepository? = null

        fun getInstance(): ReviewRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ReviewRepository().also { INSTANCE = it }
            }
        }
    }

    // 數據類別
    data class ReviewResponse(
        val reviewId: Int,
        val content: String,
        val rating: Int,
        val reviewTime: String,
        val userId: Int,
        val userNickname: String
    )

    data class ReplyResponse(
        val replyId: Int,
        val reviewId: Int,
        val memberId: Int,
        val content: String,
        val replyTime: String,
        val memberNickname: String
    )

    data class ApiResponse<T>(
        val success: Boolean,
        val message: String,
        val data: T
    )

//    data class ReviewResponse(
//        val reviewId: Int,
//        val content: String,
//        val rating: Int,
//        val reviewTime: String,
//        val userId: Int,
//        val userNickname: String
//    ) {
//        fun toReview(): Review {
//            return Review(
//                reviewId = reviewId,
//                content = content,
//                rating = rating,
//                timestamp = reviewTime,
//                reviewer = Reviewer(userId, userNickname, avatarImage = null),
//                replies = emptyList() // 回覆列表稍後加載
//            )
//        }
//    }
//
//    data class ReplyResponse(
//        val replyId: Int,
//        val reviewId: Int,
//        val memberId: Int,
//        val content: String,
//        val replyTime: String,
//        val memberNickname: String
//    ) {
//        fun toReply(): Reply {
//            return Reply(
//                id = replyId,
//                content = content,
//                timestamp = replyTime,
//                replier = Replier(memberId, memberNickname, avatarImage = null)
//            )
//        }
//    }

//    data class DeleteResponse(
//        val success: Boolean,
//        val message: String)
}

//抓會員ID用
//val membernameState = remember { mutableStateOf(0) }
//LaunchedEffect(Unit) {
//    coroutineScope.launch {
//        val user = userVM.getUserInfo(userVM.username.value)
//        //圖片
//    val user1 = userVM.image(userVM.username.value)}
//        if (user != null) {
//            membernameState.value = user.id
//            //圖片
//            user1?.profileImageBase64?.let { base64 ->
//                profileBitmap = userVM.decodeBase64ToBitmap(base64)
//        }
//    }
//}