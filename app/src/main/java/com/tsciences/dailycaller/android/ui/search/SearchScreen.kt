package com.tsciences.dailycaller.android.ui.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.colorBlack
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.core.theme.searchBackground
import com.tsciences.dailycaller.android.core.util.getTimeFromSearchResp
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.ui.commonComponents.AppBarTitle
import com.tsciences.dailycaller.android.ui.commonComponents.DailyCallerScaffold
import com.tsciences.dailycaller.android.ui.commonComponents.SnackbarController
import com.tsciences.dailycaller.android.ui.commonComponents.TransparentCenterAlignedTopAppBar
import com.tsciences.dailycaller.android.ui.commonComponents.utils.dailyCallerScreenContentPadding
import com.tsciences.dailycaller.android.utils.stripHtml

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    searchTag: String,
    snackbarController: SnackbarController,
    onNewsItemClick: (String) -> Unit,
    navigateToNewsDetailPage: (Item) -> Unit
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel, snackbarController) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchViewModel.SearchEvent.ShowUiMessage -> {
                    snackbarController.showSnackbar(event.uiText.asString(context))
                }
                is SearchViewModel.SearchEvent.SearchDetail -> {
                    navigateToNewsDetailPage(event.searchDetail)
                }
                else -> {}
            }
        }
    }

    SearchScreenContent(
        modifier = modifier,
        snackbarController = snackbarController,
        navigateBack = context::popActivity,
        state = state,
        onSearchClick = { searchText ->
            if (searchText != "") {
                viewModel.loadSearchNews(searchText, true)
            } else {
                snackbarController.showSnackbar("please enter a search key")
            }
        },
        loadNextSearchNewsList = { searchTagName ->
            viewModel.loadSearchNews(searchTagName, false)
        },
        searchTag = searchTag,
        viewModel = viewModel,
        onNewsItemClick = onNewsItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    snackbarController: SnackbarController,
    navigateBack: () -> Unit,
    state: SearchState,
    onSearchClick: (String) -> Unit,
    loadNextSearchNewsList: (String) -> Unit,
    searchTag: String,
    viewModel: SearchViewModel,
    onNewsItemClick: (String) -> Unit
) {
    Log.d("searchContent", "search")
    var searchText by remember { mutableStateOf(TextFieldValue(searchTag)) }

    LaunchedEffect(Unit) {
        viewModel.loadSearchNews(searchText.text, true)
    }
    val focusManager = LocalFocusManager.current

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
                .background(Color.White)
        ) {
            BasicTextField(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(paddingValues)
                .padding(dailyCallerScreenContentPadding())
                .clip(
                    shape = RoundedCornerShape(5.dp)
                )
                .background(searchBackground),
                value = searchText,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                    if (!searchText.text.isEmpty()) {
                        onSearchClick(searchText.text)
                    } else {
                        snackbarController.showSnackbar("Please enter a search key")
                    }
                }),
                onValueChange = {
                    searchText = it
                },
                singleLine = true,
                cursorBrush = SolidColor(Color.Red),
                textStyle = LocalTextStyle.current.copy(
                    colorBlack,
                    fontSize = dimensionResource(id = R.dimen.search_size).value.sp,
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
                                "Search",
                                color = colorBlack,
                                fontSize = dimensionResource(id = R.dimen.search_size).value.sp,
                                fontFamily = FontFamily(Font(R.font.roboto_light))
                            )
                            innerTextField()
                        }
                    }
                })
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)

            ) {
                itemsIndexed(
                    items = state.searchItemList
                ) { index, news ->

                    if (index >= state.searchItemList.lastIndex && !state.loading && !state.hasListEndReached) {
                        loadNextSearchNewsList(searchText.text)
                    }

                    Surface(onClick = { onNewsItemClick(news.link.toString()) }) {

                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Color.White)
                                .padding(dailyCallerScreenContentPadding())
                                .padding(top = 5.dp, bottom = 5.dp),

                            ) {
                            val (collapsibleNewsImage, title, time) = createRefs()
                            if (news.pagemap?.cse_image?.size?.equals(0) == false) {
                                AsyncImage(model = news.pagemap.cse_image.get(0).src ?: "",
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(dimensionResource(id = R.dimen.home_firstItemHeight))
                                        .constrainAs(collapsibleNewsImage) {})
                            }
                            stripHtml(news.title).let {
                                Text(
                                    text = it,
                                    modifier = Modifier.constrainAs(title) {
                                        top.linkTo(collapsibleNewsImage.bottom, 5.dp)
                                        start.linkTo(parent.start)
                                    },
                                    fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),
                                    fontSize = dimensionResource(id = R.dimen.search_Text_size).value.sp,
                                    color = Color.Black
                                )
                            }


                            getTimeFromSearchResp(news.snippet)?.let {
                                Text(
                                    text = it,
                                    modifier = Modifier.constrainAs(time) {
                                        top.linkTo(title.bottom, 5.dp)
                                        start.linkTo(parent.start)
                                    },
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    fontSize = dimensionResource(id = R.dimen.search_Time_size).value.sp,
                                    color = Color.Black
                                )
                            }
                        }

                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp),
                        thickness = 5.dp,
                        color = gray
                    )
                }
            }
        }
    }
}





