package com.tibame.foodhunter.a871208s

import androidx.lifecycle.ViewModel
import com.tibame.foodhunter.R
import com.tibame.foodhunter.ai_ying.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FriendViewModel: ViewModel() {
    // MutableStateFlow用來監控指定資料狀態，當資料一改變即可通知對應畫面更新
    // MutableStateFlow常與ViewModel搭配，可以讓UI元件在生命週期期間作出適當更新
    private val _FriendState = MutableStateFlow(emptyList<Friend>())
    val FriendState: StateFlow<List<Friend>> = _FriendState.asStateFlow()
    // 一開始就呼叫fetchBooks()取得測試資料以更新_bookState內容
    init {
        _FriendState.update { fetchFriends() }
    }

    /** 新增一本書到List並更新_bookState內容 */
    fun addItem(item: Friend) {
        _FriendState.update {
            val books = it.toMutableList()
            books.add(item)
            books
        }
    }

    /** 移除一本書並更新_bookState內容 */
    fun removeItem(item: Friend) {
        _FriendState.update {
            val books = it.toMutableList()
            books.remove(item)
            books
        }
    }
    fun fetchFriends(): List<Friend> {
        return listOf(
            Friend("0001", "Ivy", R.drawable.image),
            Friend("0002", "Mary", R.drawable.account_circle),
            Friend("0003", "Sue", R.drawable.account_circle),
            Friend("0004", "Sue", R.drawable.account_circle),
        )
    }
}