package com.tsciences.dailycaller.android.ui.home

import com.tsciences.dailycaller.android.data.remote.home.Item
import com.tsciences.dailycaller.android.data.remote.home.MenuItem

data class HomeState(
    val newsFirstItem: Item? = null,
    val menuItems: List<MenuItem> = emptyList(),
    val newsFullItems: List<Item> = emptyList(),
    val newsFirstThreeTabItems: List<Item> = emptyList(),
    val newsFullTabItems: List<Item> = emptyList(),
    val listPage: Int = 0,
    val hasListEndReached: Boolean = false,
    val loading: Boolean = false,
    val isLogin: Boolean = false,
    val savedNews: Int = 0
)
