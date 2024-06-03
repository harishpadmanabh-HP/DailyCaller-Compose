package com.tsciences.dailycaller.android.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.appConstants.AppConstants
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.core.util.getCategoryColor
import com.tsciences.dailycaller.android.core.util.getTime
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.database.NewsModel
import com.tsciences.dailycaller.android.ui.commonComponents.AppBarTitle
import com.tsciences.dailycaller.android.ui.commonComponents.DailyCallerScaffold
import com.tsciences.dailycaller.android.ui.commonComponents.SnackbarController
import com.tsciences.dailycaller.android.ui.commonComponents.TransparentCenterAlignedTopAppBar
import com.tsciences.dailycaller.android.ui.commonComponents.utils.dailyCallerScreenContentPadding
import com.tsciences.dailycaller.android.utils.stripHtml

@Composable
fun SavedNewsScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    snackbarController: SnackbarController,
    onNewsItemClick: (NewsModel) -> Unit
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel, snackbarController) {
        viewModel.events.collect { event ->
            when (event) {
                is MenuViewModel.MenuSectionEvent.SavedNews -> {
                    snackbarController.showSnackbar(
                        duration = SnackbarDuration.Indefinite, message = event.noSavedNews
                    )
                }
                is MenuViewModel.MenuSectionEvent.ShowUiMessage -> {
                    snackbarController.showSnackbar(event.uiText.asString(context))
                }
                else -> {}
            }
        }
    }

    SavedNewsScreenContent(
        snackbarController = snackbarController,
        state = state,
        onNewsItemClick = onNewsItemClick,
        navigateBack = context::popActivity,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SavedNewsScreenContent(
    snackbarController: SnackbarController,
    state: MenuState,
    onNewsItemClick: (NewsModel) -> Unit,
    navigateBack: () -> Unit
) {

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 10.dp)

        ) {
            items(
                items = state.savedNews
            ) { news ->
                Surface(onClick = {
                    if (news != null) {
                        onNewsItemClick(news)
                    }
                }) {

                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color.White)
                            .padding(dailyCallerScreenContentPadding())
                            .padding(top = 5.dp, bottom = 5.dp),

                        ) {
                        val (collapsibleNewsImage, categoryTag, title, authorName, whiteDotImage, time, authorImage) = createRefs()
                        AsyncImage(model = news?.newsImage,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(id = R.dimen.home_firstItemHeight))
                                .constrainAs(collapsibleNewsImage) {}, placeholder = painterResource(id = R.drawable.default_image_wide) )
                        stripHtml(news?.newsTitle).let {
                            Text(
                                text = it,

                                modifier = Modifier.constrainAs(title) {
                                    top.linkTo(collapsibleNewsImage.bottom, 5.dp)
                                    start.linkTo(parent.start)
                                },
                                fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                        Text(text = news?.category.toString(),

                            modifier = Modifier
                                .background(
                                    color = getCategoryColor(news?.category.toString()),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .constrainAs(categoryTag) {
                                    top.linkTo(title.bottom, 5.dp)
                                    start.linkTo(parent.start)
                                }
                                .padding(horizontal = 5.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White)
                        Text(
                            text = news?.authorName.toString(),

                            modifier = Modifier.constrainAs(authorName) {
                                top.linkTo(title.bottom, 5.dp)
                                start.linkTo(categoryTag.end, 10.dp)
                            },
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            fontSize = 12.sp,
                            color = Color.Black
                        )

                        Image(
                            modifier = Modifier
                                .size(10.dp)
                                .constrainAs(whiteDotImage) {
                                    bottom.linkTo(authorName.bottom)
                                    top.linkTo(authorName.top)
                                    start.linkTo(authorName.end, 10.dp)
                                },
                            contentScale = ContentScale.FillBounds,
                            painter = painterResource(id = R.drawable.ic_fiber_manual_record),
                            contentDescription = "white dot",
                            colorFilter = ColorFilter.tint(Color.Black)

                        )

                        getTime(
                            news?.newsTime, AppConstants.DATE_FORMAT_TIME_STAMP
                        )?.let {
                            Text(
                                text = it,
                                modifier = Modifier.constrainAs(time) {
                                    top.linkTo(title.bottom, 5.dp)
                                    start.linkTo(whiteDotImage.end, 10.dp)
                                },
                                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                fontSize = 12.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                Divider(
                    modifier = Modifier.fillMaxWidth(), thickness = 5.dp, color = gray
                )
            }
        }
    }
}





