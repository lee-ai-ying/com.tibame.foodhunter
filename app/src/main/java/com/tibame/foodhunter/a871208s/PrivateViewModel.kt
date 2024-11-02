package com.tibame.foodhunter.a871208s

import androidx.lifecycle.ViewModel
import com.tibame.foodhunter.ai_ying.GroupChat
import com.tibame.foodhunter.ai_ying.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

class PrivateViewModel : ViewModel() {
    private val repository = PrivateChatRepository
    val PrivateChatFlow = repository.PrivateChatList

    private val _nowChatRoomId = MutableStateFlow("")
    val nowChatRoomId = _nowChatRoomId.asStateFlow()
    fun gotoChatRoom(roomId: String) {
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

    private val _chatRoom = MutableStateFlow(PrivateChat())
    val chatRoom: StateFlow<PrivateChat> = _chatRoom.asStateFlow()
    fun setDetailPrivateChat(privateChat: PrivateChat) {
        _chatRoom.update {
            privateChat
        }
    }
}
