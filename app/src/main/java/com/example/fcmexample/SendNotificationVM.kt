package com.example.fcmexample

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fcmexample.utils.PREFS_NAME
import com.example.fcmexample.utils.SingleLiveEvent
import com.example.fcmexample.utils.TOKEN
import com.example.fcmsender.FCMSender
import com.example.fcmsender.MessageType
import com.example.fcmsender.models.FCMResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SendNotificationVM : ViewModel(), CoroutineScope {

    val sendNotificationMessage = SingleLiveEvent<String>()

    private var sendNotificationJob: Job = Job()
    private val sharedPreferences by lazy {
        BaseApplication.instance.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + sendNotificationJob

    val sendDataMessage = MutableLiveData<Boolean>(true)
    val title = MutableLiveData<String>()
    val body = MutableLiveData<String>()
    val topic = MutableLiveData<String>()

    fun sendNotification() {
        val title = title.value
        if (title.isNullOrBlank()) {
            sendNotificationMessage.value = "Please enter title"
            return
        }

        val body = body.value
        if (body.isNullOrBlank()) {
            sendNotificationMessage.value = "Please enter body"
            return
        }

        val topic = topic.value
        if (topic.isNullOrBlank()) {
            sendNotificationMessage.value = "Please enter topic"
            return
        }

        val type = if (sendDataMessage.value!!) MessageType.DATA else MessageType.NOTIFICATION

        sharedPreferences.getString(
            TOKEN, ""
        )?.let {
            launch(coroutineContext) {
                val response = FCMSender.FCMMessageBuilder()
                    .setMessageType(type)
                    .setTitle(title)
                    .setBody(body)
                    .setTopic(topic)
                    .build()
                    .sendTo(it)
                onResponse(response)
            }
        }
    }

    private fun onResponse(response: FCMResponse) {
        if (response.success == 1) {
            sendNotificationMessage.value = "Send message successfully"
            return
        }

        // Todo: map error here
        sendNotificationMessage.value = "Send message failed!"
    }

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }
}
