package com.tsciences.dailycaller.android.ui.newsDetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.tsciences.dailycaller.android.FirebaseAnalyticsEvent.EventRegister
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.application.DailyCallerApplication
import com.tsciences.dailycaller.android.core.theme.DailyCallerTheme
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.core.util.share
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.ui.comment.CommentActivity
import com.tsciences.dailycaller.android.ui.commonComponents.SnackbarController
import com.tsciences.dailycaller.android.ui.commonComponents.rememberSnackbarController
import com.tsciences.dailycaller.android.ui.networknotfound.NetworkNotFoundActivity
import dagger.hilt.android.AndroidEntryPoint
import io.piano.android.composer.Composer
import io.piano.android.composer.listeners.EventTypeListener
import io.piano.android.composer.listeners.ShowTemplateListener
import io.piano.android.composer.listeners.UserSegmentListener
import io.piano.android.composer.model.Event
import io.piano.android.composer.model.ExperienceRequest
import io.piano.android.composer.model.events.EventType
import io.piano.android.composer.model.events.ShowTemplate
import io.piano.android.composer.model.events.UserSegment
import io.piano.android.composer.showtemplate.ComposerJs
import io.piano.android.composer.showtemplate.ShowTemplateController
import io.piano.android.id.PianoId
import io.piano.android.id.PianoIdAuthResultContract
import io.piano.android.id.models.PianoIdAuthFailureResult
import io.piano.android.id.models.PianoIdAuthSuccessResult

