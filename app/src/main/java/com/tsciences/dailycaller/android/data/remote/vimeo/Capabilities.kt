package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Capabilities(
    @Expose
    @SerializedName("hasEnterpriseLihp")
    val hasEnterpriseLihp: Boolean,
    @Expose
    @SerializedName("hasLiveSubscription")
    val hasLiveSubscription: Boolean,
    @Expose
    @SerializedName("hasSimplifiedEnterpriseAccount")
    val hasSimplifiedEnterpriseAccount: Boolean,
    @Expose
    @SerializedName("hasSvvTimecodedComments")
    val hasSvvTimecodedComments: Boolean
)