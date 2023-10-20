package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Colors(
    @Expose
    @SerializedName("color_four")
    val colorFour: String,
    @Expose
    @SerializedName("color_one")
    val colorOne: String,
    @Expose
    @SerializedName("color_three")
    val colorThree: String,
    @Expose
    @SerializedName("color_two")
    val colorTwo: String
)