package com.tsciences.dailycaller.android.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.colorBLUE
import com.tsciences.dailycaller.android.core.theme.colorRED
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.core.util.popActivity
import com.tsciences.dailycaller.android.ui.commonComponents.AppBarTitle
import com.tsciences.dailycaller.android.ui.commonComponents.DailyCallerScaffold
import com.tsciences.dailycaller.android.ui.commonComponents.SnackbarController
import com.tsciences.dailycaller.android.ui.commonComponents.TransparentCenterAlignedTopAppBar

@Composable
fun MenuPrivacyScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuViewModel = hiltViewModel(),
    snackbarController: SnackbarController,
    onPrivacyPolicyClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
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

    MenuPrivacyScreenContent(snackbarController = snackbarController,
        state = state,
        onPrivacyPolicyClick = onPrivacyPolicyClick,
        onDeleteAccountClick = onDeleteAccountClick,
        onBackClick = {
            context.popActivity()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrivacyScreenContent(
    snackbarController: SnackbarController,
    state: MenuState,
    onPrivacyPolicyClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onBackClick: () -> Unit
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
                    onBackClick()
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
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    onPrivacyPolicyClick()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorBLUE)
            ) {
                Text(
                    text = stringResource(id = R.string.privacy_policy),
                    color = Color.White,
                    modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 14.sp
                )
            }
            Button(
                onClick = {
                    onDeleteAccountClick()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorRED)
            ) {
                Text(
                    text = stringResource(id = R.string.delete_account),
                    color = Color.White,
                    modifier = Modifier.padding(start = 28.dp, end = 28.dp),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = 14.sp
                )
            }
        }
    }
}



