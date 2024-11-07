package com.tibame.foodhunter.ai_ying

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tibame.foodhunter.global.CommonPost
import com.tibame.foodhunter.global.serverUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun getGroupChatDetailFromId(id: Int) {
        _chatRoom.update {
            groupChatFlow.value.find {
                it.id == id && it.state != 99
            } ?: GroupChat()
        }
    }

    private val _groupCreateData = MutableStateFlow(GroupCreateData())
    val groupCreateData: StateFlow<GroupCreateData> = _groupCreateData.asStateFlow()
    fun setGroupCreateData(data: GroupCreateData) {
        _groupCreateData.update {
            data
        }
    }

    private val _groupSearchData = MutableStateFlow(GroupSearchData())
    val groupSearchData: StateFlow<GroupSearchData> = _groupSearchData.asStateFlow()
    fun setGroupSearchData(data: GroupSearchData) {
        _groupSearchData.update {
            data
        }
    }

    private val _groupSearchResult = MutableStateFlow(emptyList<GroupSearchResult>())
    var groupSearchResult = _groupSearchResult.asStateFlow()
    fun getGroupSearchResult() {
        groupSearchResult = repository.groupSearchResult
    }

    var showEditGroup = MutableStateFlow(false)
    var showEditMember = MutableStateFlow(false)
    fun setShowEditGroup(show: Boolean) {
        showEditGroup.update { show }
    }

    fun setShowEditMember(show: Boolean) {
        showEditMember.update { show }
    }

    fun searchGroupByCondition(input: GroupSearchData) {
        repository.updateGroupSearchCache(input)
        viewModelScope.launch {
            val gson = Gson()
            var jsonObject = JsonObject()
            jsonObject.addProperty("name", input.name)
            jsonObject.addProperty("location", input.location)
            jsonObject.addProperty("time", input.time)
            jsonObject.addProperty("priceMin", input.priceMin)
            jsonObject.addProperty("priceMax", input.priceMax)
            jsonObject.addProperty("describe", input.describe)
            val result = CommonPost("$serverUrl/group/search", jsonObject.toString())
            jsonObject = gson.fromJson(result, JsonObject::class.java)
            val collectionType = object : TypeToken<List<GroupSearchResult>>() {}.type
            val list=gson.fromJson<List<GroupSearchResult>>(jsonObject.get("result").asString, collectionType)
            repository.updateSearchGroupResult(list?: emptyList())
        }
    }
    val groupSearchCache=repository.searchCache
    fun createGroup(input: GroupCreateData) {
        viewModelScope.launch {
            val gson = Gson()
            var jsonObject = JsonObject()
            jsonObject.addProperty("name", input.name)
            jsonObject.addProperty("location", "KFC")//input.location)
            jsonObject.addProperty("time", input.time)
            jsonObject.addProperty("priceMin", input.priceMin)
            jsonObject.addProperty("priceMax", input.priceMax)
            jsonObject.addProperty("isPublic", input.isPublic)
            jsonObject.addProperty("describe", input.describe)
            val result = CommonPost("$serverUrl/group/create", jsonObject.toString())
        }
    }
//    init {
//        viewModelScope.launch {
//            val gson = Gson()
//            var jsonObject = JsonObject()
//            jsonObject.addProperty("id", 2)
//            val result=CommonPost(serverUrl+"/getGroupList",jsonObject.toString())
//
//            jsonObject = gson.fromJson(result, JsonObject::class.java)
//            val collectionType = object : TypeToken<List<GroupChat>>() {}.type
//            val list = gson.fromJson<List<GroupChat>>(jsonObject.get("groups").asString, collectionType)
//            list.forEach{
//                Log.d("qq",it.id.toString()+" "+it.name)
//            }
////            Log.d("qq",jsonObject.toString())
//        }
//    }
}