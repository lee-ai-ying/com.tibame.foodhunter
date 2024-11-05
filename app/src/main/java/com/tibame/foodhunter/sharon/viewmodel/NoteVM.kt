package com.tibame.foodhunter.sharon.viewmodel

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Group
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale


class NoteVM : ViewModel() {
    /**
     * Note 核心狀態
     */
    private val repository = NoteRepository.instance
    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    /**
     * 筆記項目狀態
     */
    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())
    private val _filteredNotes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> = _filteredNotes.asStateFlow()

    /**
     * 過濾條件狀態
     * currentSearchQuery: 目前的搜尋文字
     */
    private var currentSearchQuery: String = ""

    // 預留篩選相關狀態，目前未實作
    // private val _selectedContentTypes = MutableStateFlow<Set<CardContentType>>(emptySet())
    // val selectedContentTypes = _selectedContentTypes.asStateFlow()

    init {
        initializeNotes()
    }

    /**
     * 初始化筆記列表
     */
    private fun initializeNotes() {
        viewModelScope.launch {
            try {
                repository.getNotes()
                repository.notes.collect { notes ->
                    _allNotes.value = notes
                    _filteredNotes.value = notes
                }
            } catch (e: Exception) {
                Log.e("NoteVM", "Error initializing notes", e)
            }
        }
    }

    /**
     * 處理搜尋邏輯
     * 由 PersonalToolsViewModel 調用
     */
    fun handleSearch(query: String) {
        currentSearchQuery = query
        filterNotes()
    }

    /**
     * 預留篩選方法，目前未實作
     */
    fun handleFilter(filter: CardContentType) {
        // 預留給未來實作篩選功能
    }

    /**
     * 預留重置篩選方法，目前未實作
     */
    fun resetFilters() {
        // 預留給未來實作篩選功能
    }

    /**
     * 重置搜尋和篩選狀態
     * 由 PersonalToolsViewModel 調用
     */
    fun resetSearch() {
        currentSearchQuery = ""
        _filteredNotes.value = _allNotes.value
    }

    /**
     * 筆記過濾邏輯
     * 目前只實作搜尋功能
     */
    private fun filterNotes() {
        var result = _allNotes.value

        // 套用搜尋文字
        if (currentSearchQuery.isNotEmpty()) {
            result = result.filter { note ->
                note.title.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        _filteredNotes.value = result
    }


    /**
     * 筆記核心功能
     */
    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getNoteById(noteId)
                _note.value = result
                Log.d("NoteVM", "Note received: $result")
            } catch (e: Exception) {
                Log.e("NoteVM", "Error getting note: ${e.message}")
            }
        }
    }

    /**
     * 新增筆記
     */
    fun addNote(title: String, content: String, type: CardContentType) {
        viewModelScope.launch {
            try {
                val newNote = Note(
                    noteId = System.currentTimeMillis().toInt(),
                    type = type,
                    date = getCurrentDate(),
                    day = getCurrentDay(),
                    title = title,
                    noteContent = content,
                    imageResId = null,
                    restaurantName = null
                )

                val currentList = _allNotes.value.toMutableList()
                currentList.add(0, newNote)
                _allNotes.value = currentList
                filterNotes()
            } catch (e: Exception) {
                Log.e("NoteVM", "Error adding note", e)
            }
        }
    }

    /**
     * 更新筆記
     */
    fun updateNote(
        noteId: Int,
        title: String,
        content: String,
        type: CardContentType,
        restaurantName: String? = null
    ) {
        viewModelScope.launch {
            try {
                val currentList = _allNotes.value.toMutableList()
                val index = currentList.indexOfFirst { it.noteId == noteId }

                if (index != -1) {
                    val oldNote = currentList[index]
                    val updatedNote = oldNote.copy(
                        title = title,
                        noteContent = content,
                        type = type,
                        restaurantName = restaurantName
                    )
                    currentList[index] = updatedNote
                    _allNotes.value = currentList
                    filterNotes()
                }
            } catch (e: Exception) {
                Log.e("NoteVM", "Error updating note", e)
            }
        }
    }

    /**
     * 刪除筆記
     */
    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            try {
                val currentList = _allNotes.value.toMutableList()
                currentList.removeAll { it.noteId == noteId }
                _allNotes.value = currentList
                filterNotes()
            } catch (e: Exception) {
                Log.e("NoteVM", "Error deleting note", e)
            }
        }
    }

    /**
     * 工具函數
     */
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentDay(): String {
        val sdf = SimpleDateFormat("EEEE", Locale.CHINESE)
        return "星期" + sdf.format(Date()).substring(2, 3)
    }

    /**
     * 資源清理
     */
    override fun onCleared() {
        super.onCleared()
        // 清理資源
    }
}