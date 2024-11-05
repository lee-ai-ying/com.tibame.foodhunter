package com.tibame.foodhunter.sharon.viewmodel

import androidx.lifecycle.ViewModel
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.TabConstants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Personal Tools 的 UI 狀態
 */
data class PersonalToolsUiState(
    val selectedTabIndex: Int = TabConstants.CALENDAR,
    val tabList: List<Int> = listOf(
        R.string.str_calendar,
        R.string.str_note
    )
)

/**
 * TopBar 狀態
 */
data class TopBarState(
    val isSearchVisible: Boolean = false,    // 搜尋框是否顯示
    val searchQuery: String = "",            // 搜尋文字
    val isFilterChipVisible: Boolean = false // 篩選chips是否顯示
)

class PersonalToolsVM : ViewModel() {
    // UI 狀態
    private val _uiState = MutableStateFlow(PersonalToolsUiState())
    val uiState = _uiState.asStateFlow()

    // TopBar 狀態
    private val _topBarState = MutableStateFlow(TopBarState())
    val topBarState = _topBarState.asStateFlow()

    private val calendarVM = CalendarVM()
    val calendarState = calendarVM.uiState

    // Note ViewModel
    private val noteVM = NoteVM()
//    val noteState = noteVM.uiState

    /**
     * Tab 切換
     */
    fun updateSelectedTab(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
        // 切換Tab時重置TopBar狀態
        resetTopBarState()
    }

    /**
     * 搜尋框顯示切換
     */
    fun toggleSearchVisibility() {
        _topBarState.update { currentState ->
            currentState.copy(
                isSearchVisible = !currentState.isSearchVisible,
                // 關閉搜尋時重置所有狀態
                searchQuery = if (!currentState.isSearchVisible) "" else currentState.searchQuery,
            )
        }

        // 如果是關閉搜尋，通知當前頁面重置
        if (!_topBarState.value.isSearchVisible) {
            when (_uiState.value.selectedTabIndex) {
                TabConstants.CALENDAR -> calendarVM.resetSearch()
                TabConstants.NOTE -> noteVM.resetSearch()
            }
        }
    }

    /**
     * 更新搜尋文字
     */
    fun updateSearchQuery(query: String) {
        _topBarState.update { it.copy(searchQuery = query) }

        // 轉發搜尋請求給當前頁面
        when (_uiState.value.selectedTabIndex) {
            TabConstants.CALENDAR -> calendarVM.handleSearch(query)
            TabConstants.NOTE -> noteVM.handleSearch(query)
        }
    }

    /**
     * 切換篩選器顯示狀態
     */
    fun toggleFilterChipVisibility() {
        _topBarState.update { currentState ->
            currentState.copy(
                isFilterChipVisible = !currentState.isFilterChipVisible,
                isSearchVisible = true  // 顯示篩選時確保搜尋欄也顯示
            )
        }

        // 如果是關閉篩選，通知當前頁面重置篩選狀態
        if (!_topBarState.value.isFilterChipVisible) {
            when (_uiState.value.selectedTabIndex) {
                TabConstants.CALENDAR -> calendarVM.resetFilters()
                TabConstants.NOTE -> noteVM.resetFilters()
            }
        }
    }

    /**
     * 重置TopBar狀態
     */
    private fun resetTopBarState() {
        _topBarState.value = TopBarState()
    }
}