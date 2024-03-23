package com.tsciences.dailycaller.android.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tsciences.dailycaller.android.BuildConfig
import com.tsciences.dailycaller.android.data.preferences.SharedPrefService
import com.tsciences.dailycaller.android.data.remote.streams.UserIdResponse
import com.tsciences.dailycaller.android.data.remote.streams.VideoAccessResponse
import com.tsciences.dailycaller.android.data.repository.DocumentaryRepository
import com.tsciences.dailycaller.android.data.utils.doIfFailure
import com.tsciences.dailycaller.android.data.utils.doIfSuccess
import com.tsciences.dailycaller.android.ui.commonComponents.utils.UiText
import com.tsciences.dailycaller.android.ui.home.HomeViewModel
import com.tsciences.dailycaller.android.utils.toStateFlow
import com.vimeo.networking2.*
import com.vimeo.networking2.config.VimeoApiConfiguration
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaRouteViewModel @Inject constructor(
    private val repo: DocumentaryRepository,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {
    private val _failureMessage = MutableLiveData<String>()
    private val _hasAccess = MutableLiveData<Boolean>()
    private val _video = MutableLiveData<Video>()

    val hasAccess: LiveData<Boolean> get() = _hasAccess
    val failureMessage: LiveData<String> get() = _failureMessage
    val video: LiveData<Video> get() = _video


    private val isListLoading = MutableStateFlow(false)
    private val eventsChannel = Channel<HomeViewModel.HomeEvent>()
    private val userIdResponse = MutableStateFlow<UserIdResponse?>(null)
    private val videoAccessResponse = MutableStateFlow<VideoAccessResponse?>(null)

    fun getPianoToken() =
        sharedPrefService.getPianoToken()

    fun setLoginStatus(token: String) {
        sharedPrefService.setPianoToken(token)
    }

    val state = combineTuple(
        userIdResponse,
        isListLoading
    ).map { (
                userIdResponse,
                isListLoading
            ) ->
        StreamState(
            userIdResponse = userIdResponse,
            loading = isListLoading
        )
    }.toStateFlow(viewModelScope, StreamState())

    fun getUserId(userToken: String) = viewModelScope.launch {
        isListLoading.update { true }
        repo.getUserId(BuildConfig.PIANO_AID, userToken).apply {
            isListLoading.update { false }
            doIfFailure {
                _failureMessage.postValue(it.toString())
            }
            doIfSuccess { userId ->
                getUserAccess(userId.user.uid)
            }
        }
    }

    private fun getUserAccess(uid: String) = viewModelScope.launch {
        isListLoading.update { true }
        repo.getUserAccess(BuildConfig.PIANO_AID, uid, BuildConfig.PIANO_TOKEN).apply {
            isListLoading.update { false }
            doIfFailure {
                _failureMessage.postValue(it.toString())
            }
            doIfSuccess { videoAccess ->
                val hasAccess = checkVideoAccess(videoAccess)
                _hasAccess.postValue(hasAccess)

            }
        }
    }

    fun getVideoUrl(videoId: String) {
        val confBuilder = VimeoApiConfiguration.Builder(BuildConfig.VIMEO_ID)
        val vimeoUrl = BuildConfig.VIMEO_BASE_URL + videoId
        val configuration = confBuilder.build()
        Authenticator.initialize(configuration)
        VimeoApiClient.initialize(configuration, Authenticator.instance())
        VimeoApiClient.instance()
            .fetchVideo(vimeoUrl, null, null, null, object : VimeoCallback<Video> {
                override fun onError(error: VimeoResponse.Error) {
                    _failureMessage.postValue("We have encountered difficulty locating the video.")
                }

                override fun onSuccess(response: VimeoResponse.Success<Video>) {
                    val video = response.data
                    _video.postValue(video)
                }
            })
    }

    private fun checkVideoAccess(videoAccess: VideoAccessResponse): Boolean {
        var hasAccess = false
        for (item in videoAccess.accesses) {
            if (item.resource.name.lowercase().contains("patriots") || item.resource.name.lowercase().contains("founder")) {
                hasAccess = true
                break
            }
        }
        return hasAccess
    }

    sealed class StreamEvent {
        data class ShowUiMessage(val uiText: UiText) : StreamEvent()
    }
}