package com.tibame.foodhunter.wei

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReviewVM : ViewModel() {
    // MutableStateFlow用來監控指定資料狀態，當資料一改變即可通知對應畫面更新
    // MutableStateFlow常與ViewModel搭配，可以讓UI元件在生命週期期間作出適當更新
//    private val _bookState = MutableStateFlow(emptyList<Book>())
//    val bookState: StateFlow<List<Book>> = _bookState.asStateFlow()
//    /** 新增一本書到List並更新_bookState內容 */
//    fun addItem(item: Book) {
//        _bookState.update {
//            val books = it.toMutableList()
//            books.add(item)
//            books
//        }
//    }
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
}