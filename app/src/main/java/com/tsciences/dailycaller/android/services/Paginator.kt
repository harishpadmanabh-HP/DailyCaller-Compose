package com.tsciences.dailycaller.android.services

import com.tsciences.dailycaller.android.data.utils.NetworkResult
import com.tsciences.dailycaller.android.data.utils.doIfFailure
import com.tsciences.dailycaller.android.data.utils.doIfSuccess
import com.tsciences.dailycaller.android.ui.commonComponents.utils.UiText

class Paginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadingUpdate: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> NetworkResult<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (UiText?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) {
    private var currentKey: Key = initialKey
    private var isMakingRequest = false

    suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadingUpdate(true)
        onRequest(currentKey).apply {
            doIfFailure {
                onLoadingUpdate(false)
                onError(it)
            }
            doIfSuccess {
                currentKey = getNextKey(it)
                onSuccess(it, currentKey)
                onLoadingUpdate(false)
            }
        }
        isMakingRequest = false
    }

    fun reset() {
        currentKey = initialKey
    }
}