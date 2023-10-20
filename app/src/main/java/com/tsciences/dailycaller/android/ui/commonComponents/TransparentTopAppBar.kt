package com.tsciences.dailycaller.android.ui.commonComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.SpacingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
) {
    Box(modifier = Modifier.padding(horizontal = SpacingSmall)) {
        TopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            windowInsets = windowInsets,
            colors = colors,
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentCenterAlignedTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
) {
    Box(
        modifier = Modifier
            .padding(SpacingSmall)
            .clip(shape = MaterialTheme.shapes.small)
    ) {
        CenterAlignedTopAppBar(
            title = {
                ProvideTextStyle(
                    value = titleTextStyle, content = title
                )
            },
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            windowInsets = windowInsets,
            colors = colors,
            scrollBehavior = scrollBehavior
        )
    }
}

@Composable
fun AppBarTitle(
    logoModifier: Modifier = Modifier, subLogoModifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_white),
            contentDescription = "Daily Caller logo",
            modifier = logoModifier
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_sub_logo),
            contentDescription = null,
            modifier = subLogoModifier,
            contentScale = ContentScale.Fit
        )
    }
}