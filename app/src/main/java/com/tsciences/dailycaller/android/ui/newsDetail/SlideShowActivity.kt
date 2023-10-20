package com.tsciences.dailycaller.android.ui.newsDetail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.tsciences.dailycaller.android.core.theme.DailyCallerTheme
import com.tsciences.dailycaller.android.core.util.share
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.ui.commonComponents.SnackbarController
import com.tsciences.dailycaller.android.ui.commonComponents.rememberSnackbarController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SlideShowActivity : AppCompatActivity() {
    private lateinit var snackBarController: SnackbarController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsDetails = intent.getStringExtra("news")
        val news = Gson().fromJson(newsDetails, Item::class.java)
        setContent {
            snackBarController = rememberSnackbarController()
            DailyCallerTheme {
                news.oGallery?.slideList?.let {
                    SlideShowScreen(slides = it, onShareClick = {
                        share(this, news)
                    })
                }
            }
        }
    }
}

