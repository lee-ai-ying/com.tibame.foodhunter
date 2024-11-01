package com.tibame.foodhunter.a871208s

import com.tibame.foodhunter.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object PrivateChatRepository {

    private val _PrivateChatList = MutableStateFlow(emptyList<PrivateChat>())
    val PrivateChatList: StateFlow<List<PrivateChat>> = _PrivateChatList.asStateFlow()

    init {
        _PrivateChatList.update { setTestData() }
    }

    private fun setTestData(): List<PrivateChat> {
        return listOf(
            PrivateChat("0000_0001","0001","Ivy", R.drawable.image,1),
            PrivateChat("0000_0002","0002", "Mary", R.drawable.account_circle,1),
            PrivateChat("0000_0003","0003", "Sue", R.drawable.account_circle,1),
            PrivateChat("0000_0004","0004", "Sue", R.drawable.account_circle,1),
        )
    }
}