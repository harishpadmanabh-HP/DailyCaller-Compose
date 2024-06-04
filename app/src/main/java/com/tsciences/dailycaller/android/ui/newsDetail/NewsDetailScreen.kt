package com.tsciences.dailycaller.android.ui.newsDetail

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.ads.*
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.appConstants.AppConstants
import com.tsciences.dailycaller.android.core.theme.SpacingMedium
import com.tsciences.dailycaller.android.core.theme.colorBlack
import com.tsciences.dailycaller.android.core.util.getCategoryColor
import com.tsciences.dailycaller.android.core.util.getTime
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.data.remote.home.Gallery
import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.ui.commonComponents.*
import com.tsciences.dailycaller.android.ui.commonComponents.utils.VerticalSpacer
import com.tsciences.dailycaller.android.ui.commonComponents.utils.dailyCallerScreenContentPadding
import com.tsciences.dailycaller.android.utils.getLinkFromSearchItem
import com.tsciences.dailycaller.android.utils.hasInternet
import com.tsciences.dailycaller.android.utils.isValidLink
import com.tsciences.dailycaller.android.utils.stripHtml
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun NewsDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsDetailViewModel = hiltViewModel(),
    news: Item,
    snackBarController: SnackbarController,
    isNewsAvailable: Boolean,
    onShowCommentClick: (postId: String) -> Unit,
    isTab: Boolean,
    onUrlClick: (urlIdentifier: String, url: String) -> Unit,
    navigateToNewsDetailPage: (Item) -> Unit,
    navigateToNotFoundPage: (Boolean) -> Unit,
    onSlideShowClick: (Int,Gallery) -> Unit,
    onShareClick: (item: Item) -> Unit,
    onSaveNewsClick: (isSave: Boolean, newsItem: Item) -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel, snackBarController) {
        viewModel.events.collect { event ->
            when (event) {
                is NewsDetailViewModel.NewsDetailEvent.ShowUiMessage -> {
                    snackBarController.showSnackbar(event.uiText.asString(context))
                }
                is NewsDetailViewModel.NewsDetailEvent.GetPostId -> {
                    onShowCommentClick(event.postId)
                }
                is NewsDetailViewModel.NewsDetailEvent.SearchDetail -> {
                    navigateToNewsDetailPage(event.searchDetail)
                }

                is NewsDetailViewModel.NewsDetailEvent.NotFound -> {
                    navigateToNotFoundPage(event.notFound)
                }
                else -> {}
            }
        }
    }

    NewsDetailScreenContent(
        viewModel = viewModel,
        modifier = modifier,
        navigateBack = context::popActivity,
        news = news,
        snackBarController = snackBarController,
        onSaveNewsClick = onSaveNewsClick,
        isNewsAvailable = isNewsAvailable,
        onShowCommentClick = {
            if ((news.postID != null) && (news.postID?.isNotEmpty() == true)) {
                onShowCommentClick(news.postID!!)
            } else if ((news.link != null) && news.link.isNotEmpty()) {
                viewModel.getPostIdToDisplaySpotIm(news.link)
            }
        },
        isTab = isTab,
        onUrlClick = onUrlClick,
        scrollState = scrollState,
        onSlideShowClick = onSlideShowClick,
        onShareClick = onShareClick,
        state = state
    )
}


