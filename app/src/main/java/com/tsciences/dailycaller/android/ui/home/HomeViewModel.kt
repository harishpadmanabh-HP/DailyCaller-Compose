package com.tsciences.dailycaller.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.data.preferences.SharedPrefService
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.data.remote.home.MenuItem
import com.tsciences.dailycaller.android.data.repository.HomeRepository
import com.tsciences.dailycaller.android.data.utils.doIfFailure
import com.tsciences.dailycaller.android.data.utils.doIfSuccess
import com.tsciences.dailycaller.android.database.DailyCallerDatabase
import com.tsciences.dailycaller.android.services.Paginator
import com.tsciences.dailycaller.android.ui.commonComponents.utils.UiText
import com.tsciences.dailycaller.android.utils.toStateFlow
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import spotIm.common.SpotException
import spotIm.common.SpotVoidCallback
import spotIm.sdk.SpotIm
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HomeRepository,
    private val sharedPrefService: SharedPrefService,
    private val newsDatabase: DailyCallerDatabase
) : ViewModel() {
    private val newsFirstItem = MutableStateFlow<Item?>(null)
    private val menu = MutableStateFlow<List<MenuItem>>(emptyList())
    private val newsFullItems = MutableStateFlow<List<Item>>(emptyList())
    private val newsFullTabItems = MutableStateFlow<List<Item>>(emptyList())
    private val newsFirstThreeTabItems = MutableStateFlow<List<Item>>(emptyList())
    private val eventsChannel = Channel<HomeEvent>()
    val events get() = eventsChannel.receiveAsFlow()
    private val savedNews = MutableStateFlow(0)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private val isLogin = MutableStateFlow(false)
    private val isTabDevice = MutableStateFlow(false)
    private val newsListPage = MutableStateFlow(0)
    private val isListLoading = MutableStateFlow(false)
    private val isNewsListAtEnd = MutableStateFlow(false)
    var tabName: String? = null
    var isTabOnClick: Boolean = false
    val state = combineTuple(
        newsFirstItem,
        menu,
        isLogin,
        newsFullItems,
        newsListPage,
        isNewsListAtEnd,
        isListLoading,
        savedNews,
        newsFullTabItems,
        newsFirstThreeTabItems
    ).map { (newsFirstItem, menu, isLogin, newsFullItems, newsListPage, isNewsListAtEnd, isListLoading, savedNews, newsFullTabItems, newsFirstThreeTabItems) ->
        HomeState(
            newsFirstItem = newsFirstItem,
            menuItems = menu,
            isLogin = isLogin,
            newsFullItems = newsFullItems,
            listPage = newsListPage,
            hasListEndReached = isNewsListAtEnd,
            loading = isListLoading,
            savedNews = savedNews,
            newsFirstThreeTabItems = newsFirstThreeTabItems,
            newsFullTabItems = newsFullTabItems
        )
    }.toStateFlow(viewModelScope, HomeState())

    private val newsPaginator = Paginator(initialKey = newsListPage.value,
        onLoadingUpdate = { loading -> isListLoading.update { loading } },
        onRequest = { page ->
            if (tabName.equals("Home")) {
                repo.getFullNewsList(
                    pageId = page, maxPosts = 75
                )
            } else {
                repo.getSectionNewsList(
                    type = tabName.toString(), pageId = page, maxPosts = 75
                )
            }
        },
        getNextKey = { newsListPage.value + 1 },
        onError = {
            eventsChannel.send(HomeEvent.ShowUiMessage(it ?: UiText.unknownError()))
        },
        onSuccess = { news, nextPage ->
            if (news.isNotEmpty()) {
                val newsList: MutableList<Item> = news.toMutableList()
                val newsTabList: MutableList<Item> = news.toMutableList()
                if (isTabOnClick) {
                    if (isTabDevice.value) {
                        val firstThreeItems = news.take(3)

                        firstThreeItems.forEach { newsItem ->
                            newsItem.category?.let { newsItem.setCategory(it) }
                            newsTabList.remove(newsItem)
                        }
                        newsFirstThreeTabItems.update { firstThreeItems }
                    } else {
                        val item = news.first()
                        item.category?.let { item.setCategory(it) }
                        newsFirstItem.update { item }
                        newsList.remove(item)
                    }
                }

                if (isTabDevice.value) {
                    newsTabList.forEach { newsItem ->
                        newsItem.category?.let { newsItem.setCategory(it) }
                    }
                    newsFullTabItems.update { it + newsTabList }
                } else {
                    newsList.forEach { newsItem ->
                        newsItem.category?.let { newsItem.setCategory(it) }
                    }
                    newsFullItems.update { it + newsList }
                }
                newsListPage.update { nextPage }

                if (isTabDevice.value) {
                    var newsDesign = 0
                    var isFirstItem = true
                    newsFullTabItems.value.forEach { newsItem ->
                        newsItem.category?.let { newsItem.setCategory(it) }

                        if (newsDesign.equals(4)) {
                            newsDesign = 0
                            isFirstItem = !isFirstItem
                        }
                        newsDesign++
                        newsItem.isFirstItem = !isFirstItem
                    }
                }

            } else {
                eventsChannel.send(HomeEvent.ShowUiMessage(UiText.StringResource(R.string.no_news)))
            }
            isNewsListAtEnd.update { news.isEmpty() }
            _isRefreshing.emit(false)
        })

    init {
        loadMoreNews(TabItems.Home.tab, true)
        getMenuItems()
        getLoginStatus()
        getSavedNewsList()
    }

    fun getLoginStatus() = isLogin.update {
        sharedPrefService.getPianoToken().isNotEmpty()
    }


    fun setLoginStatus(token: String) {
        sharedPrefService.setPianoToken(token)
        isLogin.update { token.isNotEmpty() }
        if (token.isEmpty() && sharedPrefService.getSpotImToken().isNotEmpty()) {
            sharedPrefService.setSpotImToken(token)
            SpotIm.logout(object : SpotVoidCallback {
                override fun onSuccess() {
                    //doSomething...
                }

                override fun onFailure(exception: SpotException) {
                    //doSomething...
                }
            })
        }
    }


    fun getPianoToken() = sharedPrefService.getPianoToken()

    fun setTabDeviceOrNot(isTab: Boolean) {
        isTabDevice.update { isTab }
    }


    fun loadMoreNews(tab: String, isTabClick: Boolean) {
        viewModelScope.launch {
            tabName = tab
            isTabOnClick = isTabClick
            if (isTabClick) {
                newsFullItems.update { emptyList() }
                newsFullTabItems.update { emptyList() }
                newsFirstThreeTabItems.update { emptyList() }
                newsPaginator.reset()
                isNewsListAtEnd.update { false }
                newsListPage.update { 0 }
            }
            newsPaginator.loadNextItems()
        }
    }

    private fun getMenuItems() = viewModelScope.launch {
        isListLoading.update { true }
        repo.getMenuDetails().apply {
            isListLoading.update { false }
            doIfFailure {
                eventsChannel.send(
                    HomeEvent.ShowUiMessage(it ?: UiText.unknownError())
                )
            }
            doIfSuccess { menuItems ->
                menu.update { menuItems.link }
            }
        }
    }

    fun getSavedNewsList() {
        CoroutineScope(Dispatchers.IO).launch {
            if (newsDatabase.newsDao().getNewsList() != null) {
                savedNews.update { newsDatabase.newsDao().countNews() }
            }
        }
    }

    sealed class HomeEvent {
        data class ShowUiMessage(val uiText: UiText) : HomeEvent()
    }

}