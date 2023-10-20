package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class LocationDetails(
    @Expose
    @SerializedName("city")
    val city: Any,
    @Expose
    @SerializedName("country")
    val country: Any,
    @Expose
    @SerializedName("country_iso_code")
    val countryIsoCode: Any,
    @Expose
    @SerializedName("formatted_address")
    val formattedAddress: String,
    @Expose
    @SerializedName("latitude")
    val latitude: Any,
    @Expose
    @SerializedName("longitude")
    val longitude: Any,
    @Expose
    @SerializedName("neighborhood")
    val neighborhood: Any,
    @Expose
    @SerializedName("state")
    val state: Any,
    @Expose
    @SerializedName("state_iso_code")
    val stateIsoCode: Any,
    @Expose
    @SerializedName("sub_locality")
    val subLocality: Any
)