package com.tsciences.dailycaller.android.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.data.preferences.SharedPrefService
import com.tsciences.dailycaller.android.data.remote.documentaries.Stream
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.data.remote.vimeo.VimeoUrlResponse
import com.tsciences.dailycaller.android.data.repository.MenuSectionRepository
import com.tsciences.dailycaller.android.data.repository.StreamRepository
import com.tsciences.dailycaller.android.data.repository.VimeoRepository
import com.tsciences.dailycaller.android.data.utils.doIfFailure
import com.tsciences.dailycaller.android.data.utils.doIfSuccess
import com.tsciences.dailycaller.android.database.DailyCallerDatabase
import com.tsciences.dailycaller.android.database.NewsModel
import com.tsciences.dailycaller.android.services.Paginator
import com.tsciences.dailycaller.android.ui.commonComponents.utils.UiText
import com.tsciences.dailycaller.android.utils.stripHtml
import com.tsciences.dailycaller.android.utils.toStateFlow
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repo: MenuSectionRepository,
    private val streamRepository: StreamRepository,
    private val vimeoRepository: VimeoRepository,
    private val sharedPrefService: SharedPrefService,
    private val newsDatabase: DailyCallerDatabase
) : ViewModel() {
    private val eventsChannel = Channel<MenuSectionEvent>()
    val events get() = eventsChannel.receiveAsFlow()
    private val streams = MutableStateFlow<List<Stream>>(emptyList())
    private val title = MutableStateFlow("")
    private val vimeoResponse = MutableStateFlow<VimeoUrlResponse?>(null)
    private val newsListPage = MutableStateFlow(0)
    private val isListLoading = MutableStateFlow(false)
    private val isNewsListAtEnd = MutableStateFlow(false)
    private val newsFullItems = MutableStateFlow<List<Item>>(emptyList())
    private val savedNews = MutableStateFlow<List<NewsModel?>>(emptyList())
    private val _isRefreshing = MutableStateFlow(false)
    private val isTabDevice = MutableStateFlow(false)
    val currentlyPlayingIndex = MutableStateFlow<Int?>(null)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    var menuTag: String = ""
    val state = combineTuple(
        newsFullItems, isListLoading, newsListPage, isNewsListAtEnd, savedNews, streams, title
    ).map { (newsFullItems, isListLoading, newsListPage, isNewsListAtEnd, savedNews, streams, title) ->
        MenuState(
            newsFullItems = newsFullItems,
            loading = isListLoading,
            listPage = newsListPage,
            hasListEndReached = isNewsListAtEnd,
            savedNews = savedNews,
            streams = streams,
            menuTitle = title
        )
    }.toStateFlow(viewModelScope, MenuState())

    private val newsPaginator = Paginator(initialKey = newsListPage.value,
        onLoadingUpdate = { loading -> isListLoading.update { loading } },
        onRequest = { page ->
            if (menuTag == "two-sides" || menuTag == "premium-content") {
                repo.getMenuNewsPremiumTwosides(
                    menuTerm = menuTag, pageId = page + 1, maxPosts = 75
                )
            } else {
                repo.getMenuNewsList(
                    menuTerm = menuTag, pageId = page, maxPosts = 75
                )
            }

        },
        getNextKey = { newsListPage.value + 1 },
        onError = {
            eventsChannel.send(MenuSectionEvent.ShowUiMessage(it ?: UiText.unknownError()))
        },
        onSuccess = { news, nextPage ->
            if (news.isNotEmpty()) {
                val newsList: MutableList<Item> = news.toMutableList()

                newsList.forEach { newsItem ->
                    newsItem.category?.let { newsItem.setCategory(it) }
                }
                newsFullItems.update { it + newsList }
                newsListPage.update { nextPage }

                if (isTabDevice.value) {
                    var newsDesign = 0
                    var isFirstItem = true
                    newsFullItems.value.forEach { newsItem ->
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
                eventsChannel.send(MenuSectionEvent.ShowUiMessage(UiText.StringResource(R.string.no_news)))
            }
            isNewsListAtEnd.update { news.isEmpty() }
            _isRefreshing.emit(false)

        })

    init {
        getSavedNewsList()
    }

    fun loadMenuNews(menuTerm: String) {
        viewModelScope.launch {
            menuTag = menuTerm
            newsPaginator.loadNextItems()
        }
    }

    fun setTabDeviceOrNot(isTab: Boolean) {
        isTabDevice.update { isTab }
    }

    fun getSavedNewsList() {
        CoroutineScope(Dispatchers.IO).launch {
            savedNews.update { emptyList() }
            if (newsDatabase.newsDao().getNewsList()?.isNotEmpty() == true) {
                savedNews.update { newsDatabase.newsDao().getNewsList()!! }
            } else {
                eventsChannel.send(
                    MenuSectionEvent.SavedNews("No Saved News")
                )
            }
        }
    }

    sealed class MenuSectionEvent {
        data class ShowUiMessage(val uiText: UiText) : MenuSectionEvent()
        data class VideoUrl(val videoUrl: String) : MenuSectionEvent()
        data class SavedNews(val noSavedNews: String) : MenuSectionEvent()
    }

    fun getStreamItems(menuTerm: String, menuTitle: String, seeMoreDocumentaries: Boolean = false) =
        viewModelScope.launch {
            isListLoading.update { true }
            streamRepository.getStreamDocumentaries().apply {
                isListLoading.update { false }
                doIfFailure {
                    eventsChannel.send(
                        MenuSectionEvent.ShowUiMessage(it ?: UiText.unknownError())
                    )
                }
                doIfSuccess { streamVideos ->
                    if (seeMoreDocumentaries) {
                        streams.update { streamVideos.stream }
                        title.update { "Documentaries" }
                    } else {
                        val sectionCollection = menuTerm.split("/")
                        if (sectionCollection.size <= 3) {
                            streams.update { streamVideos.stream }
                            title.update { "Documentaries" }
                        } else {
                            streams.update { streamVideos.stream.filter { it.slug == sectionCollection[2] } }
                            title.update { menuTitle }
                        }
                    }
                }
            }
        }

    fun mapToItem(newsModel: NewsModel) = Item(
        title = stripHtml(newsModel.newsTitle),
        pubDate = newsModel.newsTime,
        authorImage = newsModel.authorImage,
        authorName = newsModel.authorName,
        categoryTag = newsModel.category,
        imageUrl = newsModel.newsImage,
        imageLargeUrl = newsModel.largeImage,
        imageMediumUrl = newsModel.mediumImage,
        link = newsModel.newsLink,
        premiumContent = newsModel.premiumContent ?: false,
        isDcFoundation = newsModel.dcFoundation ?: false,
        encodedString = newsModel.encodedString
    )
}