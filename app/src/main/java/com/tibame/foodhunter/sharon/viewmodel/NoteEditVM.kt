package com.tibame.foodhunter.sharon.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.tibame.foodhunter.sharon.data.CardContentType
import com.tibame.foodhunter.sharon.data.Note
import com.tibame.foodhunter.sharon.data.NoteRepository
import kotlinx.coroutines.delay
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
    /**
     * 更新標題事件
     * @param title 新的標題內容
     * 使用時機：
     * 1. 使用者輸入/修改標題時
     * 2. 清空標題時
     */
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

    // 新增保存成功的事件
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess = _saveSuccess.asStateFlow()

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
                            isFirstEntry = false,      // 設為非首次進入
                            isExistingNote = true,     // 設為既有筆記
                            hasTitle = note.title.isNotEmpty(),
                            title = note.title,
                            content = note.content,
                            restaurantName = note.restaurantName,
                            type = note.type,
                            date = note.date
                        )
                    }
                    Log.d(TAG, "筆記載入成功: ${note.title}")

                } ?: run {
                    Log.e(TAG, "找不到指定筆記")
                    _uiState.update {
                        it.copy(errorMessage = "找不到指定筆記")
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
            is NoteEditEvent.UpdateTitle -> handleTitleUpdate(event.title)

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

    private fun handleTitleUpdate(newTitle: String) {
        _uiState.update { currentState ->
            currentState.copy(
                title = newTitle,
                hasTitle = newTitle.isNotEmpty(),
                isFirstEntry =
                if (currentState.isFirstEntry && newTitle.isEmpty()) true
                else currentState.isFirstEntry

            )
        }
        // 2. 根據不同情境處理自動保存
        when {
            // 情境1: 首次輸入且有標題
            uiState.value.isFirstEntry && newTitle.isNotEmpty() -> {
//                saveNote()  // 自動保存
                Log.d(TAG, "首次輸入標題，自動創建筆記")
            }

            // 情境2: 非首次輸入且標題被清空
            !uiState.value.isFirstEntry && newTitle.isEmpty() -> {
                _uiState.update {
                    it.copy(errorMessage = "請輸入標題")
                }
                Log.d(TAG, "既有筆記標題被清空，顯示警告")
            }

            // 情境3: 既有筆記修改 - 自動更新
            !uiState.value.isFirstEntry && newTitle.isNotEmpty() -> {
//                saveNote()  // 自動保存更新
                Log.d(TAG, "既有筆記標題修改，自動更新")
            }
        }
    }
    /**
     * 返回按鈕處理
     */
    fun saveAndNavigateBack(navController: NavHostController) {
        viewModelScope.launch {
            // 有標題才保存
            if (uiState.value.hasTitle) {
                saveNote()
            }
            // 保存成功或沒有內容需要保存時，返回
            navController.popBackStack()
        }
    }

    /**
     * 筆記保存邏輯
     * 功能：
     * 1. 處理新增/更新筆記
     * 2. 更新筆記狀態
     * 3. 錯誤處理
     */
    private fun saveNote() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val currentState = _uiState.value

                _uiState.update { it.copy(isLoading = true) }
                    // 執行新增操作
                    val newNoteId = repository.addNote(
                        title = currentState.title,
                        content = currentState.content,
                        type = currentState.type,
                        restaurantName = currentState.restaurantName
                    )
                        // 更新狀態：不再是首次輸入
                        _saveSuccess.value = true

                        _uiState.update {
                            it.copy(
                                isFirstEntry = false,
                                isExistingNote = true,
                                hasTitle = true
                            )
                        }

                    Log.d(TAG, "新筆記創建成功，ID: $newNoteId")

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "儲存失敗：${e.message}")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }


}




