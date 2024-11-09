package com.tibame.foodhunter.a871208s

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PrivateViewModel : ViewModel() {
    private val repository = PrivateChatRepository
    val PrivateChatFlow = repository.chatroomState
    val PrivateChatFlow2 = repository.chatroomState2
    suspend fun refreshchatroom(username:String) {
        repository.refreshchatroom(username)}
    private val _nowChatRoomId = MutableStateFlow("")
    val nowChatRoomId = _nowChatRoomId.asStateFlow()
    fun gotoChatRoom(roomId: String) {
        _nowChatRoomId.update {
            roomId
        }
    }
    var wall =mutableStateOf(false)
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
