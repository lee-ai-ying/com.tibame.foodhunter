package com.tibame.foodhunter.sharon.viewmodel

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.tibame.foodhunter.R
import com.tibame.foodhunter.sharon.TabConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TabRowToolsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TabRowUiState())
    val uiState: StateFlow<TabRowUiState> = _uiState.asStateFlow()

    fun updateSelectedTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedTabIndex = index)
        }
    }
}

// UiState.kt
data class TabRowUiState(
    val selectedTabIndex: Int = TabConstants.CALENDAR,
    val tabList: List<Int> = listOf(
        R.string.str_calendar,
        R.string.str_note
    )
)