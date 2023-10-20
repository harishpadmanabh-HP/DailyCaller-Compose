package com.tsciences.dailycaller.android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NewsModel::class], version = 1, exportSchema = false)
abstract class DailyCallerDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: DailyCallerDatabase? = null

        fun getDatabase(context: Context): DailyCallerDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, DailyCallerDatabase::class.java, "news_database")
                .fallbackToDestructiveMigration()
                .build()
    }
}
