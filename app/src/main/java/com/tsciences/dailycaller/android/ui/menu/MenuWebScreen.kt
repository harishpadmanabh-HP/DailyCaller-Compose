package com.tsciences.dailycaller.android.ui.menu

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.ui.commonComponents.*
import com.tsciences.dailycaller.android.ui.commonComponents.utils.dailyCallerScreenContentPadding

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MenuWebScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    menuLink: String,
    snackbarController: SnackbarController
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

    MenuWebScreenContent(
        snackbarController = snackbarController,
        state = state,
        menuLink = menuLink,
        navigateBack = context::popActivity,
    )
}

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MenuWebScreenContent(
    snackbarController: SnackbarController,
    state: MenuState,
    menuLink: String,
    navigateBack: () -> Unit
) {
    val loaderDialogScreen = remember { mutableStateOf(false) }
    if (loaderDialogScreen.value) {
        showLoader(isLoad = loaderDialogScreen.value)
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
        AndroidView(modifier = Modifier
            .padding(paddingValues)
            .padding(
                dailyCallerScreenContentPadding()
            ), factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        loaderDialogScreen.value = true
                    }

                    override fun onLoadResource(view: WebView?, url: String?) {
                        super.onLoadResource(view, url)
                        view?.loadUrl(
                            "javascript:(function() { " + "var head = document.getElementsByClassName('container fullContainer noTopMargin padding20-top padding20-bottom padding40H noBorder borderSolid border3px cornersAll radius0 shadow0 bgNoRepeat emptySection')[0].style.display='none'; " + "})()"
                        )

                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?, request: WebResourceRequest?
                    ): Boolean {
                        return super.shouldOverrideUrlLoading(view, request)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        loaderDialogScreen.value = false
                    }
                }
                settings.domStorageEnabled = true
                settings.javaScriptEnabled = true
                loadUrl(menuLink)

            }
        }, update = {
            it.loadUrl(menuLink)
        })
    }
}

@Composable
fun showLoader(isLoad: Boolean) {
    AnimatedVisibility(
        visible = isLoad, enter = fadeIn(), exit = fadeOut(), modifier = Modifier.zIndex(1f)
    ) {
        LoadingOverlay()
    }
}



