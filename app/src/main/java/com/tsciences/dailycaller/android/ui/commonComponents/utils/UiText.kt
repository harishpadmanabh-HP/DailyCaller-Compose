package com.tsciences.dailycaller.android.ui.commonComponents.utils

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tsciences.dailycaller.android.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class UiText : Parcelable {
    data class DynamicText(val message: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: String
    ) : UiText()

    companion object {
        fun unknownError(): UiText = StringResource(R.string.error_unknown)
    }

    fun asString(context: Context): String = when (this) {
        is DynamicText -> message
        is StringResource -> context.getString(resId, *args)
    }

    @Composable
    fun asString(): String = when (this) {
        is DynamicText -> message
        is StringResource -> stringResource(resId, *args)
    }
}