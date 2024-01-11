package com.example.whatsappclone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.whatsappclone.Activity.ChatActivity
import com.example.whatsappclone.Activity.SplashScreenActivity
import com.example.whatsappclone.Activity.SplashScreenActivity.Companion.isAppInForeground
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.UUID

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if (FirebaseAuth.getInstance().currentUser != null) {

            if (!isAppInForeground){
                sendNotification(
                    message.data.get("title").toString(),
                    message.data.get("body").toString(),
                    message.data.get("uid").toString(),
                    message.data.get("fcm").toString()
                )
            }

        }
    }

    private fun sendNotification(title: String, messageBody: String, uid: String, fcm: String) {
        val notificationId = UUID.randomUUID().toString()
        val uniqueInt = (System.currentTimeMillis() and 0xfffffffL).toInt()
        val requestCode = notificationId.hashCode() + uniqueInt

        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("fcm", fcm)
            putExtra("name", title)
            putExtra("uid", uid)
            putExtra("openChat", true)
        }

        val pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = "My channel ID"
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setSound(sound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "notification", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }
        manager.notify(notificationId.hashCode(), notificationBuilder.build())
    }

}