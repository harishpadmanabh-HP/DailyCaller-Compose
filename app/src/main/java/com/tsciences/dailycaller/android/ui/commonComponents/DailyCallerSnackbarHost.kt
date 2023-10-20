package com.tsciences.dailycaller.android.ui.commonComponents

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.tsciences.dailycaller.android.core.theme.CardSurface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun SeekerSnackbarHost(
    controller: SnackbarController,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { DefaultSnackbar(it) }
) {
    SnackbarHost(
        hostState = controller.snackbarHostState, modifier = modifier, snackbar = snackbar
    )
}

@Composable
fun DefaultSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
    actionOnNewLine: Boolean = false,
    shape: Shape = MaterialTheme.shapes.small,
    containerColor: Color = CardSurface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    actionColor: Color = SnackbarDefaults.actionColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
) {
    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionColor = actionColor,
        actionContentColor = actionContentColor,
        dismissActionContentColor = dismissActionContentColor
    )
}

class SnackbarController(
    val snackbarHostState: SnackbarHostState, private val coroutineScope: CoroutineScope
) {
    private var snackbarJob: Job? = null

    init {
        snackbarJob?.cancel()
    }

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite
    ) {
        snackbarJob?.cancel()
        snackbarJob = coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration
            )
        }
    }
}

@Composable
fun rememberSnackbarController(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): SnackbarController = remember(snackbarHostState, coroutineScope) {
    SnackbarController(
        snackbarHostState = snackbarHostState, coroutineScope = coroutineScope
    )
}