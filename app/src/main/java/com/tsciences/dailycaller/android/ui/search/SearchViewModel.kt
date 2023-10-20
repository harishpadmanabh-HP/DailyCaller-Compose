package com.tsciences.dailycaller.android.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsciences.dailycaller.android.BuildConfig
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.util.getLinkFromSearchItem
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.data.remote.search.SearchItem
import com.tsciences.dailycaller.android.data.repository.HomeRepository
import com.tsciences.dailycaller.android.data.repository.SearchRepository
import com.tsciences.dailycaller.android.data.utils.doIfFailure
import com.tsciences.dailycaller.android.data.utils.doIfSuccess
import com.tsciences.dailycaller.android.services.Paginator
import com.tsciences.dailycaller.android.ui.commonComponents.utils.UiText
import com.tsciences.dailycaller.android.utils.toStateFlow
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: SearchRepository,
    private val homeRepo: HomeRepository
) : ViewModel() {
    private val searchNewsItems = MutableStateFlow<List<SearchItem>>(emptyList())
    private val eventsChannel = Channel<SearchEvent>()
    val events get() = eventsChannel.receiveAsFlow()
    private val isLoading = MutableStateFlow(false)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    var searchTag: String = ""
    private val newsListPage = MutableStateFlow(1)
    private val isNewsListAtEnd = MutableStateFlow(false)

    private val searchNewsDetails = MutableStateFlow<Item?>(null)

    val state = combineTuple(
        searchNewsItems,
        isLoading,
        newsListPage,
        isNewsListAtEnd,
    ).map { (
                searchNewsItems,
                isLoading,
                newsListPage,
                isNewsListAtEnd
            ) ->
        SearchState(
            searchItemList = searchNewsItems,
            loading = isLoading,
            listPage = newsListPage,
            hasListEndReached = isNewsListAtEnd
        )
    }.toStateFlow(viewModelScope, SearchState())

    private val newsPaginator = Paginator(
        initialKey = newsListPage.value,
        onLoadingUpdate = { loading -> isLoading.update { loading } },
        onRequest = { page ->
            val itemNumber: Int
            if (page != 1) itemNumber = ((page - 1) * 10) + 1 else itemNumber = page
            repo.getGoogleSearch(
                searchTag,
                BuildConfig.GOOGLE_API_KEY,
                BuildConfig.GOOGLE_API_CX,
                itemNumber
            )
        },
        getNextKey = { newsListPage.value + 1 },
        onError = {
            isNewsListAtEnd.update { true }
            eventsChannel.send(SearchEvent.ShowUiMessage(it ?: UiText.unknownError()))
        },
        onSuccess = { news, nextPage ->
            if (news.isNotEmpty()) {
                val newsList: MutableList<SearchItem> = news.toMutableList()
                searchNewsItems.update { it + newsList }
                newsListPage.update { nextPage }
            } else {
                eventsChannel.send(SearchEvent.ShowUiMessage(UiText.StringResource(R.string.no_news)))
            }
            isNewsListAtEnd.update { news.isEmpty() }
            _isRefreshing.emit(false)
        }
    )

    fun loadSearchNews(searchTagName: String, isRefreshing: Boolean) {
        viewModelScope.launch {
            searchTag = searchTagName
            if (isRefreshing) {
                searchNewsItems.update { emptyList() }
                newsPaginator.reset()
                isNewsListAtEnd.update { false }
            }

            newsPaginator.loadNextItems()
        }
    }

    fun getSearchNewsDetails(searchLink: String) = viewModelScope.launch {
        isLoading.update { true }
        homeRepo.getSearchNewsDetails(searchLink = getLinkFromSearchItem(searchLink)).apply {
            isLoading.update { false }
            doIfFailure {
                eventsChannel.send(
                    SearchEvent.ShowUiMessage(it ?: UiText.unknownError())
                )
            }
            doIfSuccess { news ->
                if (news != null) {
                    news.category?.let { news.setCategory(it) }
                    news.encoded?.text.let { news.encodedString = it }
                    eventsChannel.send(
                        SearchEvent.SearchDetail(news)
                    )
                }
            }
        }
    }

    sealed class SearchEvent {
        data class ShowUiMessage(val uiText: UiText) : SearchEvent()
        data class SearchDetail(val searchDetail: Item) : SearchEvent()
    }
}