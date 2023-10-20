package com.tsciences.dailycaller.android.ui.commonComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.OverlayColor

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier, indicatorSize: Dp = DefaultIndicatorSize
) {
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    LottieAnimation(
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(indicatorSize)
    )
}

private val DefaultIndicatorSize = 48.dp

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier
) {
    Surface(
        color = OverlayColor, modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
}

@Composable
fun SeekerCircularProgressIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        color = Color.White, modifier = modifier
    )
}

fun LazyListScope.listLoadingIndicator(
    show: Boolean
) {
    if (show) {
        item(key = "ListLoadingIndicator") {
            Box(
                modifier = Modifier.fillParentMaxWidth(), contentAlignment = Alignment.Center
            ) {
                SeekerCircularProgressIndicator()
            }
        }
    }
}

fun LazyGridScope.gridLoadingIndicator(
    show: Boolean
) {
    if (show) {
        item(
            span = { GridItemSpan(maxLineSpan) }, key = "ListLoadingIndicator"
        ) {
            Box(contentAlignment = Alignment.Center) {
                SeekerCircularProgressIndicator()
            }
        }
    }
}