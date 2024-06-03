package com.tsciences.dailycaller.android.ui.deepLinking

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.tsciences.dailycaller.android.application.DailyCallerApplication
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.ui.commonComponents.LoadingOverlay
import com.tsciences.dailycaller.android.ui.menu.LocalPlayerActivity
import com.tsciences.dailycaller.android.ui.newsDetail.NewsDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeepLinkingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            if (isDeviceTablet(this)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            AnimatedVisibility(
                visible = true, enter = fadeIn(), exit = fadeOut(), modifier = Modifier.zIndex(1f)
            ) {
                LoadingOverlay()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
            val dailyCallerApplication = this.applicationContext as DailyCallerApplication
            val currActivity: Activity? = dailyCallerApplication.getCurrentActivity()
            if (currActivity != null) {
                if (currActivity is NewsDetailActivity) {
                    val intent = Intent(this, NewsDetailActivity::class.java)
                    intent.putExtra("deeplink", true)
                    startActivity(intent)
                } else if (currActivity is LocalPlayerActivity) {
                    val intent = Intent(this, LocalPlayerActivity::class.java)
                    intent.putExtra("deeplink", true)
                    startActivity(intent)
                }
            }
        }, 1000)
    }
}

