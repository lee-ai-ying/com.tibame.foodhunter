package com.tibame.foodhunter.sharon.viewmodel

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.data.NoteRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale


class NoteVM : ViewModel() {
    companion object {
        private const val TAG = "NoteVM"
        private const val SEARCH_DEBOUNCE_TIME = 300L  // 搜尋延遲時間
    }

    // 用於與後端通訊
    private val repository = NoteRepository.instance

    // 單一筆記狀態 - 用於顯示單一筆記的詳細資訊
    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    // 1. 所有筆記的完整列表
    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())
    // 2. 經過過濾後的筆記列表（這個會顯示給用戶看）
    private val _filteredNotes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> = _filteredNotes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _searchQuery = MutableStateFlow("")


    // 預留篩選相關狀態，目前未實作
    // private val _selectedContentTypes = MutableStateFlow<Set<CardContentType>>(emptySet())
    // val selectedContentTypes = _selectedContentTypes.asStateFlow()

    init {
        loadNotes()
        setupSearchFlow()  // 搜尋資料流
    }

    /**
     * 初始化筆記列表
     */
    fun loadNotes() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "開始載入筆記")
                _isLoading.value = true

                // 1. 觸發 Repository 發送 HTTP 請求
                Log.d(TAG, "呼叫 repository.getNotes()")
                repository.getNotes()

                // 2. 開始收集 Repository 的資料流
                Log.d(TAG, "開始收集 repository.notes")
                repository.notes.collect { notes ->
                    Log.d(TAG, "收到筆記資料: ${notes.size}筆")
                    // 3. 更新 ViewModel 的兩個狀態
                    _allNotes.value = notes      // 保存完整列表
                    Log.d(TAG,"設置到 _notes 的值: ${_allNotes.value}")

                    _filteredNotes.value = notes // 初始時顯示全部

                    _isLoading.value = false
                    Log.d(TAG, "設置 isLoading = false")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing notes", e)
            } finally {
                Log.d(TAG, "載入完成，設置 isLoading = false")
                _isLoading.value = false
            }
        }
    }

    /**
     * 設置搜尋流
     * 使用 Flow 處理搜尋邏輯，包含：
     * 1. 防抖（debounce）：避免過於頻繁的搜尋
     * 2. 即時過濾：搜尋條件改變時自動更新結果
     *  這裡使用 Flow 的特性自動處理狀態更新
     */
    @OptIn(FlowPreview::class)
    private fun setupSearchFlow() {
        viewModelScope.launch {
            _searchQuery
                .debounce(SEARCH_DEBOUNCE_TIME)  // 防抖，避免頻繁搜尋
                .collect { query ->
                    Log.d(TAG, "[setupSearchFlow] 搜尋關鍵字更新: $query")
                    updateSearchResults(query)
                }
        }
    }

    /**
     * 處理搜尋邏輯
     */
    fun searchNotes(query: String) {
        Log.d(TAG, "[searchNotes] 接收搜尋請求: $query")
        viewModelScope.launch {
            _searchQuery.value = query
            Log.d(TAG, "[searchNotes] 更新搜尋狀態")
        }
    }

    /**
     * 搜尋邏輯
     */
    private fun updateSearchResults(query: String) {
        Log.d(TAG, "[updateSearchResults] 開始更新搜尋結果")

        val allNotes = _allNotes.value

        // 套用搜尋文字
        val result = if (query.isEmpty()) {
            allNotes
        } else {
            allNotes.filter { note ->
                note.title.contains(query, ignoreCase = true)
            }
        }

        Log.d(TAG, "[updateSearchResults] 過濾後結果數量: ${result.size}")
        _filteredNotes.value = result
        Log.d(TAG, "_filteredNotes value: ${_filteredNotes.value}")

    }

    /**
     * 清空搜尋
     */
    fun resetSearch() {
        Log.d(TAG, "[clearSearch] 重置搜尋狀態")
        _searchQuery.value = ""
    }

    val hasSearchQuery: Boolean
        get() = _searchQuery.value.isNotEmpty()


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





    // 當用戶點選某個筆記時
    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getNoteById(noteId)
                // 更新選中的筆記
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
                    content = content,
                    imageResId = null,
                    restaurantName = null
                )

                val currentList = _allNotes.value.toMutableList()
                currentList.add(0, newNote)
                _allNotes.value = currentList
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
                        content = content,
                        type = type,
                        restaurantName = restaurantName
                    )
                    currentList[index] = updatedNote
                    _allNotes.value = currentList
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