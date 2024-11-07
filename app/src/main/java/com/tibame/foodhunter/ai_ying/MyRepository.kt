package com.tibame.foodhunter.ai_ying
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 建立資料共享物件 */
// 需使用單例模式，只要使用「object關鍵字 + 類別名稱」即可
object MyRepository {
    private val _dataFlow = MutableStateFlow("")
    val dataFlow: StateFlow<String> = _dataFlow.asStateFlow()

    fun setData(newData: String) {
        _dataFlow.value = newData
    }
}