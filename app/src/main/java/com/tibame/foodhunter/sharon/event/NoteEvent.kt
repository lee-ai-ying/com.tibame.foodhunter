package com.tibame.foodhunter.sharon.event

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object NoteEvent {
    private val _refreshTrigger = MutableStateFlow(false)  // 改用 boolean
    val refreshTrigger = _refreshTrigger.asStateFlow()

    fun triggerRefresh() {
        _refreshTrigger.value = true
    }

    fun resetTrigger() {
        _refreshTrigger.value = false
    }
}