@SuppressLint("SetJavaScriptEnabled", "VisibleForTests")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreenContent(
    viewModel: NewsDetailViewModel,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    news: Item,
    snackBarController: SnackbarController,
    onSaveNewsClick: (isSave: Boolean, newsItem: Item) -> Unit,
    isNewsAvailable: Boolean,
    onShowCommentClick: () -> Unit,
    isTab: Boolean,
    onUrlClick: (urlIdentifier: String, url: String) -> Unit,
    scrollState: ScrollState,
    onSlideShowClick: (Int,Gallery) -> Unit,
    onShareClick: (item: Item) -> Unit,
    state: NewsDetailState
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        scrollState.scrollTo(0)
    }
    var isSaveNews by rememberSaveable {
        mutableStateOf(false)
    }
    if (isNewsAvailable) isSaveNews = true
    DailyCallerScaffold(snackbarController = snackBarController, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                if (isSaveNews) {
                    isSaveNews = false
                    onSaveNewsClick(isSaveNews, news)
                } else {
                    if (hasInternet(context)) {
                        isSaveNews = true
                        onSaveNewsClick(isSaveNews, news)
                    } else {
                        snackBarController.showSnackbar("You are offline , not able to save news.")
                    }
                }

            }, containerColor = Color.Red, contentColor = Color.White, shape = CircleShape
        ) {
            Icon(
                painter = painterResource(
                    id = if (isSaveNews) {
                        R.drawable.delete
                    } else {
                        R.drawable.save
                    }
                ), ""
            )
        }
    }, showLoadingOverlay = state.loading) { paddingValues ->


        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = news.imageUrl ?: news.image?.loc?.text,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.home_firstItemHeight)),
                placeholder = painterResource(id = R.drawable.default_image_wide)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(paddingValues)
                    .padding(dailyCallerScreenContentPadding())
                    .verticalScroll(scrollState)

            ) {


                VerticalSpacer(spacing = dimensionResource(id = R.dimen.newsDetailSpacerHeight))
                Surface(modifier = Modifier.background(Color.White)) {

                    Box() {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(paddingValues)

                        ) {

                            Surface(modifier = Modifier.background(Color.White)) {
                                ConstraintLayout(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .background(Color.White)
                                        .padding(horizontal = 16.dp, vertical = 16.dp)
                                ) {
                                    val (authorImage, categoryTag, foundationImage, title, authorName, redDotImage, time, shareButton, divider1, newsView, bannerTitle, mainBanner, showComments, slideshow) = createRefs()
                                    VerticalSpacer(spacing = dimensionResource(id = R.dimen.newsDetailSpacerHeight))

                                    Text(
                                        text = news.categoryTag.toString(),
                                        modifier = Modifier
                                            .background(
                                                color = getCategoryColor(news.categoryTag.toString()),
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                            .constrainAs(categoryTag) {
                                                start.linkTo(parent.start)
                                                top.linkTo(parent.top)
                                            },
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White,
                                        fontSize = dimensionResource(id = R.dimen.category_Tag_size).value.sp
                                    )
                                    if (news.isDcFoundation) {

                                        Image(painter = painterResource(id = R.drawable.dc_foundation_update),
                                            contentDescription = "Daily Caller logo",
                                            modifier = Modifier
                                                .width(dimensionResource(id = R.dimen.appbar_logo_width))
                                                .height(dimensionResource(id = R.dimen.appbar_logo_height))
                                                .constrainAs(foundationImage) {
                                                    start.linkTo(categoryTag.end, 8.dp)
                                                    top.linkTo(categoryTag.top)
                                                    bottom.linkTo(categoryTag.bottom)
                                                })
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(shape = CircleShape)
                                            .background(Color.White)
                                            .constrainAs(authorImage) {
                                                start.linkTo(foundationImage.end)
                                                end.linkTo(parent.end)
                                            }
                                    ) {
                                        AsyncImage(
                                            model = news.authorImage,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )
                                    }

                                    stripHtml(news.title).let { titletxt ->
                                        Text(text = titletxt,
                                            modifier = Modifier
                                                .constrainAs(title) {
                                                    top.linkTo(authorImage.bottom, 16.dp)
                                                    start.linkTo(parent.start)
                                                    end.linkTo(parent.end)
                                                }
                                                .fillMaxWidth(),
                                            fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),
                                            fontSize = dimensionResource(id = R.dimen.news_size).value.sp,
                                            color = colorBlack)
                                    }

                                    Text(
                                        text = news.authorName.toString(),

                                        modifier = Modifier.constrainAs(authorName) {
                                            top.linkTo(title.bottom, 8.dp)
                                            start.linkTo(parent.start)
                                        },
                                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                                        fontSize = dimensionResource(id = R.dimen.category_Tag_size).value.sp,
                                        color = colorBlack
                                    )

                                    Image(
                                        modifier = Modifier
                                            .size(dimensionResource(id = R.dimen.red_dot_size))
                                            .constrainAs(redDotImage) {
                                                bottom.linkTo(authorName.bottom)
                                                top.linkTo(authorName.top)
                                                start.linkTo(authorName.end, 10.dp)
                                            },
                                        contentScale = ContentScale.FillBounds,
                                        painter = painterResource(id = R.drawable.ic_fiber_manual_record),
                                        contentDescription = "white dot",
                                        colorFilter = ColorFilter.tint(Color.Red)

                                    )

                                    getTime(
                                        news.pubDate, AppConstants.DATE_FORMAT_TIME_STAMP
                                    )?.let {
                                        Text(
                                            text = it,
                                            modifier = Modifier.constrainAs(time) {
                                                bottom.linkTo(redDotImage.bottom)
                                                top.linkTo(redDotImage.top)
                                                start.linkTo(redDotImage.end, 10.dp)
                                            },
                                            fontFamily = FontFamily(Font(R.font.roboto_light)),
                                            fontSize = dimensionResource(id = R.dimen.category_Tag_size).value.sp,
                                            color = colorBlack
                                        )
                                    }

                                    Image(
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .constrainAs(shareButton) {
                                                bottom.linkTo(redDotImage.bottom)
                                                top.linkTo(redDotImage.top)
                                                end.linkTo(parent.end)
                                            }
                                            .clickable { onShareClick(news) },
                                        contentScale = ContentScale.FillBounds,
                                        painter = painterResource(id = R.drawable.share),
                                        contentDescription = "share"
                                    )

                                    DailyCallerDivider(
                                        modifier = Modifier.constrainAs(divider1) {
                                            top.linkTo(shareButton.bottom, 16.dp)
                                        }, thickness = 2.dp
                                    )

                                    if (state.isNotSubscribed) {
                                        AndroidView(modifier = modifier
                                            .fillMaxWidth()
                                            .constrainAs(bannerTitle) {
                                                top.linkTo(divider1.bottom, 8.dp)
                                            }, factory = { context ->
                                            AdView(context).apply {
                                                setAdSize(AdSize.BANNER)
                                                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                                                //adUnitId = "ca-app-pub-1610338282405979/5516633215"
                                                loadAd(AdRequest.Builder().build())
                                            }
                                        })
                                    }
                                    var formattedString: String? = null

                                    if (isTab) {
                                        formattedString = news.encodedString?.replaceFirst(
                                            "<p>",
                                            "<p id = 'CapitalLetterFirst' style='font-family: MyFont;font-size:18px;'><style> #CapitalLetterFirst::first-letter { color: #000; font-size: 40px; } .wp-caption{width:100% !important;} p,div, li{font-family: MyFont; font-size:18px;} p a{color: #dc3535;} a:link { color: #dc3535; } img{width:100%;height:auto;max-height:1000px;} iframe{width:100%;min-width: auto !important;}</style>"
                                        ) ?: ""
                                    } else {
                                        formattedString = news.encodedString?.replaceFirst(
                                            "<p>",
                                            "<p id = 'CapitalLetterFirst' style='font-family: MyFont;font-size:15px;'><style> #CapitalLetterFirst::first-letter { color: #000; font-size: 40px; } .wp-caption{width:100% !important;} p,div, li{font-family: MyFont; font-size:15px;} p a{color: #dc3535;} a:link { color: #dc3535; } img{width:100%;height:auto;max-height:1000px;} iframe{width:100%;min-width: auto !important;}</style>"
                                        ) ?: ""
                                    }

                                    val formattedLinkString =
                                        formattedString.replace("src=\"//", "src=\"http://")

                                    val convertedWebView =
                                        "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///font/caecilia-roman-webfont.ttf\")} </style></head><body>$formattedLinkString</body></html>"
                                    AndroidView(modifier = Modifier
                                        .alpha(0.99f)
                                        .constrainAs(newsView) {
                                            if (state.isNotSubscribed) {
                                                top.linkTo(bannerTitle.bottom)
                                            } else {
                                                top.linkTo(divider1.bottom)
                                            }
                                        }, factory = {
                                        WebView(it).apply {
                                            layoutParams = ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                            )

                                            settings.javaScriptEnabled = true
                                            webViewClient = object : WebViewClient() {


                                                @Deprecated("Deprecated in Java")
                                                override fun shouldOverrideUrlLoading(
                                                    view: WebView,
                                                    url: String
                                                ): Boolean {

                                                    if (isValidLink(url)) {
                                                        getLinkFromSearchItem(url)?.let { link ->
                                                            onUrlClick(
                                                                "validUrl",
                                                                link
                                                            )
                                                        }

                                                    } else if (url.startsWith("mailto:")) {
                                                        onUrlClick(
                                                            "mailUrl",
                                                            url
                                                        )
                                                    } else if (url.startsWith("tel:")) {
                                                        onUrlClick(
                                                            "telUrl",
                                                            url
                                                        )
                                                    } else {
                                                        onUrlClick(
                                                            "otherUrl",
                                                            url
                                                        )
                                                    }
                                                    return true
                                                }
                                            }

                                            loadDataWithBaseURL(
                                                null,
                                                convertedWebView,
                                                "text/HTML",
                                                "UTF-8",
                                                null
                                            )
                                        }
                                    }, update = {
                                        it.loadDataWithBaseURL(
                                            null, convertedWebView, "text/HTML", "UTF-8", null
                                        )
                                    })

                                    if (news.oGallery?.slideList != null && news.oGallery.slideList.isNotEmpty()) {
                                        val slidesize = news.oGallery.slideList.size
                                        var rowSize = 0
                                        if ((slidesize % 3) > 0) {
                                            rowSize = ((slidesize / 3) + 1)
                                        } else {
                                            rowSize = slidesize / 3
                                        }
                                        val itemHeight =
                                            dimensionResource(id = R.dimen.home_firstItemHeight)
                                        val gridHeight = itemHeight * rowSize
                                        LazyVerticalGrid(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(gridHeight)
                                                .padding(top = SpacingMedium)
                                                .constrainAs(slideshow) {
                                                    top.linkTo(newsView.bottom)
                                                },

                                            columns = GridCells.Fixed(3)
                                        )
                                        {

                                            itemsIndexed(news.oGallery.slideList) { index, slide ->

                                                val slideImageWidth =
                                                    dimensionResource(id = R.dimen.home_firstItemHeight)

                                                val imageUrl =
                                                    ("https://images.dailycaller.com/image/width=" + slideImageWidth +
                                                            ",height=" + slideImageWidth //+ prefixText.getSlideFullImage()
                                                            + ",fit=cover,f=auto/" + slide.slideFullImage)
                                                AsyncImage(
                                                    model = slide.slideFullImage,
                                                    contentDescription = "",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(5.dp)
                                                        .height(dimensionResource(id = R.dimen.home_firstItemHeight))
                                                        .clickable { onSlideShowClick(index,news.oGallery) }
                                                )

                                            }
                                        }
                                    }

                                    if (state.isNotSubscribed) {
                                        AndroidView(modifier = modifier
                                            .fillMaxWidth()
                                            .constrainAs(mainBanner) {
                                                if (news.oGallery?.slideList != null && news.oGallery.slideList.isNotEmpty()) {
                                                    top.linkTo(slideshow.bottom, 8.dp)
                                                } else {
                                                    top.linkTo(newsView.bottom, 8.dp)
                                                }
                                            }, factory = { context ->
                                            AdView(context).apply {
                                                setAdSize(AdSize.MEDIUM_RECTANGLE)
                                                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                                                //adUnitId = "ca-app-pub-1610338282405979/7021286572"
                                                loadAd(AdRequest.Builder().build())
                                            }
                                        })
                                    }


                                    Image(
                                        modifier = Modifier
                                            .clickable { onShowCommentClick() }
                                            .constrainAs(showComments) {
                                                if (state.isNotSubscribed) {
                                                    top.linkTo(mainBanner.bottom)
                                                } else {
                                                    if (news.oGallery?.slideList != null && news.oGallery.slideList.isNotEmpty()) {
                                                        top.linkTo(slideshow.bottom)
                                                    } else {
                                                        top.linkTo(newsView.bottom)
                                                    }
                                                }

                                                start.linkTo(parent.start)
                                                end.linkTo(parent.end)
                                            }
                                            .padding(top = 10.dp),
                                        contentScale = ContentScale.FillBounds,
                                        painter = painterResource(id = R.drawable.showcomments_copy),
                                        contentDescription = stringResource(id = R.string.show_comment_button),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.newsDetailViewHeight)),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.gradient_top),
            contentDescription = "Section Gradient",
        )

        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.content_back_arrow),
            tint = Color.White,
            modifier = Modifier
                .padding(10.dp)
                .clickable { navigateBack() }
                .size(dimensionResource(id = R.dimen.back_button)))
    }
}




