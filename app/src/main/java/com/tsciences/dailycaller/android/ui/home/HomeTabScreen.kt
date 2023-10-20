package com.tsciences.dailycaller.android.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.appConstants.AppConstants
import com.tsciences.dailycaller.android.core.theme.*
import com.tsciences.dailycaller.android.core.util.*
import com.tsciences.dailycaller.android.data.remote.home.*
import com.tsciences.dailycaller.android.ui.commonComponents.*
import com.tsciences.dailycaller.android.utils.stripHtml
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun HomeTabScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNewsItemClick: (Item) -> Unit,
    onSearchClick: (TextFieldValue) -> Unit,
    snackBarController: SnackbarController,
    onMenuSubItemClick: (String, String, Boolean) -> Unit,
    onContactClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSavedItemsClick: (newsSize: Int) -> Unit,
    onPatriotLoginLogoutClick: (isLogin: Boolean) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel, snackBarController) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeViewModel.HomeEvent.ShowUiMessage -> {
                    snackBarController.showSnackbar(event.uiText.asString(context))
                }
            }
        }
    }

    HomeTabScreenContent(
        state = state,
        snackBarController = snackBarController,
        loadNextNewsList = { tab, isTabClick ->
            viewModel.loadMoreNews(tab, isTabClick)
        },
        viewModel = viewModel,
        onNewsItemClick = { item ->
            onNewsItemClick(item)
        },
        onSearchClick = { searchTag ->
            onSearchClick(searchTag)
        },
        onMenuSubItemClick = onMenuSubItemClick,
        onContactClick = onContactClick,
        onPrivacyClick = onPrivacyClick,
        onSavedItemsClick = onSavedItemsClick,
        onPatriotLoginLogoutClick = onPatriotLoginLogoutClick
    )
}

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class
)
@Composable
fun HomeTabScreenContent(
    state: HomeState,
    snackBarController: SnackbarController,
    loadNextNewsList: (String, Boolean) -> Unit,
    viewModel: HomeViewModel,
    onNewsItemClick: (Item) -> Unit,
    onSearchClick: (TextFieldValue) -> Unit,
    onMenuSubItemClick: (String, String, Boolean) -> Unit,
    onContactClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSavedItemsClick: (newsSize: Int) -> Unit,
    onPatriotLoginLogoutClick: (isLogin: Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var tabName by remember { mutableStateOf(TabItems.Home.tab) }
    val refreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(refreshing, {
        tabName.let {
            viewModel.loadMoreNews(
                it, true
            )
        }
    })
    val collapseState = rememberCollapsingToolbarScaffoldState()

    val isToolbarVisible: Boolean by remember {
        derivedStateOf {
            collapseState.toolbarState.progress == 1f
        }
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = true) {
        coroutineScope.launch {
            when (drawerState.currentValue) {
                DrawerValue.Closed -> {
                    context.findActivity()?.finish()
                }
                DrawerValue.Open -> {
                    drawerState.close()
                }
            }
        }
    }
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        SideNav(state = state,
            menuItems = state.menuItems,
            isLogin = state.isLogin,
            onMenuSubItemClick = { menuLink, menuTitle, isWeb ->
                onMenuSubItemClick(menuLink, menuTitle, isWeb)
                coroutineScope.launch {
                    when (drawerState.currentValue) {
                        DrawerValue.Open -> {
                            drawerState.close()
                        }
                        else -> {}
                    }
                }
            },
            snackBarController = snackBarController,
            onSearchClick = { searchText ->
                onSearchClick(searchText)
                coroutineScope.launch {
                    when (drawerState.currentValue) {
                        DrawerValue.Open -> {
                            drawerState.close()
                        }
                        else -> {}
                    }
                }
            },
            onHomeClick = {
                coroutineScope.launch {
                    when (drawerState.currentValue) {
                        DrawerValue.Open -> {
                            drawerState.close()
                        }
                        else -> {}
                    }
                }
            },
            onContactClick = {
                onContactClick()
                coroutineScope.launch {
                    when (drawerState.currentValue) {
                        DrawerValue.Open -> {
                            drawerState.close()
                        }
                        else -> {}
                    }
                }
            },
            onPrivacyClick = {
                onPrivacyClick()
                coroutineScope.launch {
                    when (drawerState.currentValue) {
                        DrawerValue.Open -> {
                            drawerState.close()
                        }
                        else -> {}
                    }
                }
            },
            onPatriotLoginLogoutClick = { isLogin ->
                onPatriotLoginLogoutClick(isLogin)
                coroutineScope.launch {
                    when (drawerState.currentValue) {
                        DrawerValue.Open -> {
                            drawerState.close()
                        }
                        else -> {}
                    }
                }
            },

            onSavedItemsClick = { savedNewsSize ->
                if (savedNewsSize == 0) {
                    snackBarController.showSnackbar(context.resources.getString(R.string.no_saved_news))
                } else {
                    onSavedItemsClick(savedNewsSize)
                }
                coroutineScope.launch {
                    when (drawerState.currentValue) {
                        DrawerValue.Open -> {
                            drawerState.close()
                        }
                        else -> {}
                    }
                }
            })
    }) {
        DailyCallerScaffold(
            containerColor = gray, topBar = {
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
                        coroutineScope.launch {
                            when (drawerState.currentValue) {
                                DrawerValue.Closed -> {
                                    drawerState.open()
                                }
                                DrawerValue.Open -> {
                                    drawerState.close()
                                }
                            }
                        }
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                            contentDescription = stringResource(R.string.content_desc_menu),
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp),
                            tint = Color.White
                        )
                    }
                })
            }, snackbarController = snackBarController, showLoadingOverlay = state.loading
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .pullRefresh(pullRefreshState)

            ) {
                CollapsingToolbarScaffold(modifier = Modifier.fillMaxSize(),
                    state = collapseState,
                    scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                    toolbarModifier = Modifier.background(gray),
                    toolbar = {

                        var searchText by remember { mutableStateOf(TextFieldValue("")) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(start = SpacingMedium, end = SpacingMedium)
                                .clip(MaterialTheme.shapes.medium)
                                .background(Color.White)
                                .clip(MaterialTheme.shapes.extraSmall)

                        ) {
                            ConstraintLayout(
                                modifier = Modifier
                                    .fillMaxWidth(.5f)
                                    .padding(end = SpacingXSmall)


                            ) {
                                if (state.newsFirstThreeTabItems.isNotEmpty()) {
                                    val news = state.newsFirstThreeTabItems.get(0)
                                    val (collapsibleNewsImage, collapsibleNewsTransparentView, categoryTag, title, authorName, whiteDotImage, time, authorImage, patriotImage) = createRefs()
                                    AsyncImage(model = news.imageLargeUrl
                                        ?: news.image?.loc?.text,
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .graphicsLayer {
                                                alpha = collapseState.toolbarState.progress
                                            }
                                            .fillMaxHeight(.75f)
                                            .constrainAs(collapsibleNewsImage) {}
                                            .clickable(isToolbarVisible) {
                                                news.let {
                                                    onNewsItemClick(
                                                        it
                                                    )
                                                }
                                            })

                                    if (news.premiumContent) {
                                        Image(
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(50.dp)
                                                .constrainAs(patriotImage) {
                                                    end.linkTo(parent.end, 10.dp)
                                                    top.linkTo(parent.top, 10.dp)

                                                },
                                            painter = painterResource(id = R.drawable.patriots),
                                            contentDescription = "Section Gradient",
                                        )
                                    }

                                    Text(text = news.categoryTag.toString(),

                                        modifier = Modifier
                                            .background(
                                                color = getCategoryColor(state.newsFirstItem?.categoryTag.toString()),
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .constrainAs(categoryTag) {
                                                top.linkTo(collapsibleNewsImage.bottom)
                                                bottom.linkTo(collapsibleNewsImage.bottom)
                                                start.linkTo(
                                                    collapsibleNewsImage.start, 10.dp
                                                )
                                            }
                                            .padding(horizontal = 5.dp, vertical = 2.dp),
                                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                        fontSize = 15.sp,
                                        color = Color.White)
                                    Text(
                                        text = stripHtml(news.title),
                                        modifier = Modifier.constrainAs(title) {
                                            top.linkTo(categoryTag.bottom, 5.dp)
                                            start.linkTo(
                                                collapsibleNewsImage.start,
                                                10.dp
                                            )
                                        },
                                        fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),
                                        maxLines = 2,
                                        fontSize = 18.sp,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = news.authorName.toString(),

                                        modifier = Modifier.constrainAs(authorName) {
                                            top.linkTo(title.bottom, 10.dp)
                                            start.linkTo(
                                                collapsibleNewsImage.start,
                                                10.dp
                                            )
                                            bottom.linkTo(parent.bottom, 10.dp)
                                        },
                                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )

                                    Image(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .constrainAs(whiteDotImage) {
                                                start.linkTo(authorName.end, 10.dp)
                                                bottom.linkTo(authorName.bottom)
                                                top.linkTo(authorName.top)

                                            },
                                        contentScale = ContentScale.FillBounds,
                                        painter = painterResource(id = R.drawable.ic_fiber_manual_record),
                                        contentDescription = "white dot"
                                    )

                                    getTime(
                                        news.pubDate,
                                        AppConstants.DATE_FORMAT_TIME_STAMP
                                    )?.let {
                                        Text(
                                            text = it,
                                            modifier = Modifier.constrainAs(time) {
                                                start.linkTo(whiteDotImage.end, 10.dp)
                                                bottom.linkTo(whiteDotImage.bottom)
                                                top.linkTo(whiteDotImage.top)
                                            },
                                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                    }
                                    AsyncImage(model = news.authorImage,
                                        contentDescription = "",
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier
                                            .size(30.dp)
                                            .constrainAs(authorImage) {
                                                bottom.linkTo(time.bottom)
                                                top.linkTo(time.top)
                                                end.linkTo(parent.end, 10.dp)
                                            }
                                            .clip(MaterialTheme.shapes.extraSmall))
                                }
                            }
                            Column(

                            ) {
                                ConstraintLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(.5f)
                                        .padding(start = SpacingXSmall, bottom = SpacingXSmall)
                                ) {
                                    if (state.newsFirstThreeTabItems.size > 1) {
                                        val news = state.newsFirstThreeTabItems.get(1)
                                        val (collapsibleNewsImage, collapsibleNewsTransparentView, categoryTag, title, authorName, whiteDotImage, time, authorImage, patriotImage) = createRefs()
                                        AsyncImage(model = news.imageLargeUrl
                                            ?: news.image?.loc?.text,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .graphicsLayer {
                                                    alpha = collapseState.toolbarState.progress
                                                }
                                                .fillMaxHeight()
                                                .constrainAs(collapsibleNewsImage) {}
                                                .clickable(isToolbarVisible) {
                                                    onNewsItemClick(
                                                        news
                                                    )
                                                })

                                        if (news.premiumContent) {
                                            Image(
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .height(50.dp)
                                                    .constrainAs(patriotImage) {
                                                        end.linkTo(parent.end, 10.dp)
                                                        top.linkTo(parent.top, 10.dp)
                                                    },
                                                painter = painterResource(id = R.drawable.patriots),
                                                contentDescription = "Section Gradient",
                                            )
                                        }

                                        Image(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(dimensionResource(id = R.dimen.home_firstItemViewHeight))
                                                .constrainAs(collapsibleNewsTransparentView) {
                                                    bottom.linkTo(collapsibleNewsImage.bottom)
                                                },
                                            contentScale = ContentScale.FillBounds,
                                            painter = painterResource(id = R.drawable.section_gradient),
                                            alignment = Alignment.BottomCenter,
                                            contentDescription = "Section Gradient",

                                            )

                                        Text(text = news.categoryTag.toString(),

                                            modifier = Modifier
                                                .background(
                                                    color = getCategoryColor(state.newsFirstItem?.categoryTag.toString()),
                                                    shape = RoundedCornerShape(5.dp)
                                                )
                                                .constrainAs(categoryTag) {
                                                    top.linkTo(collapsibleNewsImage.top, 10.dp)
                                                    start.linkTo(
                                                        collapsibleNewsTransparentView.start, 10.dp
                                                    )
                                                }
                                                .padding(horizontal = 5.dp, vertical = 2.dp),
                                            fontSize = 15.sp,
                                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                            color = Color.White)
                                        Text(
                                            text = stripHtml(news.title),
                                            modifier = Modifier.constrainAs(title) {
                                                bottom.linkTo(authorName.top, 5.dp)
                                                start.linkTo(
                                                    collapsibleNewsTransparentView.start,
                                                    10.dp
                                                )
                                            },
                                            maxLines = 2,
                                            fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )

                                        Text(
                                            text = news.authorName.toString(),

                                            modifier = Modifier.constrainAs(authorName) {
                                                bottom.linkTo(parent.bottom, 20.dp)
                                                start.linkTo(
                                                    collapsibleNewsTransparentView.start,
                                                    10.dp
                                                )
                                            },
                                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )

                                        Image(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .constrainAs(whiteDotImage) {
                                                    start.linkTo(authorName.end, 10.dp)
                                                    bottom.linkTo(authorName.bottom)
                                                    top.linkTo(authorName.top)
                                                },
                                            contentScale = ContentScale.FillBounds,
                                            painter = painterResource(id = R.drawable.ic_fiber_manual_record),
                                            contentDescription = "white dot",

                                            )

                                        getTime(
                                            news.pubDate,
                                            AppConstants.DATE_FORMAT_TIME_STAMP
                                        )?.let {
                                            Text(
                                                text = it,
                                                modifier = Modifier.constrainAs(time) {
                                                    start.linkTo(whiteDotImage.end, 10.dp)
                                                    bottom.linkTo(parent.bottom, 20.dp)
                                                },
                                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                                fontSize = 14.sp,
                                                color = Color.White
                                            )
                                        }
                                        AsyncImage(model = news.authorImage,
                                            contentDescription = "",
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .constrainAs(authorImage) {
                                                    bottom.linkTo(time.bottom)
                                                    top.linkTo(time.top)
                                                    end.linkTo(parent.end, 10.dp)
                                                }
                                                .clip(MaterialTheme.shapes.extraSmall))
                                    }
                                }
                                ConstraintLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(start = SpacingXSmall, top = SpacingXSmall)


                                ) {
                                    if (state.newsFirstThreeTabItems.size > 2) {
                                        val news = state.newsFirstThreeTabItems.get(2)
                                        val (collapsibleNewsImage, collapsibleNewsTransparentView, categoryTag, title, authorName, whiteDotImage, time, authorImage, patriotImage) = createRefs()
                                        AsyncImage(model = news.imageLargeUrl
                                            ?: news.image?.loc?.text,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .graphicsLayer {
                                                    alpha = collapseState.toolbarState.progress
                                                }
                                                .fillMaxHeight()
                                                .constrainAs(collapsibleNewsImage) {}
                                                .clickable(isToolbarVisible) {
                                                    onNewsItemClick(
                                                        news
                                                    )
                                                })

                                        if (news.premiumContent == true) {
                                            Image(
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .height(50.dp)
                                                    .constrainAs(patriotImage) {
                                                        end.linkTo(parent.end, 10.dp)
                                                        top.linkTo(parent.top, 10.dp)
                                                    },
                                                painter = painterResource(id = R.drawable.patriots),
                                                contentDescription = "Section Gradient",
                                            )
                                        }

                                        Image(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(dimensionResource(id = R.dimen.home_firstItemViewHeight))
                                                .constrainAs(collapsibleNewsTransparentView) {
                                                    bottom.linkTo(collapsibleNewsImage.bottom)
                                                },
                                            contentScale = ContentScale.FillBounds,
                                            painter = painterResource(id = R.drawable.section_gradient),
                                            alignment = Alignment.BottomCenter,
                                            contentDescription = "Section Gradient",

                                            )

                                        Text(text = news.categoryTag.toString(),

                                            modifier = Modifier
                                                .background(
                                                    color = getCategoryColor(state.newsFirstItem?.categoryTag.toString()),
                                                    shape = RoundedCornerShape(5.dp)
                                                )
                                                .constrainAs(categoryTag) {
                                                    top.linkTo(collapsibleNewsImage.top, 10.dp)
                                                    start.linkTo(
                                                        collapsibleNewsTransparentView.start, 10.dp
                                                    )
                                                }
                                                .padding(horizontal = 5.dp, vertical = 2.dp),
                                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                            fontSize = 15.sp,
                                            color = Color.White)
                                        Text(
                                            text = stripHtml(news.title),
                                            modifier = Modifier.constrainAs(title) {
                                                bottom.linkTo(authorName.top, 5.dp)
                                                start.linkTo(
                                                    collapsibleNewsTransparentView.start,
                                                    10.dp
                                                )
                                            },
                                            maxLines = 2,
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),

                                            color = Color.White
                                        )

                                        Text(
                                            text = news.authorName.toString(),

                                            modifier = Modifier.constrainAs(authorName) {
                                                bottom.linkTo(parent.bottom, 20.dp)
                                                start.linkTo(
                                                    collapsibleNewsTransparentView.start,
                                                    10.dp
                                                )
                                            },
                                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )

                                        Image(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .constrainAs(whiteDotImage) {
                                                    start.linkTo(authorName.end, 10.dp)
                                                    bottom.linkTo(authorName.bottom)
                                                    top.linkTo(authorName.top)
                                                },
                                            contentScale = ContentScale.FillBounds,
                                            painter = painterResource(id = R.drawable.ic_fiber_manual_record),
                                            contentDescription = "white dot",

                                            )

                                        getTime(
                                            news.pubDate,
                                            AppConstants.DATE_FORMAT_TIME_STAMP
                                        )?.let {
                                            Text(
                                                text = it,
                                                modifier = Modifier.constrainAs(time) {
                                                    start.linkTo(whiteDotImage.end, 10.dp)
                                                    bottom.linkTo(parent.bottom, 20.dp)
                                                },
                                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                                fontSize = 14.sp,
                                                color = Color.White
                                            )
                                        }
                                        AsyncImage(model = news.authorImage,
                                            contentDescription = "",
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier
                                                .size(30.dp)
                                                .constrainAs(authorImage) {
                                                    bottom.linkTo(time.bottom)
                                                    top.linkTo(time.top)
                                                    end.linkTo(parent.end, 10.dp)
                                                }
                                                .clip(MaterialTheme.shapes.extraSmall))
                                    }
                                }
                            }

                        }

                        if (isToolbarVisible) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                            )
                        } else {
                            BasicTextField(modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(
                                    start = SpacingMedium,
                                    end = SpacingMedium,
                                )

                                .background(searchBackground)
                                .clickable { },
                                value = searchText,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = {
                                    focusManager.clearFocus()
                                    if (searchText.text.isNotEmpty()) {
                                        onSearchClick(searchText)
                                        searchText = TextFieldValue("")
                                    } else {
                                        snackBarController.showSnackbar("Please enter a search key")
                                    }

                                }),
                                onValueChange = {
                                    searchText = it
                                },
                                singleLine = true,
                                cursorBrush = SolidColor(Color.Red),
                                textStyle = LocalTextStyle.current.copy(
                                    colorBlack,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_light))
                                ),
                                decorationBox = { innerTextField ->
                                    Row(
                                        modifier = Modifier.padding(
                                            start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp
                                        ), verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.search_icon),
                                            contentDescription = "search",
                                            modifier = Modifier.padding(end = 10.dp)
                                        )
                                        Box() {
                                            if (searchText.text.isEmpty()) Text(
                                                stringResource(id = R.string.search),
                                                color = colorBlack,
                                                fontFamily = FontFamily(Font(R.font.roboto_light)),
                                                fontSize = 18.sp
                                            )
                                            innerTextField()
                                        }

                                    }
                                })
                        }


                    }) {
                    var tabIndex by remember { mutableStateOf(0) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = SpacingMedium,
                                start = SpacingMedium,
                                end = SpacingMedium
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .background(
                                    color = Color.White
                                )
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp,
                                    start = SpacingMedium,
                                    end = SpacingMedium
                                )
                        ) {
                            Row() {

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append("Latest")
                                        }
                                        append(" ")
                                        append("News ")
                                    },
                                    color = Color.Black,
                                    fontSize = 20.sp, modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp, end = 10.dp)
                                )

                                ScrollableTabRow(selectedTabIndex = tabIndex,
                                    edgePadding = 0.dp,
                                    modifier = Modifier.padding(start = 40.dp),
                                    divider = {},
                                    indicator = {}) {
                                    TabItems.values().forEachIndexed { index, tabItems ->

                                        Tab(
                                            text = {
                                                Text(
                                                    tabItems.tab.uppercase(),
                                                    color = Color.Black,
                                                    fontSize = 18.sp,
                                                    modifier = Modifier.padding(
                                                        start = 40.dp,
                                                        end = 40.dp
                                                    )
                                                )
                                            },
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .background(
                                                    color = if (tabIndex == index) Color.White else Color.Transparent,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                                .border(
                                                    width = 2.dp,
                                                    color = if (tabIndex == index) colorRed else gray,
                                                    shape = RoundedCornerShape(15.dp)
                                                ),

                                            selected = tabIndex == index,
                                            onClick = {
                                                tabIndex = index
                                                when (tabIndex) {
                                                    0 -> {
                                                        tabName = TabItems.Home.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                    1 -> {
                                                        tabName = TabItems.Us.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                    2 -> {
                                                        tabName = TabItems.Business.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                    3 -> {
                                                        tabName = TabItems.Tech.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                    4 -> {
                                                        tabName = TabItems.Politics.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                    5 -> {
                                                        tabName = TabItems.Opinion.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                    6 -> {
                                                        tabName = TabItems.Entertainment.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                    7 -> {
                                                        tabName = TabItems.Education.tab
                                                        loadNextNewsList(tabName, true)
                                                    }
                                                }
                                            },
                                        )
                                    }
                                }
                            }

                        }

                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = SpacingMedium),

                            columns = GridCells.Fixed(2)

                        )
                        {

                            itemsIndexed(state.newsFullTabItems) { index, news ->
                                if (index >= state.newsFullTabItems.lastIndex && !state.loading && !state.hasListEndReached) {
                                    loadNextNewsList(tabName, false)
                                }

                                if (news.isFirstItem == true) {
                                    HomeNewsItemTab1(news = news, onNewsItemClick = { newsItem ->
                                        onNewsItemClick(news)
                                    })
                                } else {
                                    HomeNewsItemTab2(news = news, onNewsItemClick = { newsItem ->
                                        onNewsItemClick(news)
                                    })
                                }

                            }
                        }
                    }
                }
                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SideNav(
    state: HomeState,
    menuItems: List<MenuItem>,
    isLogin: Boolean,
    modifier: Modifier = Modifier,
    onMenuSubItemClick: (String, String, Boolean) -> Unit,
    onSearchClick: (TextFieldValue) -> Unit,
    snackBarController: SnackbarController,
    onHomeClick: () -> Unit,
    onContactClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSavedItemsClick: (newsSize: Int) -> Unit,
    onPatriotLoginLogoutClick: (isLogin: Boolean) -> Unit

) {
    val focusManager = LocalFocusManager.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current
    ModalDrawerSheet(drawerContainerColor = Color.Transparent) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = SpacingXXLarge)
                .padding(top = SpacingXXLarge),
            verticalArrangement = Arrangement.spacedBy(SpacingMedium)
        ) {
            getTime(null, AppConstants.DATE_ONLY_FORMAT)?.let {
                Text(
                    text = it,
                    fontFamily = FontFamily(Font(R.font.sofia_pro_medium)),
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            getWishes(context)?.let {
                Text(
                    text = it,
                    fontFamily = FontFamily(Font(R.font.sofia_pro_bold)),
                    fontSize = 24.sp,
                    color = Color.White
                )
            }
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (home, send_tip, offline, privacy) = createRefs()
                Icon(painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home",
                    tint = Color.White,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable {
                            onHomeClick()
                        }
                        .constrainAs(home) {
                            start.linkTo(parent.start)
                        })

                Icon(painter = painterResource(id = R.drawable.send_tip),
                    contentDescription = "Send tip",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onContactClick()
                        }
                        .constrainAs(send_tip) {
                            start.linkTo(home.end)
                            end.linkTo(offline.start)
                        })
                BadgedBox(badge = {
                    if (state.savedNews > 0) {
                        Badge {
                            Text(state.savedNews.toString())
                        }
                    }
                }, modifier = Modifier.constrainAs(offline) {
                    start.linkTo(send_tip.end)
                    end.linkTo(privacy.start)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.offline_indicator),
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onSavedItemsClick(state.savedNews)
                            },
                        tint = Color.White,
                    )
                }

                Icon(painter = painterResource(id = R.drawable.privacy),
                    contentDescription = "Privacy",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onPrivacyClick()
                        }
                        .constrainAs(privacy) {
                            end.linkTo(parent.end)
                        })
            }

            BasicTextField(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()

                .clip(
                    shape = RoundedCornerShape(5.dp)
                )
                .background(searchBackground),
                value = searchText,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                    if (searchText.text.isNotEmpty()) {
                        onSearchClick(searchText)
                        searchText = TextFieldValue("")
                    } else {
                        snackBarController.showSnackbar("Please enter a search key")
                    }

                }),
                onValueChange = {
                    searchText = it
                },
                singleLine = true,
                cursorBrush = SolidColor(Color.Red),
                textStyle = LocalTextStyle.current.copy(
                    colorBlack, fontSize = 18.sp, fontFamily = FontFamily(Font(R.font.roboto_light))
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.padding(
                            start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp
                        ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(id = R.drawable.search_icon),
                            contentDescription = "search",
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Box() {
                            if (searchText.text.isEmpty()) Text(
                                "Search",
                                color = colorBlack,
                                fontFamily = FontFamily(Font(R.font.roboto_light))
                            )
                            innerTextField()
                        }

                    }
                })
            Text(text = if (isLogin) "PATRIOT LOGOUT" else "PATRIOT LOGIN",
                fontFamily = FontFamily(Font(R.font.sofia_pro_bold)),
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.clickable {
                    onPatriotLoginLogoutClick(isLogin)
                })


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                items(menuItems) { menu ->
                    ExpandableTabCard(
                        menuHeading = menu,
                        menuSubItems = menu.link,
                        onMenuSubItemClick = onMenuSubItemClick
                    )
                }
            }
        }
    }
}


