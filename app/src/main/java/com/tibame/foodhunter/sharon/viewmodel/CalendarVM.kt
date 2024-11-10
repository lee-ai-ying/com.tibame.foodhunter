package com.tibame.foodhunter.sharon.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Group
import com.tibame.foodhunter.sharon.data.GroupRepository
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.data.NoteRepository
import com.tibame.foodhunter.sharon.util.CalendarDataSource
import com.tibame.foodhunter.sharon.util.CalendarUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date

//// 統一日曆項目介面
//interface CalendarItem {
//    val date: Date
//    val title: String
//    val type: CardContentType
//}

class CalendarVM : ViewModel() {
    companion object {
        private const val TAG = "CalendarVM"
        private const val SEARCH_DEBOUNCE_TIME = 300L  // 搜尋延遲時間
    }

    // 新增Repository
    private val noteRepository = NoteRepository.instance  // 手札Repository
    private val groupRepository = GroupRepository.instance // 揪團Repository

    /**
     * 資料狀態
     * _allItems: 原始卡片列表（包含手札和揪團）
     * _filteredItems: 經過搜尋/篩選後的卡片列表
     */
    private val _allItems = MutableStateFlow<List<Any>>(emptyList())  // Any 可以是 Note 或 Group
    private val _filteredItems = MutableStateFlow<List<Any>>(emptyList())
    val filteredItems = _filteredItems.asStateFlow()

    /**
     * 篩選器狀態
     * _selectedContentTypes: 目前選中的內容類型（可複選）
     */
    private val _selectedContentTypes = MutableStateFlow<Set<CardContentType>>(emptySet())
    val selectedContentTypes: StateFlow<Set<CardContentType>> = _selectedContentTypes.asStateFlow()

    // 狀態：載入中
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 搜尋狀態相關
    private val _searchQuery = MutableStateFlow("")


    private var memberId: Int? = null

    /**
     * 設置會員ID並載入資料
     * @param id 會員ID
     */
    fun setMemberId(id: Int) {
        memberId = id
        loadItems()
    }

    /**
     * 載入會員的所有項目（手札和揪團）
     */
    private fun loadItems() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "開始載入資料")
                _isLoading.value = true

                // 檢查memberId
                val currentMemberId = memberId
                if (currentMemberId == null) {
                    Log.e(TAG, "memberId為null，無法載入資料")
                    return@launch
                }

                // 1. 先載入手札資料
                noteRepository.getNotes(currentMemberId)
                val notes = noteRepository.notes.value

                /* 之後要加入揪團資料時:
                groupRepository.getGroups(currentMemberId)
                val groups = groupRepository.groups.value
                _allItems.value = notes + groups
                */

                // 2. 更新資料和視圖
                Log.d(TAG, "資料載入完成，共 ${notes.size} 筆")
                _allItems.value = notes
                applyFilters()
                updateCalendarView()

            } catch (e: Exception) {
                Log.e(TAG, "載入資料過程發生錯誤", e)
            } finally {
                _isLoading.value = false
                Log.d(TAG, "載入程序完成")
            }
        }
    }

    // 初始化時設置搜尋流
    init {
        setupSearchFlow()
    }

    // LaunchedEffect 會在 CalendarScreen 中呼叫這個方法
    fun initUserData(memberId: Int) {
        setMemberId(memberId)
        loadItems()  // 載入資料
    }

    /**
     * 接收搜尋請求
     */
    fun searchItems(query: String) {
        Log.d(TAG, "[searchNotes] 接收搜尋請求: $query")
        viewModelScope.launch {
            _searchQuery.value = query
            Log.d(TAG, "[searchNotes] 更新搜尋狀態")
        }
    }

    /**
     * 處理篩選請求
     * @param contentType 要切換的內容類型
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
     * 重置篩選
     * 只清空篩選條件，保留搜尋狀態
     */
    fun resetFilters() {
        _selectedContentTypes.value = emptySet()
        applyFilters()  // 重新套用搜尋，但沒有篩選條件
    }


    /**
     * 1. 搜尋流程設置
     * 使用 Flow 處理搜尋邏輯，包含：
     * 1.1. 防抖（debounce）：避免過於頻繁的搜尋
     * 1.2. 即時過濾：搜尋條件改變時自動更新結果
     *  這裡使用 Flow 的特性自動處理狀態更新
     */
    @OptIn(FlowPreview::class)
    private fun setupSearchFlow() {
        viewModelScope.launch {
            _searchQuery
                .debounce(SEARCH_DEBOUNCE_TIME)  // 防抖，避免頻繁搜尋
                .collect { query ->
                    Log.d(TAG, "[setupSearchFlow] 搜尋關鍵字更新: $query")
                    applyFilters()
                }
        }
    }

    /**
     * 應用搜尋和篩選條件
     * 這個函數負責將搜尋和篩選條件應用到原始資料上，產生最終顯示的結果
     * 處理順序：
     * 1. 取得原始資料
     * 2. 套用內容類型篩選（如果有選擇的類型）
     * 3. 套用搜尋文字（如果有輸入搜尋關鍵字）
     * 4. 更新顯示結果
     */
    private fun applyFilters() {
        // 步驟1: 從原始資料開始，建立一個可以修改的結果集合
        var result = _allItems.value

        // 步驟2: 處理內容類型篩選
        // 檢查是否有選擇的內容類型（Note或Group）
        if (_selectedContentTypes.value.isNotEmpty()) {
            result = result.filter { item ->
                when (item) {
                    is Note -> item.type in _selectedContentTypes.value
                    is Group -> item.type in _selectedContentTypes.value
                    else -> false
                }
            }
        }

        // 步驟3: 處理搜尋文字
        if (_searchQuery.value.isNotEmpty()) {
            // 使用 filter 根據筆記標題、揪團名稱進行過濾
            result = result.filter { item ->
                when (item) {
                    is Note -> item.title.contains(_searchQuery.value, ignoreCase = true)
                    is Group -> item.groupName.contains(_searchQuery.value, ignoreCase = true)
                    else -> false
                }
            }
            Log.d(TAG, "搜尋後的項目數量: ${result.size}")
        }

        // 步驟4: 更新最終的過濾結果
        _filteredItems.value = result
        Log.d(TAG, "最終顯示的項目數量: ${result.size}")
    }


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

    /**
     * 更新日曆視圖
     * 將過濾後的項目反映到日曆上
     */
    private fun updateCalendarView() {
        viewModelScope.launch {
            val filteredItems = _filteredItems.value

            // 使用當前月份來更新日期
            _uiState.update { currentState ->
                currentState.copy(
                    dates = updateCalendarDates(yearMonth = currentState.yearMonth,)
                )
            }
        }
    }


    // 更新日期列表，根據月份和書籍資訊
    private fun updateCalendarDates(yearMonth: YearMonth): List<CalendarUiState.Date> {
        val dates = dataSource.getDates(yearMonth)

        return dates.map { date ->
            // 找出該日期的所有項目
            val dateItems = _filteredItems.value.filter { item ->
                when (item) {
                    is Note -> {
                        val calendar = Calendar.getInstance().apply {
                            time = item.selectedDate
                        }
                        calendar.get(Calendar.YEAR) == date.year &&
                                (calendar.get(Calendar.MONTH) + 1) == date.month &&
                                calendar.get(Calendar.DAY_OF_MONTH).toString() == date.dayOfMonth
                    }
                    is Group -> false  // 之後加入揪團時再修改
                    else -> false
                }
            }

            date.copy(
                hasCard = dateItems.isNotEmpty(),  // 如果有項目就顯示指示點
                items = dateItems  // 存放該日期的所有項目
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



