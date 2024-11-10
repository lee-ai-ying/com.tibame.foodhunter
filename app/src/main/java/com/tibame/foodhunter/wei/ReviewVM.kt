package com.tibame.foodhunter.wei

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewVM : ViewModel() {

    // MutableStateFlow用來監控指定資料狀態，當資料一改變即可通知對應畫面更新
    // MutableStateFlow常與ViewModel搭配，可以讓UI元件在生命週期期間作出適當更新
    private val repository = ReviewRepository.getInstance()

    // 用來監控評論列表資料狀態
    private val _reviewState = MutableStateFlow<List<Reviews>>(emptyList())
    val reviewState: StateFlow<List<Reviews>> = _reviewState.asStateFlow()

    // 用來監控評論搜尋關鍵字
    private val _searchKeyWord = MutableStateFlow("")
    val searchKeyWord: StateFlow<String> = _searchKeyWord.asStateFlow()

    // 用來監控回覆列表資料狀態
    private val _replyState = MutableStateFlow<List<Reply>>(emptyList())
    val replyState: StateFlow<List<Reply>> = _replyState.asStateFlow()

    // 用來監控新增評論的資料狀態
    private val _reviewCreateData = MutableStateFlow(ReviewCreateData())
    val reviewCreateData: StateFlow<ReviewCreateData> = _reviewCreateData.asStateFlow()

    // 目前的篩選條件，預設為最新
    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()


    fun setRestaurantId(id:Int){
        loadReviews(id)  // 預設載入評論資料
    }

    // 根據選擇的排序方式更新評論列表
    private fun sortReviews() {
        _reviewState.update { reviews ->
            when (_sortOrder.value) {
                SortOrder.NEWEST -> reviews.sortedByDescending { it.timestamp } // 根據時間排序
                SortOrder.MOST_LIKED -> reviews.sortedByDescending { it.isLiked } // 根據讚數排序
                SortOrder.HIGHEST_RATING -> reviews.sortedByDescending { it.rating } // 根據評分排序
            }
        }
    }

    // 更新排序方式
    fun updateSortOrder(order: SortOrder) {
        _sortOrder.update { order }
        sortReviews()  // 更新排序
    }

    /** 根據餐廳ID載入所有評論 */
    fun loadReviews(restaurantId: Int) {
        viewModelScope.launch {
            try {
                val reviewsResponse = repository.fetchReviewByRestId(restaurantId)
                _reviewState.value = reviewsResponse.map { review ->
                    Reviews(
                        reviewId = review?.reviewId ?: 0,
                        reviewer = Reviewer(
                            id = review?.userId ?: 0,
                            name = review?.userNickname.orEmpty(),
                            avatarImage = null,  // 若有頭像可設定
                            followers = 0,  // 可加入追蹤者人數
                            following = 0   // 可加入追蹤中人數
                        ),
                        restaurantId = 0,  // 假設評論的餐廳ID會在這裡填充
                        rating = review?.rating ?: 0,
                        content = review?.content.orEmpty(),
                        timestamp = review?.reviewTime.orEmpty(),
                        isLiked = false,  // 可以根據需要初始化
                        isDisliked = false,  // 可以根據需要初始化
                        replies = emptyList(),  // 初始回覆列表為空，稍後載入
                        maxPrice = 0,  // 假設有價格範圍
                        minPrice = 0,  // 假設有價格範圍
                        serviceCharge = 0  // 可以根據需求設置
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading reviews for restaurant $restaurantId", e)
            }
        }
    }

    /** 根據評論ID載入該評論的回覆 */
    fun loadRepliesOfReview(reviewId: Int) {
        viewModelScope.launch {
            try {
                repository.loadReplies(reviewId)  // 透過Repository載入回覆
                val replies = repository.replyList.value  // 取得回覆資料
                _replyState.value = replies
            } catch (e: Exception) {
                Log.e(TAG, "Error loading replies", e)
            }
        }
    }

    /** 添加一個新的評論 */
    fun addReview(item: Reviews) {
        _reviewState.update {
            val reviews = it.toMutableList()
            reviews.add(item)
            reviews
        }
    }

    /** 更新搜尋關鍵字，並重新過濾評論 */
    fun updateSearchKeyword(keyword: String) {
        _searchKeyWord.update { keyword }
        filterReviews()  // 更新後重新過濾評論
    }

    /** 過濾評論（根據搜尋關鍵字） */
    private fun filterReviews() {
        val keyword = _searchKeyWord.value
        _reviewState.update { currentReviews ->
            currentReviews.filter { review ->
                review.content.contains(keyword, ignoreCase = true)
            }
        }
    }

    /** 創建回覆 */
    fun createReply(reviewId: Int, userId: Int, content: String) {
        viewModelScope.launch {
            try {
                val success = repository.createReply(reviewId, userId, content)
                if (success) {
                    loadRepliesOfReview(reviewId)  // 成功創建回覆後重新載入回覆列表
                }
            } catch (e: Exception) {
                Log.e(TAG, " Reply crate fail", e)
            }
        }
    }

    /** 設置創建評論資料 */
    fun setReviewCreateData(data: ReviewCreateData) {
        _reviewCreateData.update { data }
    }

    /** 清空搜尋關鍵字 */
    fun resetSearch() {
        _searchKeyWord.update { "" }
        filterReviews()  // 清空搜尋後重新過濾評論
    }
}

// 定義篩選條件的類型
enum class SortOrder {
    NEWEST,        // 最新評論
    MOST_LIKED,    // 讚數最多
    HIGHEST_RATING // 評分最高
}

//
//    /** 移除一本書並更新_bookState內容 */
//    fun removeItem(item: Book) {
//        _bookState.update {
//            val books = it.toMutableList()
//            books.remove(item)
//            books
//        }
//    }




//    @Composable
//    fun GetReviews() {
//        val reviews = listOf(
//            Review("使用者名稱1", 4),
//            Review("使用者名稱2", 5),
//            Review("使用者名稱3", 3),
//            Review("使用者名稱4", 2),
//            Review("使用者名稱5", 1)
//        )
//
//        LazyColumn {
//            items(reviews) { review ->
//                ReviewItem(review)
//                Spacer(modifier = Modifier.size(10.dp)) // 每筆評論間的間距
//            }
//        }
//    }
//    companion object {
//    }


