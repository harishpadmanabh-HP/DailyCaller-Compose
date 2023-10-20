package com.tsciences.dailycaller.android.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.SpacingMedium
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.ui.commonComponents.*
import com.tsciences.dailycaller.android.ui.commonComponents.utils.dailyCallerScreenContentPadding

@Composable
fun MenuSectionScreenTab(
    viewModel: MenuViewModel = hiltViewModel(),
    menuTerm: String,
    snackbarController: SnackbarController,
    onNewsItemClick: (Item) -> Unit,
    menuTitle: String
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel, snackbarController) {
        viewModel.events.collect { event ->
            when (event) {
                is MenuViewModel.MenuSectionEvent.ShowUiMessage -> {
                    snackbarController.showSnackbar(event.uiText.asString(context))
                }
                else -> {}
            }
        }
    }

    MenuScreenTabContent(
        snackbarController = snackbarController,
        state = state,
        loadNextNewsList = { menuName ->
            viewModel.loadMenuNews(menuName)
        },
        menuTerm = menuTerm,
        viewModel = viewModel,
        onNewsItemClick = onNewsItemClick,
        menuTitle = menuTitle,
        navigateBack = context::popActivity
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MenuScreenTabContent(
    snackbarController: SnackbarController,
    state: MenuState,
    loadNextNewsList: (String) -> Unit,
    menuTerm: String,
    viewModel: MenuViewModel,
    onNewsItemClick: (Item) -> Unit,
    menuTitle: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadMenuNews(menuTerm)
    }
    val refreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(refreshing, {
        viewModel.loadMenuNews(menuTerm)
    })

    DailyCallerScaffold(containerColor = gray,
        snackbarController = snackbarController,
        showLoadingOverlay = state.loading,
        topBar = {
            TransparentCenterAlignedTopAppBar(title = {
                AppBarTitle(
                    logoModifier = Modifier
                        .width(dimensionResource(id = R.dimen.appbar_logo_width))
                        .height((dimensionResource(id = R.dimen.appbar_logo_height))),
                    subLogoModifier = Modifier
                        .width(dimensionResource(id = R.dimen.appbar_sub_logo_width))
                        .height(dimensionResource(id = R.dimen.appbar_sub_logo_height))
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    navigateBack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.content_back_arrow),
                        tint = Color.White,
                        modifier = Modifier.size(dimensionResource(id = R.dimen.back_button))
                    )
                }
            })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dailyCallerScreenContentPadding())
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            Text(
                text = menuTitle,
                fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = SpacingMedium),
                columns = GridCells.Fixed(2)
            ) {
                itemsIndexed(
                    items = state.newsFullItems
                ) { index, news ->
                    if (index >= state.newsFullItems.lastIndex && !state.loading && !state.hasListEndReached) {
                        loadNextNewsList(menuTerm)
                    }

                    if (news.isFirstItem == true) {
                        HomeNewsItemTab1(news = news, onNewsItemClick = { newsItem ->
                            onNewsItemClick(newsItem)
                        })
                    } else {
                        HomeNewsItemTab2(news = news, onNewsItemClick = { newsItem ->
                            onNewsItemClick(newsItem)
                        })
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}



