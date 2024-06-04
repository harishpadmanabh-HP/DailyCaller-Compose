package com.tsciences.dailycaller.android.application

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.FacebookSdk
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import com.tsciences.dailycaller.android.BuildConfig
import com.tsciences.dailycaller.android.core.util.getLinkFromSearchItem
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.data.preferences.SharedPrefService
import com.tsciences.dailycaller.android.ui.home.HomeActivity
import dagger.hilt.android.HiltAndroidApp
import io.piano.android.composer.Composer
import io.piano.android.id.PianoId
import io.piano.android.id.facebook.FacebookOAuthProvider
import io.piano.android.id.google.GoogleOAuthProvider
import io.piano.android.id.models.PianoIdAuthFailureResult
import io.piano.android.id.models.PianoIdAuthSuccessResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import spotIm.sdk.SpotIm
import javax.inject.Inject

@HiltAndroidApp
class DailyCallerApplication : Application() {

    private var mInstance: DailyCallerApplication? = null

    @Inject
    lateinit var sharedPrefService: SharedPrefService

    private var mCurrentActivity: Activity? = null
    override fun onCreate() {
        super.onCreate()

        mInstance = this
        FirebaseApp.initializeApp(this)

        MobileAds.initialize(this)
        Composer.init(this, BuildConfig.PIANO_AID)
        FacebookSdk.setClientToken(BuildConfig.FACEBOOK_CLIENT_TOKEN)
        FacebookSdk.sdkInitialize(this.applicationContext)
        PianoId.init(PianoId.ENDPOINT_PRODUCTION, BuildConfig.PIANO_AID)
            .with(GoogleOAuthProvider())
            .with(FacebookOAuthProvider())

        SpotIm.init(this, BuildConfig.SPOT_IM_ID)

        //OneSignal
        OneSignal.Debug.logLevel = LogLevel.ERROR




        //OneSignal.consentRequired = false
        OneSignal.initWithContext(this, BuildConfig.ONESIGNAL_APP_ID)
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }
      /*  if (!OneSignal.Notifications.permission) {
            CoroutineScope(Dispatchers.IO).launch {
                OneSignal.Notifications.requestPermission(true)
            }
        }*/

        val clickListener = object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                event.notification.additionalData?.let { notificationOpened(it) }
            }
        }
        OneSignal.Notifications.addClickListener(clickListener)
        OneSignal.Notifications.removeClickListener(clickListener)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                Log.d("CurrentActivity", "Resumed: ${activity.javaClass.simpleName}")
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }

    fun notificationOpened(data: JSONObject) {
        var customUrl: String? = null
        var launchHome = true

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

        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        if (launchHome) {

            if (isDeviceTablet(this)) {
                val intent = Intent(
                    this.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                this.startActivity(intent)
            } else {
                val intent = Intent(
                    this.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                this.getApplicationContext().startActivity(intent)
            }
        } else {
            val bundle = Bundle()
            bundle.putBoolean("fromSearch", true)
            bundle.putString("search_link", customUrl?.let { getLinkFromSearchItem(it) })
            if (isDeviceTablet(this)) {
                val intent = Intent(
                    this.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("news_bundle", bundle)
                this.getApplicationContext().startActivity(intent)
            } else {
                val intent = Intent(
                    this.getApplicationContext(),
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("news_bundle", bundle)
                this.getApplicationContext().startActivity(intent)
            }
        }
    }

    fun getCurrentActivity(): Activity? {
        return mCurrentActivity
    }

    fun setCurrentActivity(mCurrentActivity: Activity?) {
        this.mCurrentActivity = mCurrentActivity
    }

    @Synchronized
    fun getInstance(): DailyCallerApplication? {
        return DailyCallerApplication().mInstance
    }
}