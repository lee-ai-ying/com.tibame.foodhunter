package com.tibame.foodhunter.ai_ying

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

class GroupViewModel : ViewModel() {
    private val repository = GroupRepository
    val groupChatFlow = repository.groupChatList

    private val _nowChatRoomId = MutableStateFlow(999)
    val nowChatRoomId = _nowChatRoomId.asStateFlow()
    fun gotoChatRoom(roomId: Int) {
        _nowChatRoomId.update {
            roomId
        }
    }

    private val _chatInput = MutableStateFlow("")
    val chatInput = _chatInput.asStateFlow()
    fun chatRoomInput(text: String) {
        _chatInput.update {
            text
        }
    }

    private val _chatRoom = MutableStateFlow(GroupChat())
    val chatRoom: StateFlow<GroupChat> = _chatRoom.asStateFlow()
    fun setDetailGroupChat(groupChat: GroupChat) {
        _chatRoom.update {
            groupChat
        }
    }
}