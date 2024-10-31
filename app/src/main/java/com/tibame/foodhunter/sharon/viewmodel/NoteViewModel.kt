package com.tibame.foodhunter.sharon.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteViewModel : ViewModel() {
    // 搜尋相關
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 篩選相關
    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()

    fun handleSearch(query: String) {
        _searchQuery.value = query
        // 實作搜尋邏輯
    }

    fun handleFilter(filters: List<String>) {
        _selectedFilters.value = filters
        // 實作篩選邏輯
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun clearFilters() {
        _selectedFilters.value = emptyList()
    }
}