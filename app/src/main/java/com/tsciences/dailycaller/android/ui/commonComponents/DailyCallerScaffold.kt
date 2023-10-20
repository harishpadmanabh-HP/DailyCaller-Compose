package com.tsciences.dailycaller.android.ui.commonComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCallerScaffold(
    modifier: Modifier = Modifier,
    snackbarController: SnackbarController = rememberSnackbarController(),
    showLoadingOverlay: Boolean = false,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = { SeekerSnackbarHost(snackbarController) },
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    Box() {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = content
        )
        AnimatedVisibility(
            visible = showLoadingOverlay,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.zIndex(1f)
        ) {
            LoadingOverlay()
        }
    }
}


fun Modifier.drawDailyCallerBackground(
    containerColor: Color
): Modifier = drawBehind {
    drawRect(color = containerColor)

    val blurRadiusPx = BlurredCircleRadius.toPx()
    val circleRadiusPx = CircleRadius.toPx()
    val width = size.width
    val height = size.height

    val topRightCircleOffset = Offset(
        x = width + (circleRadiusPx - 150f), y = (blurRadiusPx + 24f)
    )
    drawCircle(
        brush = topRightCircleBrush(
            center = topRightCircleOffset, radius = blurRadiusPx
        ), center = topRightCircleOffset, radius = blurRadiusPx, alpha = 0.8f
    )

    val bottomLeftCircleOffset = Offset(
        x = -(circleRadiusPx - 200f), y = height - (blurRadiusPx + 24f)
    )
    drawCircle(
        brush = bottomLeftCircleBrush(
            center = bottomLeftCircleOffset, radius = blurRadiusPx
        ), center = bottomLeftCircleOffset, radius = blurRadiusPx, alpha = 0.8f
    )
}

private val CircleRadius = 135.dp
private val BlurredCircleRadius = 250.dp

private fun topRightCircleBrush(
    center: Offset, radius: Float
) = Brush.radialGradient(
    listOf(
        Color(0xFF492E82),
        Color(0xFF492E82).copy(alpha = 0.7f),
        Color(0xFF272C44).copy(alpha = 0.5f),
        Color.Transparent
    ), radius = radius, center = center
)

private fun bottomLeftCircleBrush(
    center: Offset, radius: Float
) = Brush.radialGradient(
    listOf(
        Color(0xFF239789),
        Color(0xFF239789).copy(alpha = 0.4f),
        Color(0xFF492E82).copy(alpha = 0.2f),
        Color.Transparent,
    ), radius = radius, center = center
)