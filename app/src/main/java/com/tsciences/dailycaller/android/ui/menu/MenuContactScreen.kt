package com.tsciences.dailycaller.android.ui.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.ui.commonComponents.AppBarTitle
import com.tsciences.dailycaller.android.ui.commonComponents.DailyCallerScaffold
import com.tsciences.dailycaller.android.ui.commonComponents.SnackbarController
import com.tsciences.dailycaller.android.ui.commonComponents.TransparentCenterAlignedTopAppBar

@Composable
fun MenuContactScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = hiltViewModel(),
    snackbarController: SnackbarController,
    onEmailClick: () -> Unit
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

    MenuContactScreenContent(snackbarController = snackbarController,
        state = state,
        onEmailClick = onEmailClick,
        onClickBack = { context.popActivity() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuContactScreenContent(
    snackbarController: SnackbarController,
    state: MenuState,
    onEmailClick: () -> Unit,
    onClickBack: () -> Unit
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
                    onClickBack()
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
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .clickable {
                    onEmailClick()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.contact_us),
                fontFamily = FontFamily(Font(R.font.sofia_pro_bold)),
                fontSize = 24.sp,
                color = Color.Black,
            )
            Text(
                text = stringResource(id = R.string.send_tip_text),
                fontFamily = FontFamily(Font(R.font.sofia_pro_extra_light)),
                fontSize = 18.sp,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.email),
                contentDescription = stringResource(id = R.string.email),
                colorFilter = ColorFilter.tint(Color.Black)
            )

            Text(
                text = stringResource(id = R.string.email),
                fontFamily = FontFamily(Font(R.font.sofia_pro_bold)),
                fontSize = 20.sp,
                color = Color.Black,
            )
        }
    }
}



