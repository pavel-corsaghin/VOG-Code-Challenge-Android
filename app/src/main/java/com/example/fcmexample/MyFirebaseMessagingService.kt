package com.example.fcmexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.example.fcmexample.db.FCMExampleDB
import com.example.fcmexample.db.Notification
import com.example.fcmexample.utils.PREFS_NAME
import com.example.fcmexample.utils.TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.StringBuilder

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.d("FCM token: $token")
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(
            TOKEN, token
        )
            .apply()
        super.onNewToken(token)
    }

    private fun getIntentForRemoteMessage(remoteMessage: RemoteMessage): Intent {
        return when (remoteMessage.data["Topic"]) {
            "test" -> Intent(this, TestActivity::class.java)
            else -> Intent(this, MainActivity::class.java)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val content = StringBuilder()
        if (remoteMessage.notification != null) {
            content.append("NOTIFICATION\n")
                .append("[Title: ${remoteMessage.notification?.title}]\n")
                .append("[Body: ${remoteMessage.notification?.body}]")
                .append("\n\n")
        }
        content.append("DATA")
        remoteMessage.data.entries.forEach {
            content.append("\n[${it.key}: ${it.value}]")
        }
        val notification = Notification(System.currentTimeMillis(), content.toString())
        CoroutineScope(Dispatchers.IO).launch {
            FCMExampleDB.getDatabase(this@MyFirebaseMessagingService).notificationsDao()
                .upsert(notification)
        }
        Timber.v(notification.content)
        displayNotification(remoteMessage)
    }

    private fun displayNotification(remoteMessage: RemoteMessage) {
        val topic = remoteMessage.data["Topic"]

        val stackBuilder = TaskStackBuilder.create(this.applicationContext)
        val intent = getIntentForRemoteMessage(remoteMessage)
        stackBuilder.addNextIntentWithParentStack(intent)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = getString(R.string.app_name) + topic
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val summaryNotification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.app_name))
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setStyle(NotificationCompat.InboxStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setGroup(channelId)
            .setGroupSummary(true)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .setColorized(true)
            .setContentTitle(remoteMessage.data["Title"] ?: "")
            .setContentText(remoteMessage.data["Body"] ?: "")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(channelId)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        notificationManager.apply {
            notify(0, summaryNotification.build())
            notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }
}