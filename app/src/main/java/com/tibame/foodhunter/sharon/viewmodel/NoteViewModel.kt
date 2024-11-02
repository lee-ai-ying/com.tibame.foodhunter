package com.tibame.foodhunter.sharon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NoteViewModel : ViewModel() {
    // 搜尋相關
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 篩選相關
    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters = _selectedFilters.asStateFlow()

    // repository 保持私有，因為它只在 ViewModel 內部使用
    private val repository = NoteRepository
    // noteList 改名並設為公開，因為 UI 需要直接觀察這個列表
    val noteList = repository.noteList

    // 根據 ID 查找筆記
    // getNoteById 保持公開，因為是給 UI (NoteEdit) 使用的方法
    fun getNoteById(noteId: Int): StateFlow<Note?> {
        return noteList.map { notes ->
            notes.find { it.noteId == noteId }  // 在列表中查找匹配的筆記
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

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