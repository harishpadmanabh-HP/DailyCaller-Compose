package com.tsciences.dailycaller.android.ui.commonComponents.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.tsciences.dailycaller.android.core.theme.SpacingMedium

fun LazyListState.isScrollAtEnd(): Boolean =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

fun dailyCallerScreenContentPadding(
    start: Dp = SpacingMedium,
    end: Dp = SpacingMedium
): PaddingValues = PaddingValues(
    start = start,
    end = end
)

@Composable
fun VerticalSpacer(spacing: Dp) {
    Spacer(modifier = Modifier.height(spacing))
}

@Composable
fun HorizontalSpacer(spacing: Dp) {
    Spacer(modifier = Modifier.width(spacing))
}