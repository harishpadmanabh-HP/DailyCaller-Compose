package com.tsciences.dailycaller.android.ui.menu

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import com.tsciences.dailycaller.android.FirebaseAnalyticsEvent.EventRegister
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.appConstants.AppConstants
import com.tsciences.dailycaller.android.appConstants.AppConstants.DAILY_CALLER_CONTACT
import com.tsciences.dailycaller.android.appConstants.AppConstants.DELETION_URL
import com.tsciences.dailycaller.android.appConstants.AppConstants.PRIVACY_POLICY_URL
import com.tsciences.dailycaller.android.core.theme.DailyCallerTheme
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.ui.commonComponents.rememberSnackbarController
import com.tsciences.dailycaller.android.ui.newsDetail.NewsDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuSectionActivity : ComponentActivity() {
    private val viewModel: MenuViewModel by viewModels()
    var menuItemCategory: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuItemCategory = intent.getStringExtra("menuItemCategory")
        setContent {
            if (isDeviceTablet(this)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            val snackbarController = rememberSnackbarController()
            viewModel.setTabDeviceOrNot(isDeviceTablet(this))
            DailyCallerTheme {
                if (menuItemCategory.equals(AppConstants.MENU_ITEM)) {
                    val menuTag = intent.getStringExtra("menu_tag")
                    val menuTitle = intent.getStringExtra("menu_title")
                    val isWebLink = intent.getBooleanExtra("isWebLink", false)
                    if (!isWebLink) {
                        if (isDeviceTablet(this)) {
                            MenuSectionScreenTab(viewModel = viewModel,
                                menuTerm = menuTag ?: "",
                                snackbarController = snackbarController,
                                menuTitle = menuTitle ?: "",
                                onNewsItemClick = { item ->
                                    item.encodedString = item.encoded?.text
                                    val intent = Intent(this, NewsDetailActivity::class.java)
                                    val newsItem = Gson().toJson(item)
                                    intent.putExtra("news_item", newsItem)
                                    intent.putExtra("fromSearch", false)
                                    startActivity(intent)
                                })
                        } else {
                            MenuSectionScreen(viewModel = viewModel,
                                menuTerm = menuTag ?: "",
                                snackbarController = snackbarController,
                                menuTitle = menuTitle ?: "",
                                onNewsItemClick = { item ->
                                    item.encodedString = item.encoded?.text
                                    val intent = Intent(this, NewsDetailActivity::class.java)
                                    val newsItem = Gson().toJson(item)
                                    intent.putExtra("news_item", newsItem)
                                    intent.putExtra("fromSearch", false)
                                    startActivity(intent)
                                })
                        }
                    } else {
                        if (menuTag != null) {
                            if (menuTag.contains("/stream/")) {
                                StreamDocumentaryScreen(viewModel = viewModel,
                                    menuTerm = menuTag ?: "",
                                    snackbarController = snackbarController,
                                    menuTitle = menuTitle ?: "",
                                    navigateToVideoScreen = { stream ->
                                        val promoVideoId = extractVimeoID(stream.videoUrl) ?: ""
                                        val fullVideoId = extractVimeoID(stream.fullVideoUrl) ?: ""
                                        val intent = Intent(this, LocalPlayerActivity::class.java)
                                        intent.putExtra("promoVideoId", promoVideoId)
                                        intent.putExtra("fullVideoId", fullVideoId)
                                        intent.putExtra("description", stream.summary)
                                        intent.putExtra("title", stream.title)
                                        intent.putExtra("subtitle", stream.name)
                                        intent.putExtra("shouldStart", false)
                                        intent.putExtra("image", stream.thumImage)
                                        startActivity(intent)
                                    })
                            } else {
                                MenuWebScreen(
                                    viewModel = viewModel,
                                    menuLink = menuTag.toString(),
                                    snackbarController = snackbarController
                                )
                            }
                        }

                    }
                } else if (menuItemCategory.equals(AppConstants.CONTACT)) {
                    MenuContactScreen(
                        snackbarController = snackbarController, viewModel = viewModel
                    ) {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse("mailto:")
                            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(DAILY_CALLER_CONTACT))
                            startActivity(intent)
                        } catch (ex: ActivityNotFoundException) {
                            snackbarController.showSnackbar(getString(R.string.no_email_app))
                        }
                    }
                } else if (menuItemCategory.equals(AppConstants.PRIVACY)) {
                    MenuPrivacyScreen(snackbarController = snackbarController,
                        viewModel = viewModel,
                        onDeleteAccountClick = {
                            EventRegister().registerEvent(
                                applicationContext, "Account_Deletion_Viewed", null
                            )
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW, Uri.parse(DELETION_URL)
                            )
                            startActivity(browserIntent)
                        },
                        onPrivacyPolicyClick = {
                            EventRegister().registerEvent(
                                applicationContext, "Privacy_Policy_Viewed", null
                            )
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)
                            )
                            startActivity(browserIntent)
                        })
                } else if (menuItemCategory.equals(AppConstants.SAVED_NEWS)) {
                    SavedNewsScreen(
                        snackbarController = snackbarController, viewModel = viewModel
                    ) { savedNews ->
                        val intent = Intent(this, NewsDetailActivity::class.java)
                        val newsItem = viewModel.mapToItem(savedNews)
                        val newsItemGson = Gson().toJson(newsItem)
                        intent.putExtra("news_item", newsItemGson)
                        intent.putExtra("fromSearch", false)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    fun extractVimeoID(url: String): String? {
        val pattern = """vimeo\.com/(?:video/)?(\d+)""".toRegex()
        val matchResult = pattern.find(url)
        return matchResult?.groupValues?.get(1)
    }

    override fun onResume() {
        super.onResume()
        Log.d("onSave", "onSave")
        viewModel.getSavedNewsList()

        val bundle = Bundle()
        bundle.putInt("Offline_News_Screen_Size", viewModel.state.value.savedNews.size)

        EventRegister().registerEvent(applicationContext, "Offline_News_Screen_Viewed", bundle)
    }
}


