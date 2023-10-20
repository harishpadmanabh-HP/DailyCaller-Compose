package com.tsciences.dailycaller.android.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
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
import com.tsciences.dailycaller.android.core.theme.colorBlack
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.data.remote.documentaries.Stream
import com.tsciences.dailycaller.android.ui.commonComponents.AppBarTitle
import com.tsciences.dailycaller.android.ui.commonComponents.DailyCallerScaffold
import com.tsciences.dailycaller.android.ui.commonComponents.SnackbarController
import com.tsciences.dailycaller.android.ui.commonComponents.TransparentCenterAlignedTopAppBar
import com.tsciences.dailycaller.android.ui.commonComponents.utils.dailyCallerScreenContentPadding
import com.tsciences.dailycaller.android.utils.stripHtml

@Composable
fun StreamDocumentaryScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    menuTerm: String,
    snackbarController: SnackbarController,
    menuTitle: String,
    navigateToVideoScreen: (Stream) -> Unit
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(context, viewModel, snackbarController) {
        viewModel.events.collect { event ->
            when (event) {
                is MenuViewModel.MenuSectionEvent.ShowUiMessage -> {
                    snackbarController.showSnackbar(event.uiText.asString(context))
                }
                is MenuViewModel.MenuSectionEvent.VideoUrl -> {
                    //   navigateToVideoScreen(event.videoUrl)
                }
                else -> {}
            }
        }
    }

    StreamDocumentaryContent(snackbarController = snackbarController,
        state = state,
        menuTerm = menuTerm,
        viewModel = viewModel,
        menuTitle = menuTitle,
        navigateBack = context::popActivity,
        onVideoClick = { stream ->
            navigateToVideoScreen(stream)
        })
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun StreamDocumentaryContent(
    snackbarController: SnackbarController,
    state: MenuState,
    menuTerm: String,
    viewModel: MenuViewModel,
    menuTitle: String,
    navigateBack: () -> Unit,
    onVideoClick: (Stream) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getStreamItems(menuTitle)
    }

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
                .fillMaxHeight()
                .padding(paddingValues)
                .background(Color.White)
        ) {

            Text(
                text = state.menuTitle,
                fontFamily = FontFamily(Font(R.font.sofia_pro_semibold)),
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            LazyColumn(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 10.dp)
            ) {
                itemsIndexed(items = state.streams) { index, stream ->

                    Surface(onClick = { onVideoClick(stream) }) {

                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Color.White)
                                .padding(dailyCallerScreenContentPadding())
                                .padding(top = 5.dp, bottom = 5.dp),

                            ) {
                            val (streamThumbImage, title, name) = createRefs()
                            AsyncImage(model = stream.thumImage,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.home_firstItemHeight))
                                    .constrainAs(streamThumbImage) {})

                            Text(
                                text = stripHtml(stream.title),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(top = 5.dp)
                                    .constrainAs(title) {
                                        start.linkTo(parent.start)
                                        top.linkTo(streamThumbImage.bottom)

                                    },
                                fontFamily = FontFamily(Font(R.font.sofia_pro_medium)),
                                fontSize = 14.sp,
                                color = Color.Black,
                            )
                            Text(
                                text = stripHtml(stream.name),
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(top = 5.dp)
                                    .constrainAs(name) {
                                        start.linkTo(parent.start)
                                        top.linkTo(title.bottom)

                                    },
                                fontFamily = FontFamily(Font(R.font.sofia_pro_medium)),
                                fontSize = 14.sp,
                                color = Color.Black,
                            )



                            Divider(
                                modifier = Modifier.fillMaxWidth(), thickness = 5.dp, color = gray
                            )
                        }
                    }

                }
            }

            if (menuTitle.uppercase() == "Groomed".uppercase()) {
                Button(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 30.dp)
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.getStreamItems("Stream Documentaries")
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorBlack)
                ) {
                    Text(
                        text = stringResource(id = R.string.click_to_view_documentaries),
                        color = Color.White,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}