@Composable
fun ExpandableTabCard(
    menuHeading: MenuItem,
    menuSubItems: List<MenuSubItem>?,
    onMenuSubItemClick: (String, String, Boolean) -> Unit,
) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val transitionState = remember {
        MutableTransitionState(isExpanded).apply {
            targetState = !isExpanded
        }
    }
    val transition = updateTransition(transitionState, label = "")
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = 10)
    }, label = "") {
        if (isExpanded) 0f else 180f
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            if (menuSubItems != null) {
                isExpanded = !isExpanded
            } else {
                val menuRef = menuHeading.href
                if (menuRef != null) {
                    menuHeading.title?.let {
                        getTabMenuLink(menuRef, onMenuSubItemClick = { menuLink, isWeb ->
                            onMenuSubItemClick(menuLink, it, isWeb)
                        })
                    }
                }
            }
        }
        .padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        menuHeading.title?.let {
            CardTabTitle(title = it)
        }
        if (menuSubItems != null) CardTabArrow(degrees = arrowRotationDegree)
    }

    if (menuSubItems != null) {
        ExpandableTabContent(
            visible = isExpanded, menuSubItems = menuSubItems
        ) { menuRef, menuTitle ->
            getTabMenuLink(menuRef, onMenuSubItemClick = { menuLink, isWeb ->
                onMenuSubItemClick(menuLink, menuTitle, isWeb)
            })
        }
    }
}

