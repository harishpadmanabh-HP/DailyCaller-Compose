package com.tsciences.dailycaller.android.FirebaseAnalyticsEvent

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class EventRegister {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    fun getInstance(context: Context?): FirebaseAnalytics? {
        if (mFirebaseAnalytics == null) {
            initializeFirebaseAnalytics(context)
        }
        return mFirebaseAnalytics
    }

    fun initializeFirebaseAnalytics(context: Context?) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
    }

    fun registerEvent(context: Context?, eventName: String?, bundle: Bundle?) {
        getInstance(context)!!.logEvent(eventName!!, bundle)
    }
}