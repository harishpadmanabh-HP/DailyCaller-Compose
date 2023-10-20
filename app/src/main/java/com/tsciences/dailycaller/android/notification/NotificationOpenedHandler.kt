package com.tsciences.dailycaller.android.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
//import app.android.seekerwallet.core.util.getLinkFromSearchItem
//import app.android.seekerwallet.core.util.isDeviceTablet
import com.onesignal.OSNotificationOpenedResult
import com.onesignal.OneSignal
import com.tsciences.dailycaller.android.core.util.getLinkFromSearchItem
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.ui.home.HomeActivity
import org.json.JSONException
import org.json.JSONObject

class NotificationOpenedHandler(private val context: Context) :
    OneSignal.OSNotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenedResult?) {
        val data: JSONObject? = result?.getNotification()?.getAdditionalData()
        var customUrl: String? = null
        var launchHome = true

        if (data != null) {
            try {
                customUrl = data["myappurl"] as String
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val mainUrl = customUrl?.replace("\\", "")
            val arrOfStr: Array<String>? =
                mainUrl?.split("[?]+".toRegex())?.dropLastWhile { it.isEmpty() }
                    ?.toTypedArray()
            if (arrOfStr?.isNotEmpty() == true) customUrl = arrOfStr[0]
            if (customUrl != null) {
                launchHome = false
            }
        }

        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        if (launchHome) {

            if (isDeviceTablet(context)) {
                val intent = Intent(
                    context.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } else {
                val intent = Intent(
                    context.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                context.getApplicationContext().startActivity(intent)
            }
        } else {
            val bundle = Bundle()
            bundle.putBoolean("fromSearch", true)
            bundle.putString("search_link", customUrl?.let { getLinkFromSearchItem(it) })
            if (isDeviceTablet(context)) {
                val intent = Intent(
                    context.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("news_bundle", bundle)
                context.getApplicationContext().startActivity(intent)
            } else {
                val intent = Intent(
                    context.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("news_bundle", bundle)
                context.getApplicationContext().startActivity(intent)
            }
        }
    }
}