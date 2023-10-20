package com.tsciences.dailycaller.android.data.remote.vimeo


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep

data class InteractionsX(
    @Expose
    @SerializedName("add_subfolder")
    val addSubfolder: AddSubfolder,

    @Expose
    @SerializedName("delete_video")
    val deleteVideo: DeleteVideo,
    @Expose
    @SerializedName("edit")
    val edit: EditX,
    @Expose
    @SerializedName("edit_settings")
    val editSettings: EditSettings,

    @Expose
    @SerializedName("move_video")
    val moveVideo: MoveVideo,
    @Expose
    @SerializedName("upload_video")
    val uploadVideo: UploadVideo,
    @Expose
    @SerializedName("view")
    val view: View
)