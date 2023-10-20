package com.tsciences.dailycaller.android.ui.newsDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.util.getLinkFromSearchItem
import com.tsciences.dailycaller.android.data.preferences.SharedPrefService
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.data.repository.HomeRepository
import com.tsciences.dailycaller.android.data.utils.doIfFailure
import com.tsciences.dailycaller.android.data.utils.doIfSuccess
import com.tsciences.dailycaller.android.database.DailyCallerDatabase
import com.tsciences.dailycaller.android.database.NewsDao
import com.tsciences.dailycaller.android.database.NewsModel
import com.tsciences.dailycaller.android.ui.commonComponents.utils.UiText
import com.tsciences.dailycaller.android.utils.stripHtml
import com.tsciences.dailycaller.android.utils.toStateFlow
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val sharedPrefService: SharedPrefService,
    private val newdDao: NewsDao,
    private val newsDatabase: DailyCallerDatabase,
    private val homeRepo: HomeRepository,
) : ViewModel() {

    private val eventsChannel = Channel<NewsDetailEvent>()
    val events get() = eventsChannel.receiveAsFlow()
    private val isLoading = MutableStateFlow(false)

    val isSave = MutableStateFlow(false)
    private val isNotSubscribed = MutableStateFlow(false)

    val state = combineTuple(
        isNotSubscribed, isLoading
    ).map { (isNotSubscribed, isLoading) ->
        NewsDetailState(
            loading = isLoading, isNotSubscribed = isNotSubscribed
        )
    }.toStateFlow(viewModelScope, NewsDetailState())

    fun setLoginStatus(token: String) {
        sharedPrefService.setPianoToken(token)
    }

    fun getPianoToken() = sharedPrefService.getPianoToken()

    fun setSpotImToken(token: String) {
        sharedPrefService.setSpotImToken(token)
    }

    fun setUnSubscribedState(notSubscribed: Boolean) {
        isNotSubscribed.update { notSubscribed }
    }

    fun getSpotImToken() = sharedPrefService.getSpotImToken()

    fun setAddCount() {
        val adCount: Int = sharedPrefService.getInterstitialAddCount()
        sharedPrefService.setInterstitialAddCount(adCount + 1)
    }

    fun setAddCountAsOne() {
        sharedPrefService.setInterstitialAddCount(1)
    }

    fun getAddCount(): Int = sharedPrefService.getInterstitialAddCount()

    fun checkNewsInDatabase(item: Item) {
        CoroutineScope(Dispatchers.IO).launch {
            val newsModelList = newsDatabase.newsDao().getNews(stripHtml(item.title), item.pubDate)
            if (newsModelList?.size?.equals(0) == false) {
                isSave.update { true }
            }
        }
    }

    sealed class NewsDetailEvent {
        data class ShowUiMessage(val uiText: UiText) : NewsDetailEvent()
        data class GetPostId(val postId: String) : NewsDetailEvent()
        data class SearchDetail(val searchDetail: Item) : NewsDetailEvent()
        data class NotFound(val notFound: Boolean) : NewsDetailEvent()
    }

    fun saveNews(isSaveNews: Boolean, newsItem: Item) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isSaveNews) {
                isSave.update { true }
                newsDatabase.newsDao().removeNews(stripHtml(newsItem.title), newsItem.pubDate)
                Log.d("delete", "delete")
            } else {
                isSave.update { false }
                val newsModel = mapToNewsModel(newsItem)
                newdDao.insertUser(newsModel)
                Log.d("insert", "insert")
            }
        }
    }

    fun getPostIdToDisplaySpotIm(link: String) = viewModelScope.launch {
        isLoading.update { true }
        homeRepo.getSearchNewsDetails(searchLink = getLinkFromSearchItem(link)).apply {
            isLoading.update { false }
            doIfFailure {
                eventsChannel.send(
                    NewsDetailEvent.ShowUiMessage(it ?: UiText.unknownError())
                )
            }
            doIfSuccess { news ->
                if (news != null) {
                    news.category?.let { news.setCategory(it) }
                    news.encoded?.text.let { news.encodedString = it }
                    if ((news.postID != null) && (news.postID?.isNotEmpty() == true)) {
                        eventsChannel.send(
                            NewsDetailEvent.GetPostId(news.postID!!)
                        )
                    }
                }
            }
        }
    }

    fun getSearchNewsDetails(searchLink: String) = viewModelScope.launch {
        isLoading.update { true }
        homeRepo.getSearchNewsDetails(searchLink = getLinkFromSearchItem(searchLink)).apply {
            isLoading.update { false }
            doIfFailure {
                if (it?.equals(R.string.error_network_not_found) == true) {
                    eventsChannel.send(
                        NewsDetailEvent.NotFound(true)
                    )
                }
                eventsChannel.send(
                    NewsDetailEvent.ShowUiMessage(it ?: UiText.unknownError())
                )
            }
            doIfSuccess { news ->
                if (news != null) {
                    news.category?.let { news.setCategory(it) }
                    news.encoded?.text.let { news.encodedString = it }
                    eventsChannel.send(
                        NewsDetailEvent.SearchDetail(news)
                    )
                }
            }
        }
    }

    private fun mapToNewsModel(oItem: Item) = NewsModel(
        newsTitle = stripHtml(oItem.title),
        newsTime = oItem.pubDate,
        authorImage = oItem.authorImage,
        authorName = oItem.authorName,
        category = oItem.categoryTag,
        newsImage = oItem.imageUrl,
        largeImage = oItem.imageLargeUrl,
        mediumImage = oItem.imageMediumUrl,
        newsLink = oItem.link,
        premiumContent = oItem.premiumContent,
        dcFoundation = oItem.isDcFoundation,
        encodedString = oItem.encodedString
    )
}