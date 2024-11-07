package com.tibame.foodhunter.sharon.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 筆記編輯頁面的 UI 狀態
 * 包含頁面上所有需要追蹤的狀態
 */
data class NoteEditUiState(
    val isFirstEntry: Boolean = true,  // 是否首次進入
    val hasTitle: Boolean = false,     // 是否有標題
    val isExistingNote: Boolean = false, // 是否為既有筆記
    val title: String = "",               // 標題
    val content: String = "",             // 內容
    val restaurantName: String? = null,   // 關聯餐廳名稱
    val type: CardContentType = CardContentType.NOTE, // 筆記類型
    val date: String = "",                // 日期
    val isLoading: Boolean = false,       // 加載狀態
    val errorMessage: String? = null,      // 錯誤訊息
)

/**
 * 定義筆記編輯頁面可能發生的所有事件
 */
sealed class NoteEditEvent {
    // 輸入相關事件
    data class UpdateTitle(val title: String) : NoteEditEvent()
    data class UpdateContent(val content: String) : NoteEditEvent()
    data class UpdateRestaurant(val name: String?) : NoteEditEvent()
    data class UpdateDate(val date: String) : NoteEditEvent()

    // 操作相關事件
    object SaveNote : NoteEditEvent()      // 保存筆記
    object NavigateBack : NoteEditEvent()  // 返回上一頁
    object DismissError : NoteEditEvent()  // 關閉錯誤提示
}

/**
 * 筆記編輯頁面的 ViewModel
 */
class NoteEditVM : ViewModel() {

    companion object {
        private const val TAG = "NoteEditVM"
    }

    // UI 狀態
    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState = _uiState.asStateFlow()

    // 當前編輯的筆記
    private val _note = MutableStateFlow<Note?>(null)
    val note = _note.asStateFlow()

    // 初始化 repository
    private val repository = NoteRepository.instance

    /**
     * 載入指定 ID 的筆記
     */
    fun loadNote(noteId: Int) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // 使用你現有的 repository 方法
                val noteData = repository.getNoteById(noteId)
                _note.value = noteData

                // 更新 UI 狀態
                noteData?.let { note ->
                    _uiState.update { state ->
                        state.copy(
                            title = note.title,
                            content = note.noteContent,
                            restaurantName = note.restaurantName,
                            type = note.type,
                            date = note.date
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "載入筆記失敗", e)
                _uiState.update {
                    it.copy(errorMessage = "載入筆記失敗：${e.message}")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * 處理所有 UI 事件
     */
    fun onEvent(event: NoteEditEvent) {
        when (event) {
            is NoteEditEvent.UpdateTitle -> {
                _uiState.update { it.copy(title = event.title) }
                // 自動保存機制
                if (event.title.isNotEmpty()) {
                    saveNote()
                }
            }

            is NoteEditEvent.UpdateContent -> {
                _uiState.update { it.copy(content = event.content) }
            }

            is NoteEditEvent.UpdateRestaurant -> {
                _uiState.update { it.copy(restaurantName = event.name) }
            }

            is NoteEditEvent.UpdateDate -> {
                _uiState.update { it.copy(date = event.date) }
            }

            is NoteEditEvent.SaveNote -> saveNote()
            is NoteEditEvent.NavigateBack -> {
                // 導航邏輯會在 UI 層處理
            }

            is NoteEditEvent.DismissError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    /**
     * 保存筆記
     */
    private fun saveNote() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val currentState = _uiState.value

                // 檢查必填欄位
                if (currentState.title.isBlank()) {
                    _uiState.update {
                        it.copy(errorMessage = "請輸入標題")
                    }
                    return@launch
                }

                if (note.value == null) {
                    // 新增筆記
                    repository.addNote(
                        title = currentState.title,
                        content = currentState.content,
                        type = currentState.type,
                        restaurantName = currentState.restaurantName
                    )
//                } else {
//                    // 更新筆記
//                    repository.updateNote(
//                        noteId = note.value!!.noteId,
//                        title = currentState.title,
//                        content = currentState.content,
//                        type = currentState.type,
//                        restaurantName = currentState.restaurantName
//                    )
                }
                // 保存成功的處理會在 UI 層進行
            } catch (e: Exception) {
                Log.e(TAG, "保存筆記失敗", e)
                _uiState.update {
                    it.copy(errorMessage = "保存失敗：${e.message}")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // 新增一個方法處理保存後的導航
    fun saveAndNavigateBack(navController: NavHostController) {
        viewModelScope.launch {
            try {
                onEvent(NoteEditEvent.SaveNote)
                // 保存成功後返回
                navController.popBackStack()
            } catch (e: Exception) {
                // 錯誤處理
            }
        }
    }
}