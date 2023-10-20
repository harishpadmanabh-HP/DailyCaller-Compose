package com.tsciences.dailycaller.android.application

import android.app.Activity
import android.app.Application
import com.facebook.FacebookSdk
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal
import com.tsciences.dailycaller.android.BuildConfig
import com.tsciences.dailycaller.android.data.preferences.SharedPrefService
import com.tsciences.dailycaller.android.notification.NotificationOpenedHandler
import com.tsciences.dailycaller.android.notification.NotificationReceivedHandler
import dagger.hilt.android.HiltAndroidApp
import io.piano.android.composer.Composer
import io.piano.android.id.PianoId
import io.piano.android.id.facebook.FacebookOAuthProvider
import io.piano.android.id.google.GoogleOAuthProvider
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

        MobileAds.initialize(this)
        Composer.init(this, BuildConfig.PIANO_AID)
        FacebookSdk.setClientToken(BuildConfig.FACEBOOK_CLIENT_TOKEN)
        FacebookSdk.sdkInitialize(this.applicationContext)
        PianoId.init(PianoId.ENDPOINT_PRODUCTION, BuildConfig.PIANO_AID)
        PianoId.init(PianoId.ENDPOINT_PRODUCTION, BuildConfig.PIANO_AID)
            .with(FacebookOAuthProvider())
        PianoId.init(PianoId.ENDPOINT_PRODUCTION, BuildConfig.PIANO_AID).with(GoogleOAuthProvider())
        SpotIm.init(this, BuildConfig.SPOT_IM_ID)

        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONESIGNAL_APP_ID)
        OneSignal.setNotificationOpenedHandler(NotificationOpenedHandler(this))
        OneSignal.setNotificationWillShowInForegroundHandler(NotificationReceivedHandler())
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