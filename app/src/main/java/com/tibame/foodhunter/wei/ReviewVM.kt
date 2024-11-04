package com.tibame.foodhunter.wei

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.zoe.Post
import com.tibame.foodhunter.zoe.PostCreateData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ReviewVM : ViewModel() {
    // MutableStateFlow用來監控指定資料狀態，當資料一改變即可通知對應畫面更新
    // MutableStateFlow常與ViewModel搭配，可以讓UI元件在生命週期期間作出適當更新
    private val _reviewState = MutableStateFlow(emptyList<Reviews>())
    val reviewState: StateFlow<List<Reviews>> = _reviewState.asStateFlow()

    /** 新增一則評論到List並更新_reviewState內容 */
    fun addItem(item: Reviews) {
        _reviewState.update {
            val reviews = it.toMutableList()
            reviews.add(item)
            reviews
        }
    }


    private val _reviewCreateData = MutableStateFlow(ReviewCreateData())
    val reviewCreateData: StateFlow<ReviewCreateData> = _reviewCreateData.asStateFlow()
    fun setReviewCreateData(data: ReviewCreateData){
        _reviewCreateData.update {
            data
        }
    }

    // 搜尋關鍵字
    private val _searchKeyWord = MutableStateFlow("")
    val searchKeyWord: StateFlow<String> = _searchKeyWord.asStateFlow()

//
//    /** 移除一本書並更新_bookState內容 */
//    fun removeItem(item: Book) {
//        _bookState.update {
//            val books = it.toMutableList()
//            books.remove(item)
//            books
//        }
//    }

    //改為篩選器項目
//    fun getFilteredPosts(): StateFlow<List<Post>> {
//        return combine(
//            postFlow,
//            selectedFilters,
//            searchQuery
//        ) { posts, filters, query ->
//            var filteredList = posts
//
//            // 應用標籤過濾
//            if (filters.isNotEmpty()) {
//                filteredList = filteredList.filter { post ->
//                    filters.contains(post.postTag)
//                }
//            }
//
//            // 應用搜尋過濾
//            if (query.isNotEmpty()) {
//                filteredList = filteredList.filter { post ->
//                    post.content.contains(query, ignoreCase = true) ||
//                            post.location.contains(query, ignoreCase = true) ||
//                            post.publisher.name.contains(query, ignoreCase = true)
//                }
//            }
//
//            // 根據時間排序（最新的優先）
//            filteredList.sortedByDescending { post ->
//                post.timestamp
//            }
//        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
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
    companion object {
    }
}