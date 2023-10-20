package com.tsciences.dailycaller.android.data.utils

import com.tsciences.dailycaller.android.appConstants.AppConstants
import kotlinx.coroutines.flow.SharingStarted

// Default Sharing strategy with **AppConstants.STATE_FLOW_SUBSCRIPTION_TIMEOUT** as timeout
val ShareWhileSubscribed: SharingStarted
    get() = SharingStarted.WhileSubscribed(AppConstants.STATE_FLOW_SUBSCRIPTION_TIMEOUT)

// Default Sharing strategy as function with timeout as param
fun shareWhileSubscribed(
    timeoutMillis: Long = AppConstants.STATE_FLOW_SUBSCRIPTION_TIMEOUT
): SharingStarted = SharingStarted.WhileSubscribed(timeoutMillis)