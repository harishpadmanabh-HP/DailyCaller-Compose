package com.tsciences.dailycaller.android.data.remote.home

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject

class CategoryObj : Parcelable {
    @SerializedName("_domain")
    @Expose
    var domain: String? = null

    @SerializedName("__text")
    @Expose
    var text: String? = null

    constructor(jsonObject: JSONObject) {
        try {
            domain = jsonObject.getString("_domain")
        } catch (e: JSONException) {
        }
        try {
            text = jsonObject.getString("__text")
        } catch (e: JSONException) {
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(domain)
        dest.writeString(text)
    }

    protected constructor(`in`: Parcel) {
        domain = `in`.readString()
        text = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<CategoryObj> {
        override fun createFromParcel(parcel: Parcel): CategoryObj {
            return CategoryObj(parcel)
        }

        override fun newArray(size: Int): Array<CategoryObj?> {
            return arrayOfNulls(size)
        }
    }
}


