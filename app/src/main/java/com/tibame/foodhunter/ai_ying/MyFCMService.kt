package com.tibame.foodhunter.ai_ying


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {
    // repository採單例模式
    private val groupRepository = GroupRepository

    // 當App在前景收到FCM時呼叫，App在背景收到FCM時不會呼叫此方法
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var title = ""
        var body = ""
        // 取得notification資料，主要為title與body這2個保留字
        remoteMessage.notification?.let { notification ->
            title = notification.title ?: ""
            body = notification.body ?: ""
        }
        // 取得自訂資料
        val data = remoteMessage.data["data"]
        groupRepository.gChatVM?.updateGroupChat()
        val userVM = groupRepository.gChatVM?.userVM
        groupRepository.pChatVM?.refreshmessage2(userVM?.username?.value?:"", groupRepository.pChatVM?.friend?.value?:"")
    }

    // 當registration token更新時呼叫，應該將新的token傳送至server
    override fun onNewToken(token: String) {
        //Log.d("qq", "onNewToken: $token")
    }

}
