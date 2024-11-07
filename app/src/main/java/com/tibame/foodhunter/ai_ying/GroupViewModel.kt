package com.tibame.foodhunter.ai_ying

import android.util.Log
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class GroupViewModel : ViewModel() {
    private val repository = GroupRepository
    val groupChat = repository.groupChatList
    fun getGroupChatList(memberId: String) {
        viewModelScope.launch {
            val gson = Gson()
            var jsonObject = JsonObject()
            jsonObject.addProperty("id", memberId)
            val result = CommonPost("$serverUrl/group/list", jsonObject.toString())
            jsonObject = gson.fromJson(result, JsonObject::class.java)
            val collectionType = object : TypeToken<List<GroupChat>>() {}.type
            val list =
                gson.fromJson<List<GroupChat>>(jsonObject.get("result").asString, collectionType)
                    ?: emptyList()
            val incList = emptyList<GroupChat>().toMutableList()
            val endList = emptyList<GroupChat>().toMutableList()
            list.forEach {
                val date = LocalDate.parse(it.time, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val today = LocalDate.now()
                if (today > date) {
                    it.state = 0
                    endList.add(it)
                } else {
                    incList.add(it)
                }
            }
            incList.add(0, GroupChat(name = "進行中", state = 99))
            endList.add(0, GroupChat(name = "已結束", state = 99))
            repository.updateGroupChatList(incList.plus(endList))
        }
    }

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
            groupChat.value.find {
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
            val list = gson.fromJson<List<GroupSearchResult>>(
                jsonObject.get("result").asString,
                collectionType
            )
            repository.updateSearchGroupResult(list ?: emptyList())
        }
    }

    val groupSearchCache = repository.searchCache
    fun createGroup(input: GroupCreateData) {
        viewModelScope.launch {
            val gson = Gson()
            val jsonObject = JsonObject()
            jsonObject.addProperty("name", input.name)
            jsonObject.addProperty("location", "KFC")//input.location)
            jsonObject.addProperty("time", input.time)
            jsonObject.addProperty("priceMin", input.priceMin)
            jsonObject.addProperty("priceMax", input.priceMax)
            jsonObject.addProperty("isPublic", input.isPublic)
            jsonObject.addProperty("describe", input.describe)
            val result = gson.fromJson(
                CommonPost("$serverUrl/group/create", jsonObject.toString()),
                JsonObject::class.java
            )
            joinGroup(result.get("groupId").toString(), "1")//TODO:memberId
        }
    }

    fun joinGroup(groupId: String, memberId: String) {
        viewModelScope.launch {
            val jsonObject = JsonObject()
            jsonObject.addProperty("groupId", groupId)
            jsonObject.addProperty("memberId", memberId)
            CommonPost("$serverUrl/group/join", jsonObject.toString())
            getGroupChatList(memberId)
        }
    }

    val selectSearchResult = repository.selectSearchResult
    fun updateSelectSearchResult(input: GroupSearchResult) {
        repository.updateSelectSearchResult(input)
    }

    val groupChatHistory = repository.groupChatHistory
    fun getGroupChatHistory(groupRoomId: Int) {
        viewModelScope.launch {
            val gson = Gson()
            var jsonObject = JsonObject()
            jsonObject.addProperty("id", "$groupRoomId")
            val result = CommonPost("$serverUrl/group/chat/get", jsonObject.toString())
            jsonObject = gson.fromJson(result, JsonObject::class.java)
            val collectionType = object : TypeToken<List<GroupChatHistory>>() {}.type
            val list = gson.fromJson<List<GroupChatHistory>>(
                jsonObject.get("result").asString,
                collectionType
            )?: emptyList()
            repository.updateGroupChatHistory(list)
        }
    }

    fun sendMessage(chatInput: String) {
        viewModelScope.launch {
            val jsonObject = JsonObject()
            jsonObject.addProperty("groupId", "${_nowChatRoomId.value}")
            jsonObject.addProperty("memberId", "1")//TODO:memberId
            jsonObject.addProperty("message", chatInput)
            val result = CommonPost("$serverUrl/group/chat/send", jsonObject.toString())
            Log.d("qq",result)
            //TODO: getGroupChatHistory
            getGroupChatHistory(_nowChatRoomId.value)
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