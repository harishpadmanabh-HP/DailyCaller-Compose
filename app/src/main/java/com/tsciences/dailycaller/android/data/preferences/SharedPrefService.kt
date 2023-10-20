package com.tsciences.dailycaller.android.data.preferences

import android.content.SharedPreferences
import com.tsciences.dailycaller.android.appConstants.KEY_INTERSTITIAL_ADD_COUNT
import com.tsciences.dailycaller.android.appConstants.KEY_PIANO_TOKEN
import com.tsciences.dailycaller.android.appConstants.KEY_SPOT_IM_TOKEN

class SharedPrefService(
    private val sharedPreferences: SharedPreferences
) {
    fun getPianoToken(): String = sharedPreferences.getString(KEY_PIANO_TOKEN, "").orEmpty()
    fun setPianoToken(token: String?) {
        with(sharedPreferences.edit()) {
            putString(KEY_PIANO_TOKEN, token)
            apply()
        }
    }

    fun getSpotImToken(): String = sharedPreferences.getString(KEY_SPOT_IM_TOKEN, "").orEmpty()
    fun setSpotImToken(token: String?) {
        with(sharedPreferences.edit()) {
            putString(KEY_SPOT_IM_TOKEN, token)
            apply()
        }
    }

    fun getInterstitialAddCount(): Int =
        sharedPreferences.getInt(KEY_INTERSTITIAL_ADD_COUNT, 0)

    fun setInterstitialAddCount(adCount: Int?) {
        with(sharedPreferences.edit()) {
            if (adCount != null) {
                putInt(KEY_INTERSTITIAL_ADD_COUNT, adCount)
            }
            apply()
        }
    }
}