package com.tibame.foodhunter.ai_ying

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GroupViewModel : ViewModel() {
    private val repository = GroupRepository
    val groupChatFlow: StateFlow<List<GroupChat>> = repository.groupChatList


}