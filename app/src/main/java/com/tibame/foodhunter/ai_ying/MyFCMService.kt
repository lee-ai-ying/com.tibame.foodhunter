package com.tibame.foodhunter.ai_ying

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFCMService : FirebaseMessagingService() {
    // repository採單例模式
    private val repository = GroupRepository

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
        //Log.d("qq","onMessageReceived():\ntitle: $title, body: $body, data: $data")
        repository.gChatVM?.updateGroupChat()
    }

    // 當registration token更新時呼叫，應該將新的token傳送至server
    override fun onNewToken(token: String) {
        //Log.d("qq", "onNewToken: $token")
    }

    // 當FCM server刪除暫存訊息時呼叫(例如：裝置超過4週未存取訊息)
    override fun onDeletedMessages() {
        super.onDeletedMessages()
        //Log.d("qq", "onDeletedMessages")
    }
}
