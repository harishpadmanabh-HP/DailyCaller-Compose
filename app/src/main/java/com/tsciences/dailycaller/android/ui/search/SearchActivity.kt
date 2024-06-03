package com.tsciences.dailycaller.android.ui.search

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import com.tsciences.dailycaller.android.FirebaseAnalyticsEvent.EventRegister
import com.tsciences.dailycaller.android.core.theme.DailyCallerTheme
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.ui.commonComponents.rememberSnackbarController
import com.tsciences.dailycaller.android.ui.newsDetail.NewsDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    private val viewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchTag = intent.getStringExtra("search_tag")

        if (searchTag != null) {
            val eventBundle = Bundle()
            eventBundle.putString("search_key", searchTag.trim { it <= ' ' })
            EventRegister().registerEvent(applicationContext, "SearchPage_Viewed", eventBundle)
        }
        setContent {
            val snackbarController = rememberSnackbarController()
            DailyCallerTheme {
                if (isDeviceTablet(this)) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                }
                SearchScreen(viewModel = viewModel,
                    searchTag = searchTag.toString(),
                    snackbarController = snackbarController,
                    onNewsItemClick = { searchLink ->
                        viewModel.getSearchNewsDetails(searchLink)
                    },
                    navigateToNewsDetailPage = { newsItem ->
                        val intent = Intent(this, NewsDetailActivity::class.java)
                        val item = Gson().toJson(newsItem)
                        intent.putExtra("search_news_item", item)
                        intent.putExtra("fromSearch", true)
                        startActivity(intent)
                    })
            }
        }
    }
}

