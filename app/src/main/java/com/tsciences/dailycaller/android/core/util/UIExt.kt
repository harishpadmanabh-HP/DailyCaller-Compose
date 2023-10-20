package com.tsciences.dailycaller.android.core.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun Context.popActivity() {
    (this as ComponentActivity).finish()
}

fun Context.findActivity(): Activity? = when (this) {
    is AppCompatActivity -> this
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity() as Activity
    else -> null
}

fun Color.onVariant(): Color = if (this.luminance() > 0.5f) Color.Black else Color.White