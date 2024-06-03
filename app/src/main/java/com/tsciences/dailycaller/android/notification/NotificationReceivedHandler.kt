package com.tsciences.dailycaller.android.notification

//import android.util.Log
//import com.onesignal.OSNotificationReceivedEvent
//import com.onesignal.OneSignal
//import org.json.JSONObject

//class NotificationReceivedHandler() : OneSignal.OSNotificationWillShowInForegroundHandler {
//
//    override fun notificationWillShowInForeground(osNotificationReceivedEvent: OSNotificationReceivedEvent?) {
//        val data: JSONObject? = osNotificationReceivedEvent?.getNotification()?.getAdditionalData()
//        val notificationID: String? =
//            osNotificationReceivedEvent?.getNotification()?.getNotificationId()
//        val title: String? = osNotificationReceivedEvent?.getNotification()?.getTitle()
//        val body: String? = osNotificationReceivedEvent?.getNotification()?.getBody()
//        val smallIcon: String? = osNotificationReceivedEvent?.getNotification()?.getSmallIcon()
//        val largeIcon: String? = osNotificationReceivedEvent?.getNotification()?.getLargeIcon()
//        val bigPicture: String? = osNotificationReceivedEvent?.getNotification()?.getBigPicture()
//        val smallIconAccentColor: String? =
//            osNotificationReceivedEvent?.getNotification()?.getSmallIconAccentColor()
//        val sound: String? = osNotificationReceivedEvent?.getNotification()?.getSound()
//        val ledColor: String? = osNotificationReceivedEvent?.getNotification()?.getLedColor()
//        val lockScreenVisibility: Int? =
//            osNotificationReceivedEvent?.getNotification()?.getLockScreenVisibility()
//        val groupKey: String? = osNotificationReceivedEvent?.getNotification()?.getGroupKey()
//        val groupMessage: String? =
//            osNotificationReceivedEvent?.getNotification()?.getGroupMessage()
//        val fromProjectNumber: String? =
//            osNotificationReceivedEvent?.getNotification()?.getFromProjectNumber()
//        val rawPayload: String? = osNotificationReceivedEvent?.getNotification()?.getRawPayload()
//
//        val customKey: String?
//
//        Log.i("OneSignalExample", "NotificationID received: $notificationID")
//
//        if (data != null) {
//            customKey = data.optString("customkey", null)
//            if (customKey != null) Log.i(
//                "OneSignalExample",
//                "customkey set with value: $customKey"
//            )
//        }
//    }
//}