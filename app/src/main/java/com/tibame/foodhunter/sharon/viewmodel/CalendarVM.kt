package com.tibame.foodhunter.sharon.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.data.Book
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Group
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.util.CalendarDataSource
import com.tibame.foodhunter.sharon.util.CalendarUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth


class CalendarVM : ViewModel() {

    /**
     * 資料狀態
     * _allItems: 原始卡片列表（包含手札和揪團）
     * _filteredItems: 經過搜尋/篩選後的卡片列表
     */
    private val _allItems = MutableStateFlow<List<Any>>(emptyList())
    private val _filteredItems = MutableStateFlow<List<Any>>(emptyList())
    val filteredItems = _filteredItems.asStateFlow()

    /**
     * 過濾條件狀態
     * _selectedContentTypes: 目前選中的內容類型（可複選）
     * currentSearchQuery: 目前的搜尋文字
     */
    private val _selectedContentTypes = MutableStateFlow<Set<CardContentType>>(emptySet())
    val selectedContentTypes = _selectedContentTypes.asStateFlow()
    private var currentSearchQuery: String = ""

    /**
     * 處理搜尋
     * @param query 搜尋文字
     * 在當前顯示的卡片中搜尋符合 title 的項目
     */
    fun handleSearch(query: String) {
        currentSearchQuery = query
        applyFilters()
    }
    /**
     * 處理篩選
     * @param contentType 被點擊的內容類型
     * 切換內容類型的選中狀態，並重新過濾資料
     */
    fun handleFilter(contentType: CardContentType) {
        val newTypes = _selectedContentTypes.value.toMutableSet().apply {
            if (contentType in this) remove(contentType)
            else add(contentType)
        }
        _selectedContentTypes.value = newTypes
        applyFilters()
    }

    /**
     * 重置搜尋
     * 只清空搜尋文字，保留篩選狀態
     */
    fun resetSearch() {
        currentSearchQuery = ""
        applyFilters()  // 重新套用篩選，但搜尋文字為空
    }

    /**
     * 重置篩選
     * 只清空篩選條件，保留搜尋狀態
     */
    fun resetFilters() {
        _selectedContentTypes.value = emptySet()
        applyFilters()  // 重新套用搜尋，但沒有篩選條件
    }


    /**
     * 應用搜尋和篩選條件
     * 1. 先根據內容類型篩選
     * 2. 再根據搜尋文字過濾
     */
    private fun applyFilters() {
        var result = _allItems.value

        // 套用內容類型篩選
        if (_selectedContentTypes.value.isNotEmpty()) {
            result = result.filter { item ->
                when (item) {
                    is Note -> CardContentType.NOTE in _selectedContentTypes.value
                    is Group -> CardContentType.GROUP in _selectedContentTypes.value
                    else -> false
                }
            }
        }

        // 套用搜尋文字
        if (currentSearchQuery.isNotEmpty()) {
            result = result.filter { item ->
                when (item) {
                    is Note -> item.title.contains(currentSearchQuery, ignoreCase = true)
                    is Group -> item.title.contains(currentSearchQuery, ignoreCase = true)
                    else -> false
                }
            }
        }

        _filteredItems.value = result
    }



    /**
     * 狀態：載入中
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    /**
     * 測試資料
     */
//    private fun loadTestData() {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//
//                // 模擬測試資料
//                val testNotes = listOf(
//                    Note(
//                        noteId = 1,
//                        type = CardContentType.NOTE,
//                        date = "2024-03-05",
//                        day = "星期二",
//                        title = "新開幕咖啡廳",
//                        content = "環境很不錯，餐點也很精緻",
//                        restaurantName = "晴天咖啡",
//                        imageResId = R.drawable.breakfast_image_3
//                    ),
//                    Note(
//                        noteId = 2,
//                        type = CardContentType.NOTE,
//                        date = "2024-03-06",
//                        day = "星期三",
//                        title = "美式餐廳探索",
//                        content = "漢堡很juicy，薯條酥脆",
//                        restaurantName = "Big Burger",
//                        imageResId = R.drawable.image
//                    )
//                )
//
//                val testGroups = listOf(
//                    Group(
//                        id = 1,
//                        type = CardContentType.GROUP,
//                        location = "fdfdfdf",
//                        date = LocalDate.now(),
//                        title = "下午茶聚會",
//                        memberCount = 4,
//                        memberId = 1,
//                    )
//                )
//
//                // 合併資料
//                _allItems.value = (testNotes + testGroups)
//                _filteredItems.value = _allItems.value
//
//            } catch (e: Exception) {
//                // 處理錯誤
//                e.printStackTrace()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    /**
     * 資源清理
     */
    override fun onCleared() {
        super.onCleared()
        // 清理資源
    }

    private val dataSource by lazy { CalendarDataSource() }

    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    // 用於儲存所有書籍資料
    private var allBooks: List<Book> = emptyList()

    init {
        // 初始化日期列表
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    dates = updateCalendarDates(currentState.yearMonth)
                )
            }
        }
    }

    // 更新書籍數據的方法
    fun updateBooks(books: List<Book>) {
        allBooks = books
        viewModelScope.launch {
            // 使用當前月份來更新日期
            _uiState.update { currentState ->
                currentState.copy(
                    dates = updateCalendarDates(uiState.value.yearMonth)
                )
            }
        }
    }

    // 更新日期列表，根據月份和書籍資訊
    private fun updateCalendarDates(yearMonth: YearMonth): List<CalendarUiState.Date> {
        val dates = dataSource.getDates(yearMonth)

        return dates.map { date ->
            date.copy(
                hasBook = allBooks.any { book ->
                    book.date.year == date.year &&
                            book.date.monthValue == date.month &&
                            book.date.dayOfMonth.toString() == date.dayOfMonth
                }
            )
        }
    }

    // 切換到下一個月份
    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = nextMonth,
                    dates = updateCalendarDates(nextMonth)
                )
            }
        }
    }

    // 切換到上一個月份
    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = prevMonth,
                    dates = updateCalendarDates(prevMonth)
                )
            }
        }
    }

}



