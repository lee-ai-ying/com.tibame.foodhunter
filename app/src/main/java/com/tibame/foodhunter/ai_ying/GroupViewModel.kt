package com.tibame.foodhunter.ai_ying

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class GroupViewModel : ViewModel() {
    private val repository = GroupRepository
    val groupChatFlow: StateFlow<List<GroupChat>> = repository.groupChatList


}