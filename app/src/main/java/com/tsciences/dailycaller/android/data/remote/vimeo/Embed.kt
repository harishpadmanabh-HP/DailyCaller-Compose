package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class Embed(
    @Expose
    @SerializedName("autopip")
    val autopip: Boolean,
    @Expose
    @SerializedName("badges")
    val badges: Badges,
    @Expose
    @SerializedName("buttons")
    val buttons: Buttons,
    @Expose
    @SerializedName("cards")
    val cards: List<Any>,
    @Expose
    @SerializedName("color")
    val color: String,
    @Expose
    @SerializedName("colors")
    val colors: Colors,
    @Expose
    @SerializedName("email_capture_form")
    val emailCaptureForm: Any,
    @Expose
    @SerializedName("end_screen")
    val endScreen: List<Any>,
    @Expose
    @SerializedName("event_schedule")
    val eventSchedule: Boolean,
    @Expose
    @SerializedName("has_cards")
    val hasCards: Boolean,
    @Expose
    @SerializedName("html")
    val html: String,
    @Expose
    @SerializedName("interactive")
    val interactive: Boolean,
    @Expose
    @SerializedName("logos")
    val logos: Logos,
    @Expose
    @SerializedName("outro_type")
    val outroType: String,
    @Expose
    @SerializedName("pip")
    val pip: Boolean,
    @Expose
    @SerializedName("playbar")
    val playbar: Boolean,
    @Expose
    @SerializedName("show_timezone")
    val showTimezone: Boolean,
    @Expose
    @SerializedName("speed")
    val speed: Boolean,
    @Expose
    @SerializedName("title")
    val title: Title,
    @Expose
    @SerializedName("uri")
    val uri: Any,
    @Expose
    @SerializedName("volume")
    val volume: Boolean
)