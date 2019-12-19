package com.kyawhtut.pos.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.kyawhtut.lib.minidrawer.DrawerItemBase
import com.kyawhtut.pos.base.BaseRepository

interface HomeRepository : BaseRepository {

    fun getSharedPreference(): SharedPreferences

    val isCartDataHas: LiveData<Int>

    fun getDrawerMenuList(
        context: Context,
        removeUnavailableFunction: Boolean = false
    ): List<DrawerItemBase>
}