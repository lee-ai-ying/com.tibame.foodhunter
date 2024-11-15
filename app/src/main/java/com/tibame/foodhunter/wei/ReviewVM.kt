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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ReviewVM : ViewModel() {

    // MutableStateFlow監控指定資料狀態，當資料一改變即可通知對應畫面更新
    // MutableStateFlow常與ViewModel搭配，可以讓UI元件在生命週期期間作出適當更新
    private val repository = ReviewRepository.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    // 當前顯示的評論列表狀態
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

    // 新增一個 StateFlow 來保存單一評論的狀態
    private val _currentReview = MutableStateFlow<Reviews?>(null)
    val currentReview: StateFlow<Reviews?> = _currentReview.asStateFlow()

    // 標籤相關的狀態
    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    val selectedTags: StateFlow<Set<String>> = _selectedTags.asStateFlow()

    // 保存原始評論列表
    private val _searchOriginalReviews = MutableStateFlow<List<Reviews>>(emptyList())


    // 定義篩選條件的類型
    enum class SortOrder {
        NEWEST,        // 最新評論
        MOST_LIKED,    // 讚數最多
        HIGHEST_RATING // 評分最高
    }



    private fun sortReviews() {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val sortedList = when (_sortOrder.value) {
            SortOrder.NEWEST -> _reviewState.value.sortedByDescending { review ->
                try {
                    LocalDateTime.parse(review.timestamp, dateTimeFormatter)
                } catch (e: Exception) {
                    Log.e("ReviewVM", "Error parsing date: ${review.timestamp}", e)
                    LocalDateTime.MIN
                }
            }
            SortOrder.MOST_LIKED -> _reviewState.value.also { reviewList ->
                Log.d("Sort", "Before sort thumbsup values: ${reviewList.map { it.thumbsup }}")
            }.sortedByDescending { it.thumbsup ?: 0 }.also { sortedList ->
                Log.d("Sort", "After sort thumbsup values: ${sortedList.map { it.thumbsup }}")
            }

            SortOrder.HIGHEST_RATING -> _reviewState.value.sortedByDescending { it.rating }
        }

        _reviewState.value = sortedList //每次切換排序方式時都會重新賦值
    }

    fun updateSortOrder(order: SortOrder) {
        viewModelScope.launch {
            _sortOrder.value = order
            sortReviews()
            // 強制重新發送狀態更新
            _reviewState.value = _reviewState.value
        }
    }

    // 更新排序方式
//    fun updateSortOrder(order: SortOrder) {
//        _sortOrder.update { order }
//        sortReviews()  // 更新排序
//    }





    /** 根據餐廳ID載入所有評論 */
    fun loadReviews(restaurantId: Int) {
        viewModelScope.launch {
            _isLoading.update { true }
            try {
                if (restaurantId <= 0) {
                    Log.e("ReviewVM", "Invalid restaurant ID: $restaurantId")
                    return@launch
                }
                Log.d("ReviewVM", "Loading reviews for restaurant ID: $restaurantId")
                val reviewsResponse = repository.fetchReviewByRestId(restaurantId)
                val mappedReviews = reviewsResponse.map { review ->
                    Reviews(
                        reviewId = review?.reviewId ?: 0,
                        reviewer = Reviewer(
                            id = review?.reviewer ?: 0,
                            name = review?.reviewerNickname.orEmpty(),
                        ),
                        restaurantId = review?.restaurantId ?: 0,
                        rating = review?.rating ?: 0,
                        content = review?.comments.orEmpty(),
                        timestamp = review?.reviewDate.orEmpty(),
                        thumbsup = review?.thumbsUp ?: 0,
                        thumbsdown = review?.thumbsDown ?: 0,
                        isLiked = false,
                        isDisliked = false,
                        replies = emptyList(),
                        maxPrice = review?.priceRangeMax ?: 0,
                        minPrice = review?.priceRangeMin ?: 0,
                        serviceCharge = review?.serviceCharge ?: 0
                    )
                }

                // 更新主要評論狀態
                _reviewState.value = mappedReviews
                // 同時更新搜尋用的原始列表
                _searchOriginalReviews.value = mappedReviews

                // 如果有搜尋關鍵字，立即進行過濾
                if (_searchKeyWord.value.isNotEmpty()) {
                    filterReviews()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading reviews for restaurant $restaurantId", e)
            }
            _isLoading.update { false }
        }
    }

    fun loadReviewById(reviewId: Int) {
        viewModelScope.launch {
            _isLoading.update { true }
            try {
                if (reviewId == null) {
                    // 處理 reviewId 為空的情況
                    _currentReview.value = null
                    return@launch
                }
                val reviewResponse = repository.fetchReviewById(reviewId)

                // 將 ReviewResponse 轉換為 Reviews 物件
                reviewResponse?.let { response ->
                    val review = Reviews(
                        reviewId = response.reviewId,
                        reviewer = Reviewer(
                            id = response.reviewer,
                            name = response.reviewerNickname
                        ),
                        restaurantId = response.restaurantId,
                        rating = response.rating,
                        content = response.comments,
                        timestamp = response.reviewDate,
                        thumbsup = response.thumbsUp,
                        thumbsdown = response.thumbsDown,
                        isLiked = false,
                        isDisliked = false,
                        replies = emptyList(),  // 稍後可以載入回覆
                        maxPrice = response.priceRangeMax,
                        minPrice = response.priceRangeMin,
                        serviceCharge = response.serviceCharge
                    )
                    _currentReview.value = review
                }
            } catch (e: Exception) {
                Log.e("ReviewVM", "Error loading review $reviewId", e)
                _currentReview.value = null

            }
            _isLoading.update { false }
        }
    }

    /** 更新搜尋關鍵字，並重新過濾評論 */
    fun updateSearchKeyword(keyword: String) {
        _searchKeyWord.value = keyword
        if (keyword.isEmpty()) {
            // 清空搜尋時，使用原始列表
            _reviewState.value = _searchOriginalReviews.value
        } else {
            filterReviews()
        }
    }

    /** 過濾評論（根據搜尋關鍵字） */
    private fun filterReviews() {
        val keyword = _searchKeyWord.value
        val filteredReviews = _searchOriginalReviews.value.filter { review ->
            review.content.contains(keyword, ignoreCase = true)
        }
        _reviewState.value = filteredReviews
    }

    /** 清空搜尋關鍵字 */
    fun resetSearch() {
        _searchKeyWord.value = ""
        _reviewState.value = _searchOriginalReviews.value
        Log.d("ReviewVM", "Search reset. Showing ${_reviewState.value.size} reviews")
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


    /** 新增評論 */
    fun createReview(item: Reviews) {
        _reviewState.update {
            val reviews = it.toMutableList()
            reviews.add(item)
            reviews
        }
    }


    /** 新增回覆 */
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


