package com.tsciences.dailycaller.android.ui.home

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.google.gson.Gson
import com.tsciences.dailycaller.android.FirebaseAnalyticsEvent.EventRegister
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.appConstants.AppConstants
import com.tsciences.dailycaller.android.core.theme.DailyCallerTheme
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.ui.commonComponents.rememberSnackbarController
import com.tsciences.dailycaller.android.ui.menu.MenuSectionActivity
import com.tsciences.dailycaller.android.ui.newsDetail.NewsDetailActivity
import com.tsciences.dailycaller.android.ui.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import io.piano.android.id.PianoId
import io.piano.android.id.PianoIdAuthResultContract
import io.piano.android.id.models.PianoIdAuthFailureResult
import io.piano.android.id.models.PianoIdAuthSuccessResult

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    val authResult = registerForActivityResult(PianoIdAuthResultContract()) { result ->
        when (result) {
            null -> {
                Toast.makeText(this, "Login cancel", Toast.LENGTH_SHORT).show()
            }
            is PianoIdAuthSuccessResult -> {
                val token = result.token
                if (token != null) {
                    viewModel.setLoginStatus(token.accessToken)
                    EventRegister().registerEvent(applicationContext, "Piano_Login", null)
                    Toast.makeText(
                        this, resources.getString(R.string.success_logged_in), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "no token", Toast.LENGTH_SHORT).show()
                }
            }
            is PianoIdAuthFailureResult -> {
                val e = result.exception
                Log.d("exceptionGoogle","$e.cause.message")
                Toast.makeText(this, e.cause?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarController = rememberSnackbarController()
            viewModel.setTabDeviceOrNot(isDeviceTablet(this))
            EventRegister().registerEvent(
                applicationContext, "Homepage_Loaded", null
            )

            startEventRegister()
            FirebaseEventRegister()

            DailyCallerTheme {
                if (isDeviceTablet(this)) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
                    HomeTabScreen(viewModel = viewModel,
                        onNewsItemClick = {
                            it.encodedString = it.encoded?.text
                            val intent = Intent(this, NewsDetailActivity::class.java)
                            val item = Gson().toJson(it)
                            intent.putExtra("news_item", item)
                            intent.putExtra("fromSearch", false)
                            startActivity(intent)
                        },
                        onSearchClick = { searchTag ->
                            val intent = Intent(this, SearchActivity::class.java)
                            intent.putExtra("search_tag", searchTag.text)
                            startActivity(intent)
                        },
                        snackBarController = snackbarController,
                        onMenuSubItemClick = { menuTag, menuTitle, isWebLink ->
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menu_tag", menuTag)
                            intent.putExtra("menu_title", menuTitle)
                            intent.putExtra("isWebLink", isWebLink)
                            intent.putExtra("menuItemCategory", AppConstants.MENU_ITEM)
                            startActivity(intent)
                        },
                        onContactClick = {
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menuItemCategory", AppConstants.CONTACT)
                            startActivity(intent)
                        },
                        onPrivacyClick = {
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menuItemCategory", AppConstants.PRIVACY)
                            startActivity(intent)
                        },
                        onPatriotLoginLogoutClick = { isLogin ->
                            if (isLogin) {
                                PianoId.getInstance().signOut(viewModel.getPianoToken()) {
                                    viewModel.setLoginStatus("")
                                    snackbarController.showSnackbar(resources.getString(R.string.success_logged_out))
                                    EventRegister().registerEvent(
                                        applicationContext, "Piano_Logout", null
                                    )
                                }
                            } else {
                                authResult.launch(
                                    PianoId.getInstance().signIn()
                                )
                                EventRegister().registerEvent(
                                    applicationContext, "Piano_Login", null
                                )
                            }
                        },
                        onSavedItemsClick = {
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menuItemCategory", AppConstants.SAVED_NEWS)
                            startActivity(intent)
                        })
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    HomeScreen(viewModel = viewModel,
                        onNewsItemClick = {
                            it.encodedString = it.encoded?.text
                            val intent = Intent(this, NewsDetailActivity::class.java)
                            val item = Gson().toJson(it)
                            intent.putExtra("news_item", item)
                            intent.putExtra("fromSearch", false)
                            startActivity(intent)
                        },
                        onSearchClick = { searchTag ->
                            val intent = Intent(this, SearchActivity::class.java)
                            intent.putExtra("search_tag", searchTag.text)
                            startActivity(intent)
                        },
                        snackBarController = snackbarController,
                        onMenuSubItemClick = { menuTag, menuTitle, isWebLink ->
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menu_tag", menuTag)
                            intent.putExtra("menu_title", menuTitle)
                            intent.putExtra("isWebLink", isWebLink)
                            intent.putExtra("menuItemCategory", AppConstants.MENU_ITEM)
                            startActivity(intent)
                        },
                        onContactClick = {
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menuItemCategory", AppConstants.CONTACT)
                            startActivity(intent)
                        },
                        onPrivacyClick = {
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menuItemCategory", AppConstants.PRIVACY)
                            startActivity(intent)
                        },
                        onPatriotLoginLogoutClick = { isLogin ->
                            if (isLogin) {
                                PianoId.signOut(viewModel.getPianoToken()) {
                                    viewModel.setLoginStatus("")
                                    snackbarController.showSnackbar(resources.getString(R.string.success_logged_out))
                                }
                            } else {
                                authResult.launch(
                                    PianoId.getInstance().signIn()
                                )
                            }
                        },
                        onSavedItemsClick = {
                            val intent = Intent(this, MenuSectionActivity::class.java)
                            intent.putExtra("menuItemCategory", AppConstants.SAVED_NEWS)
                            startActivity(intent)
                        })
                }
            }
        }
    }

    private fun startEventRegister() {
        if (isFirstInstall()) {
            EventRegister().registerEvent(applicationContext, "App_Start_After_Installing", null)
        } else if (isInstallFromUpdate()) {
            EventRegister().registerEvent(applicationContext, "App_Updated_Since_Last_Run", null)
        } else {
            EventRegister().registerEvent(applicationContext, "App_Started_Normal", null)
        }
    }

    fun isFirstInstall(): Boolean {
        return try {
            val firstInstallTime = applicationContext.packageManager.getPackageInfo(
                packageName, 0
            ).firstInstallTime
            val lastUpdateTime = applicationContext.packageManager.getPackageInfo(
                packageName, 0
            ).lastUpdateTime
            firstInstallTime == lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            true
        }
    }

    private fun FirebaseEventRegister() {
        val bundle = Bundle()
        if (isDeviceTablet(this)) bundle.putString(
            "Device_Type", "Android Tab"
        ) else bundle.putString("Device_Type", "Android Phone")
        EventRegister().registerEvent(applicationContext, "Device_Type", bundle)
    }

    private fun isInstallFromUpdate(): Boolean {
        return try {
            val firstInstallTime = applicationContext.packageManager.getPackageInfo(
                packageName, 0
            ).firstInstallTime
            val lastUpdateTime = applicationContext.packageManager.getPackageInfo(
                packageName, 0
            ).lastUpdateTime
            firstInstallTime != lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getLoginStatus()
        viewModel.getSavedNewsList()
    }
}

