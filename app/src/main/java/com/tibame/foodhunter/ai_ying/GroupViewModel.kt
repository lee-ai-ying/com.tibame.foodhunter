package com.tibame.foodhunter.ai_ying

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    private val _groupCreateData = MutableStateFlow(GroupCreateData())
    val groupCreateData: StateFlow<GroupCreateData> = _groupCreateData.asStateFlow()
    fun setGroupCreateData(data:GroupCreateData){
        _groupCreateData.update {
            data
        }
        Log.d("ai",data.toString())
    }

    private val _groupSearchData = MutableStateFlow(GroupSearchData())
    val groupSearchData: StateFlow<GroupSearchData> = _groupSearchData.asStateFlow()
    fun setGroupSearchData(data:GroupSearchData){
        _groupSearchData.update {
            data
        }
        Log.d("ai",data.toString())
    }

    //var isChatRoomFunctionButtonShow = MutableStateFlow(false).asStateFlow()
}