package com.tsciences.dailycaller.android.notification

//import android.content.Context
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.onesignal.OSNotification
//import com.onesignal.OSNotificationReceivedEvent
//import com.onesignal.OneSignal
//import java.math.BigInteger
//
//
//class NotificationServiceExtension() : OneSignal.OSRemoteNotificationReceivedHandler {
//
//    override fun remoteNotificationReceived(
//        p0: Context?,
//        osNotificationReceivedEvent: OSNotificationReceivedEvent?
//    ) {
//        val notification: OSNotification? = osNotificationReceivedEvent?.getNotification()
//
//        // Example of modifying the notification's accent color
//
//        // Example of modifying the notification's accent color
//        val mutableNotification = notification?.mutableCopy()
//        mutableNotification?.setExtender { builder: NotificationCompat.Builder ->
//            // Sets the accent color to Green on Android 5+ devices.
//            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
//            builder.color = BigInteger("FFFF0000", 16).toInt()
//            builder
//        }
//        val data = notification?.additionalData
//        Log.i("OneSignalExample", "Received Notification Data: $data")
//
//        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
//        // To omit displaying a notification, pass `null` to complete()
//
//        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
//        // To omit displaying a notification, pass `null` to complete()
//        osNotificationReceivedEvent?.complete(mutableNotification)
//    }
//}