@AndroidEntryPoint
class NewsDetailActivity : AppCompatActivity() {
    private val viewModel: NewsDetailViewModel by viewModels()
    private var showTemplateController: ShowTemplateController? = null
    private var request: ExperienceRequest? = null
    private var listeners = listOf<EventTypeListener<out EventType>>()
    private var newsDetails = Item()
    private lateinit var snackBarController: SnackbarController
    private var mInterstitialAd: InterstitialAd? = null
    private var eventRegisterBundle: Bundle? = null
    protected var dailyCallerApplication: DailyCallerApplication? = null
    val authResult = registerForActivityResult(PianoIdAuthResultContract()) { result ->
        when (result) {
            null -> {
                Toast.makeText(this, "Login cancel", Toast.LENGTH_SHORT).show()
            }
            is PianoIdAuthSuccessResult -> {
                val token = result.token
                if (token != null) {
                    viewModel.setLoginStatus(token.accessToken)
                    snackBarController.showSnackbar(resources.getString(R.string.success_logged_in))
                    EventRegister().registerEvent(
                        applicationContext, "Piano_Login", null
                    )

                    runOnUiThread {
                        val templateFragment =
                            supportFragmentManager.findFragmentByTag("ShowTemplateDialogFragment") as DialogFragment?
                        if (templateFragment != null) {
                            supportFragmentManager.beginTransaction().remove(templateFragment)
                                .commitAllowingStateLoss()
                        }
                    }

                    Composer.getInstance().userToken(token.accessToken)
                    if (request != null) {
                        Composer.getInstance().getExperience(request!!, listeners) {
                                // process exception here.
                            }
                    }
                }
            }
            is PianoIdAuthFailureResult -> {
                val e = result.exception
                Toast.makeText(this, e.cause?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppLinkData(intent)
        dailyCallerApplication = this.applicationContext as DailyCallerApplication
        dailyCallerApplication?.setCurrentActivity(this)
    }

    override fun onResume() {
        super.onResume()
        dailyCallerApplication?.setCurrentActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearReferences()
    }

    private fun clearReferences() {
        val currActivity: Activity? = dailyCallerApplication?.getCurrentActivity()
        if (this == currActivity) dailyCallerApplication?.setCurrentActivity(null)
    }

    private fun getAppLinkData(intent: Intent) {
        if (viewModel.getPianoToken().isNotEmpty()) {
            Composer.getInstance().userToken(viewModel.getPianoToken())
        } else {
            Composer.getInstance().userToken("")
        }


        //Save ad Count
        viewModel.setAddCount()

        val newsItem = intent.getStringExtra("news_item")
        val searchNewsItem = intent.getStringExtra("search_news_item")
        val fromSearch = intent.getBooleanExtra("fromSearch", false)
        newsDetails = if (fromSearch) Gson().fromJson(searchNewsItem, Item::class.java)
        else Gson().fromJson(newsItem, Item::class.java)

        setUpEventRegisterBundle(newsDetails)

        viewModel.checkNewsInDatabase(newsDetails)
        getToken()

        registerEvent("Article_Page_viewed")

        setContent {
            snackBarController = rememberSnackbarController()
            DailyCallerTheme {
                NewsDetailScreen(news = newsDetails,
                    snackBarController = snackBarController,
                    viewModel = viewModel,
                    isNewsAvailable = viewModel.isSave.value,
                    onShowCommentClick = { postId ->
                        val commentIntent = Intent(this, CommentActivity::class.java)
                        commentIntent.putExtra("post_id", postId)
                        startActivity(commentIntent)
                    },
                    isTab = isDeviceTablet(this),
                    onUrlClick = { urlIdentifier, url ->
                        if (urlIdentifier.equals("mailUrl")) {
                            //Handle mail Urls
                            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(url)))
                        } else if (urlIdentifier.equals("telUrl")) {
                            //Handle telephony Urls
                            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(url)))
                        } else if (urlIdentifier.equals("validUrl")) {
                            viewModel.getSearchNewsDetails(url)
                        } else {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW, Uri.parse(url)
                            )
                            startActivity(browserIntent)
                        }
                    },
                    navigateToNewsDetailPage = { newsItem ->
                        val newsDetailIntent = Intent(this, NewsDetailActivity::class.java)
                        val item = Gson().toJson(newsItem)
                        newsDetailIntent.putExtra("search_news_item", item)
                        newsDetailIntent.putExtra("fromSearch", true)
                        startActivity(newsDetailIntent)
                    },
                    navigateToNotFoundPage = { isNotFound ->
                        if (isNotFound) {
                            val networkNotFoundIntent =
                                Intent(this, NetworkNotFoundActivity::class.java)
                            startActivity(networkNotFoundIntent)
                        }
                    },
                    onSlideShowClick = { gallery ->
                        registerEvent("Article_Gallery_Viewed")
                        val slideShowActivityIntent = Intent(this, SlideShowActivity::class.java)
                        val news = Gson().toJson(newsDetails)
                        slideShowActivityIntent.putExtra("news", news)
                        startActivity(slideShowActivityIntent)
                    },
                    onShareClick = { news ->
                        share(this, news)
                    },
                    onSaveNewsClick = { isSaveNews, newsItem ->
                        viewModel.saveNews(isSaveNews, newsItem)
                        if (isSaveNews) registerEvent("Article_Offline_Added")
                        else registerEvent("Article_Offline_Removed")
                    })
            }
        }
    }

    private fun getToken() {
        request = newsDetails.link?.let {
            ExperienceRequest.Builder().contentSection("premium-content").debug(true).url(it)
                .build()
        }

        listeners = listOf(ShowTemplateListener { event: Event<ShowTemplate> ->
            if (newsDetails.premiumContent) {
                showTemplateController = ShowTemplateController(event, object : ComposerJs() {

                    @JavascriptInterface
                    override fun login(eventData: String) {
                        if (viewModel.getPianoToken().isEmpty()) {
                            authResult.launch(
                                PianoId.signIn()
                            )
                        } else {

                            PianoId.signOut(viewModel.getPianoToken()) {
                                EventRegister().registerEvent(
                                    applicationContext, "Piano_Logout", null
                                )
                                viewModel.setLoginStatus("")
                                Composer.getInstance().userToken("")
                                authResult.launch(
                                    PianoId.signIn()
                                )
                            }
                        }
                    }

                    @JavascriptInterface
                    override fun close(eventData: String?) {
                        super.close(eventData)
                        Log.d("close", "close")
                        Toast.makeText(applicationContext, "cancel", Toast.LENGTH_SHORT).show()

                    }
                })
                showTemplateController?.show(this)

            }
        }, UserSegmentListener { event: Event<UserSegment> ->
            if (!newsDetails.premiumContent) {
                viewModel.setUnSubscribedState(true)
                displayInterstitialAdd()
            }
        })

        if (request != null) {
            Composer.getInstance().getExperience(request!!, listeners) {
                    Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show()
                    // process exception here.
                }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            if (intent.getBooleanExtra("deeplink", false) == true) {
                finish()
            } else {
                getAppLinkData(intent)
            }
        }
    }

    private fun displayInterstitialAdd() {
        if (viewModel.getAddCount().equals(4) || (viewModel.getAddCount() % 4).equals(0)) {
            val adRequest = AdRequest.Builder().build()

            InterstitialAd.load(this,
                "ca-app-pub-1610338282405979/3517720487",
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        adError.toString().let { Log.d("AdError", it) }
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("AdSuccess", "Ad was loaded.")
                        mInterstitialAd = interstitialAd
                        viewModel.setAddCountAsOne()
                        mInterstitialAd?.show(this@NewsDetailActivity)
                    }
                })

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    Log.d("Add", "Ad dismissed fullscreen content.")
                    mInterstitialAd = null
                }
            }
        }
    }

    private fun setUpEventRegisterBundle(oItem: Item?) {
        if (null != oItem) return
        eventRegisterBundle = Bundle()
        eventRegisterBundle!!.putString("news_title", oItem?.title)
        eventRegisterBundle!!.putString("author_name", oItem?.authorName)
        eventRegisterBundle!!.putString("date_time", oItem?.pubDate)
        eventRegisterBundle!!.putString("category", oItem?.categoryTag)

        eventRegisterBundle!!.putString("is_dc_foundation", setYesOrNo(oItem?.isDcFoundation))
        eventRegisterBundle!!.putString("is_premium_content", setYesOrNo(oItem?.premiumContent))
    }

    private fun setYesOrNo(value: Boolean?): String? {
        return if (value == true) "Yes" else "No"
    }

    private fun registerEvent(eventName: String) {
        if (null != eventRegisterBundle) EventRegister().registerEvent(
            applicationContext, eventName, eventRegisterBundle
        )
    }
}

