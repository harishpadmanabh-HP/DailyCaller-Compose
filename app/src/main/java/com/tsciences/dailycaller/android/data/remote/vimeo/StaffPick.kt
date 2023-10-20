package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class StaffPick(
    @Expose
    @SerializedName("best_of_the_month")
    val bestOfTheMonth: Boolean,
    @Expose
    @SerializedName("best_of_the_year")
    val bestOfTheYear: Boolean,
    @Expose
    @SerializedName("normal")
    val normal: Boolean,
    @Expose
    @SerializedName("premiere")
    val premiere: Boolean
)