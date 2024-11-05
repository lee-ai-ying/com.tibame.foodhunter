package com.tibame.foodhunter.sharon.viewmodel

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.data.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

/**
 * 筆記相關的ViewModel，繼承TopBarViewModel以使用搜尋相關功能
 */
class NoteViewModel : TopBarViewModel() {
    // 用於存放單一筆記查詢結果
    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    // 查詢單一筆記
    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getNoteById(noteId)
                _note.value = result
                Log.d("NoteViewModel", "Note received: $result")
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Error getting note: ${e.message}")
            }
        }
    }


    // 儲存選中的篩選條件 (手札/揪團)
    private val _selectedFilters = MutableStateFlow<List<String>>(emptyList())
    val selectedFilters: StateFlow<List<String>> = _selectedFilters.asStateFlow()

    // 儲存所有筆記項目（原始數據）
    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())

    // 取得Repository實例
    private val repository = NoteRepository.instance

    // 用於顯示的筆記項目（過濾後的結果）
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        // 初始化測試資料
        getNotes()
    }

    /**
     * 從Repository獲取筆記列表
     * 並更新UI狀態
     */
    private fun getNotes() {
        viewModelScope.launch {
            try {
                // 先取得資料
                repository.getNotes()
                // 監聽資料變化
                repository.notes.collect { notes ->
                    _notes.value = notes
                }
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Error getting notes", e)
                // TODO: 處理錯誤狀態
            }
        }
    }


    // 重寫 toggleSearchVisibility 來處理搜尋框關閉時的重置邏輯
    override fun toggleSearchVisibility() {
        super.toggleSearchVisibility()
        if (!_isSearchVisible.value) {
            // 如果關閉搜尋，重置所有狀態
            _isFilterChipVisible.value = false
            _selectedFilters.value = emptyList()
            _notes.value = _allNotes.value
        }
    }

    // 更新選擇的篩選條件
    fun updateSelectedFilter(filter: String) {
        val currentFilters = _selectedFilters.value.toMutableList()
        if (currentFilters.contains(filter)) {
            currentFilters.remove(filter)
        } else {
            currentFilters.add(filter)
        }
        _selectedFilters.value = currentFilters
        // 更新筆記項目顯示
        filterItems()
    }

    // 處理搜尋文字變更
    override fun updateSearchQuery(newQuery: String) {
        super.updateSearchQuery(newQuery)
        filterItems()
    }

    /**
     * 處理搜尋和篩選邏輯
     */
    private fun filterItems() {
        val query = _searchQuery.value
        val filters = _selectedFilters.value

        viewModelScope.launch {
            var filteredNotes = _allNotes.value

            // 根據搜尋關鍵字過濾
            if (query.isNotEmpty()) {
                filteredNotes = filteredNotes.filter { note ->
                    note.title.contains(query, ignoreCase = true) ||
                            note.noteContent.contains(query, ignoreCase = true) ||
                            note.restaurantName?.contains(query, ignoreCase = true) == true
                }
            }

            // 根據篩選條件過濾
            if (filters.isNotEmpty()) {
                filteredNotes = filteredNotes.filter { note ->
                    when (note.type) {
                        CardContentType.GROUP -> filters.contains("揪團")
                        CardContentType.NOTE -> filters.contains("手札")
                    }
                }
            }

            _notes.value = filteredNotes
        }
    }

    /**
     * 根據ID獲取單一筆記
     * 先檢查本地快取，若無則從API獲取
     */
//    fun getNoteById(noteId: Int): Flow<Note?> {
//        return flow {
//            try {
//                // 先查找本地快取
//                val cachedNote = _allNotes.value.find { it.noteId == noteId }
//                if (cachedNote != null) {
//                    emit(cachedNote)
//                } else {
//                    // 如果快取沒有，從API獲取
//                    val note = repository.getNoteById(noteId)
//                    emit(note)
//                }
//            } catch (e: Exception) {
//                Log.e("NoteViewModel", "Error getting note: ${e.message}")
//                emit(null)
//            }
//        }
//    }

    /**
     * 新增筆記
     * TODO: 之後會改為呼叫 API 新增筆記
     * @param title 筆記標題
     * @param content 筆記內容
     * @param type 筆記類型 (GROUP/NOTE)
     */
    fun addNote(title: String, content: String, type: CardContentType) {
        viewModelScope.launch {
            val newNote = Note(
                noteId = System.currentTimeMillis().toInt(), // 暫時用時間戳當 ID
                type = type,
                date = getCurrentDate(), // 可以自定義日期格式
                day = getCurrentDay(),    // 可以自定義星期格式
                title = title,
                noteContent = content,
                // 選填欄位可以根據需求加入
                imageResId = null,
                restaurantName = null
            )

            val currentList = _allNotes.value.toMutableList()
            currentList.add(0, newNote) // 新筆記加到最前面
            _allNotes.value = currentList

            // 重新過濾以更新顯示列表
            filterItems()
        }
    }

    /**
     * 刪除筆記
     * TODO: 之後會改為呼叫 API 刪除筆記
     * @param noteId 要刪除的筆記 ID
     */
    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            val currentList = _allNotes.value.toMutableList()
            currentList.removeAll { it.noteId == noteId }
            _allNotes.value = currentList

            // 重新過濾以更新顯示列表
            filterItems()
        }
    }

    /**
     * 更新筆記
     * TODO: 之後會改為呼叫 API 更新筆記
     * @param noteId 要更新的筆記 ID
     * @param title 新的標題
     * @param content 新的內容
     * @param type 新的類型
     */
    fun updateNote(
        noteId: Int,
        title: String,
        content: String,
        type: CardContentType,
        restaurantName: String? = null
    ) {
        viewModelScope.launch {
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

                // 重新過濾以更新顯示列表
                filterItems()
            }
        }
    }

    // 輔助函數：獲取當前日期
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM/dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // 輔助函數：獲取當前星期
    private fun getCurrentDay(): String {
        val sdf = SimpleDateFormat("EEEE", Locale.CHINESE)
        return "星期" + sdf.format(Date()).substring(2, 3)
    }
}