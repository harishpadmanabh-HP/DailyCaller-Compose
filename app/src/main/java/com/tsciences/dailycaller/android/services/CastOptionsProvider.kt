package com.tsciences.dailycaller.android.services

import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.LaunchOptions
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.*
import com.google.android.gms.common.images.WebImage
import com.tsciences.dailycaller.android.ui.menu.ExpandedControlsActivity

class CastOptionsProvider : OptionsProvider {
    companion object {
        const val CUSTOM_NAMESPACE = "urn:x-cast:custom_namespace"
    }

    override fun getCastOptions(appContext: Context): CastOptions {
        val supportedNamespaces: MutableList<String> = ArrayList()
        supportedNamespaces.add(CUSTOM_NAMESPACE)
        val notificationOptions = NotificationOptions.Builder().setActions(
                listOf(
                    MediaIntentReceiver.ACTION_SKIP_NEXT,
                    MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK,
                    MediaIntentReceiver.ACTION_STOP_CASTING
                ), intArrayOf(1, 2)
            ).setTargetActivityClassName(ExpandedControlsActivity::class.java.name).build()
        val mediaOptions = CastMediaOptions.Builder().setImagePicker(ImagePickerImpl())
            .setNotificationOptions(notificationOptions)
            .setExpandedControllerActivityClassName(ExpandedControlsActivity::class.java.name)
            .build()
        val launchOptions = LaunchOptions.Builder().setAndroidReceiverCompatible(true).build()
        return CastOptions.Builder()
            .setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)

            .setLaunchOptions(launchOptions).setCastMediaOptions(mediaOptions).build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }

    private class ImagePickerImpl : ImagePicker() {
        override fun onPickImage(mediaMetadata: MediaMetadata?, hints: ImageHints): WebImage? {
            val type = hints.type
            if (!mediaMetadata!!.hasImages()) {
                return null
            }
            val images = mediaMetadata.images
            return if (images.size == 1) {
                images[0]
            } else {
                if (type == IMAGE_TYPE_MEDIA_ROUTE_CONTROLLER_DIALOG_BACKGROUND) {
                    images[0]
                } else {
                    images[1]
                }
            }
        }
    }
}