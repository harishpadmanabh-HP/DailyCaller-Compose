package com.tsciences.dailycaller.android.data.remote.home

import androidx.annotation.Keep
import com.google.gson.JsonArray
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue
import org.json.JSONArray
import org.json.JSONObject

@Keep
data class NewsResponse(
    @Expose
    @SerializedName("title")
    var title: String? = null,

    @Expose
    @SerializedName("description")
    val description: String? = null,

    @Expose
    @SerializedName("item")
    val newsItem: List<Item> = emptyList()
)

@Keep
data class Item(
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("dc:headline")
    @Expose
    var headline: String? = null,

    @SerializedName("category")
    @Expose
    val category: @RawValue JsonArray? = null,

    @SerializedName("link")
    @Expose
    val link: String? = null,

    @SerializedName("creator")
    @Expose
    val creator: PrefixText? = null,

    @SerializedName("encoded")
    @Expose
    val encoded: PrefixText? = null,

    @SerializedName("content")
    @Expose
    private val content: Content? = null,

    @SerializedName("image")
    @Expose
    val image: Image? = null,

    @SerializedName("gallery")
    @Expose
    val oGallery: Gallery? = null,

    @SerializedName("pubDate")
    @Expose
    val pubDate: String? = null,

    @SerializedName("guid")
    @Expose
    private val guid: String? = null,

    @SerializedName("description")
    @Expose
    private val description: String? = null,

    var isFirstItem: Boolean? = false,
    var categoryTag: String? = null,
    var authorName: String? = null,
    var authorImage: String? = null,
    var imageUrl: String? = null,
    var imageLargeUrl: String? = null,
    var imageMediumUrl: String? = null,
    var encodedString: String? = null,
    var postID: String? = null,
    var premiumContent: Boolean = false,
    var isDcFoundation: Boolean = false,
    var categoryObj: CategoryObj? = null,
) {
    fun setCategory(jsonArray: JsonArray) {
        try {
            val categoryArray = JSONArray(jsonArray.toString())
            for (i in 0 until categoryArray.length()) {
                if (categoryArray[i] is JSONObject) {
                    val catObj = categoryArray.getJSONObject(i)
                    this.categoryObj = CategoryObj(catObj)

                    //set author name
                    if (categoryObj?.domain
                            .equals("author")
                    ) {
                        this.authorName = categoryObj?.text
                    }

                    //set author image
                    if (categoryObj?.domain
                            .equals("author_image")
                    ) {
                        this.authorImage = categoryObj?.text
                    }

                    //set image full url
                    if (categoryObj?.domain
                            .equals("dc:image_full")
                    ) {
                        this.imageUrl = categoryObj?.text
                    }

                    //set image Medium url
                    if (categoryObj?.domain
                            .equals("dc:image_medium")
                    ) {
                        this.imageMediumUrl = categoryObj?.text
                    }

                    //set image Large url
                    if (categoryObj?.domain
                            .equals("dc:image_large")
                    ) {
                        this.imageLargeUrl = categoryObj?.text
                    }

                    //set premium content
                    if (categoryObj?.domain.equals("category") && categoryObj?.text
                            .equals("premium-content")
                    ) this.premiumContent = true

                    //set dc foundation
                    if (categoryObj?.domain.equals("section") && categoryObj?.text
                            .equals("daily-caller-news-foundation")
                    ) this.isDcFoundation = true

                    //set dc post:id
                    if (categoryObj?.domain.equals("dc:postid")
                    ) this.postID = categoryObj?.text

                    //set Title
                    if (categoryObj?.domain.equals("dc:headline")
                    ) this.headline = categoryObj?.text

                } else if (categoryArray[i] is String) {
                    this.categoryTag = categoryArray.getString(i)
                }
            }
        } catch (er: Exception) {
            er.printStackTrace()
        }
    }
}

@Keep
data class PrefixText(
    @SerializedName("__prefix")
    @Expose
    private var prefix: String? = null,
    @SerializedName("__text")
    @Expose
    val text: String? = null
)

@Keep
data class Content(
    @SerializedName("__prefix")
    @Expose
    private var prefix: String? = null,
    @SerializedName("__text")
    @Expose
    private val text: String? = null
)

@Keep
data class Image(
    @SerializedName("__prefix")
    @Expose
    private var prefix: String? = null,

    @SerializedName("loc")
    @Expose
    var loc: PrefixText? = null,

    @SerializedName("title")
    @Expose
    private val title: PrefixText? = null,

    @SerializedName("caption")
    @Expose
    private val caption: PrefixText? = null
)

@Keep
data class Gallery(
    @SerializedName("__prefix")
    @Expose
    var prefix: String? = null,
    @SerializedName("__text")
    val text: String? = null,

    @SerializedName("slide")
    @Expose
    val slideList: List<Slide>? = null
)

@Keep
data class Slide(
    @SerializedName("_title")
    @Expose
    var slideTitle: String? = null,
    @SerializedName("_full")
    @Expose
    val slideFullImage: String? = null,

    @SerializedName("_name")
    @Expose
    val slideName: String? = null,

    @SerializedName("_thumb")
    @Expose
    val slideThumbImage: String? = null
)