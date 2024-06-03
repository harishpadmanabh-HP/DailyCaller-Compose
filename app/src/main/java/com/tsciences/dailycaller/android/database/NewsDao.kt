package com.tsciences.dailycaller.android.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {
    @Insert
    fun insertUser(oNewsModel: NewsModel)

    @Query("DELETE FROM news_item")
    fun deleteNews()

    @Query("DELETE FROM news_item WHERE news_title =:title and news_time =:time")
    fun removeNews(title: String?, time: String?)

    @Query("SELECT * FROM news_item")
    fun getNewsList(): List<NewsModel?>?

    @Query("SELECT COUNT(*) from news_item")
    fun countNews(): Int

    @Query("SELECT * FROM news_item WHERE news_title =:title and news_time =:time")
    fun getNews(title: String?, time: String?): List<NewsModel?>?
}