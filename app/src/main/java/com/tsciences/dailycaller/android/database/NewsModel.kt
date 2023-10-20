package com.tsciences.dailycaller.android.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_item")
data class NewsModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "news_id")
    var newsId: Int? = null,

    @ColumnInfo(name = "news_title")
    var newsTitle: String? = null,

    @ColumnInfo(name = "news_time")
    var newsTime: String? = null,

    @ColumnInfo(name = "author_name")
    var authorName: String? = null,

    @ColumnInfo(name = "author_image")
    var authorImage: String? = null,

    @ColumnInfo(name = "news_image")
    var newsImage: String? = null,

    @ColumnInfo(name = "news_large")
    var largeImage: String? = null,

    @ColumnInfo(name = "news_medium")
    var mediumImage: String? = null,

    @ColumnInfo(name = "news_category")
    var category: String? = null,

    @ColumnInfo(name = "news_link")
    var newsLink: String? = null,

    @ColumnInfo(name = "dcFoundation")
    var dcFoundation: Boolean? = null,

    @ColumnInfo(name = "premiumContent")
    var premiumContent: Boolean? = null,

    @ColumnInfo(name = "encoded")
    var encodedString: String? = null
)