@Composable
fun CardTabArrow(
    degrees: Float
) {
    Icon(
        imageVector = Icons.Rounded.KeyboardArrowUp,
        contentDescription = "Expandable Arrow",
        modifier = Modifier.rotate(degrees),
        tint = Color.White
    )
}

@Composable
fun CardTabTitle(title: String) {
    Text(
        text = title.uppercase(),
        modifier = Modifier
            .wrapContentWidth()
            .padding(top = 5.dp),
        fontFamily = FontFamily(Font(R.font.sofia_pro_bold)),
        fontSize = 18.sp,
        color = Color.White,
    )
}

@Composable
fun ExpandableTabContent(
    visible: Boolean = true,
    menuSubItems: List<MenuSubItem>,
    onMenuSubItemClick: (String, String) -> Unit
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top, animationSpec = tween(100)
        ) + fadeIn(
            initialAlpha = 0.3f, animationSpec = tween(100)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top, animationSpec = tween(100)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween(100)
        )
    }
    AnimatedVisibility(
        visible = visible, enter = enterTransition, exit = exitTransition
    ) {
        Column {
            menuSubItems.forEach { menu ->
                menu.title?.let { menuTitle ->
                    Text(text = menuTitle.uppercase(),
                        fontFamily = FontFamily(Font(R.font.sofia_pro_medium)),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        color = colorGray,
                        modifier = Modifier
                            .clickable {
                                menu.href?.let { menuRef -> onMenuSubItemClick(menuRef, menuTitle) }
                            }
                            .padding(start = 10.dp, top = 5.dp, bottom = 5.dp))
                }
            }
        }
    }
}

fun getTabMenuLink(
    menuRef: String, onMenuSubItemClick: (String, Boolean) -> Unit
) {
    if (menuRef.contains("section/")) {
        val tabName: String = menuRef.substring(menuRef.lastIndexOf("section/") + 8)
        var menu = tabName.replace("/", "")
        //Hardcoded based on the request of client
        if (menu == "patriots") {
            menu = "premium-content"
        }
        onMenuSubItemClick(menu, false)
    } else {
        if (menuRef.contains("https")) {
            onMenuSubItemClick(menuRef, true)
        } else {
            val menuLink = menuRef.plus("/")
            val webLink: String = AppConstants.WEB_BASE_URL.plus(menuLink)
            onMenuSubItemClick(webLink, true)
        }
    }
}



