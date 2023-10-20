package com.tsciences.dailycaller.android.ui.menu

import com.tsciences.dailycaller.android.data.remote.streams.UserIdResponse

data class StreamState(
    val userIdResponse: UserIdResponse? = null, val loading: Boolean = false
)
