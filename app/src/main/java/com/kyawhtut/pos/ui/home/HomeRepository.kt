package com.kyawhtut.pos.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.kyawhtut.lib.minidrawer.DrawerItemBase
import com.kyawhtut.pos.base.BaseRepository

interface HomeRepository : BaseRepository {

    val isLowerItem: LiveData<Int>

    fun registerSharedPreference(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    fun unregisterSharedPreference(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    val isCartDataHas: LiveData<Int>

    fun getDrawerMenuList(
        context: Context,
        removeUnavailableFunction: Boolean = false
    ): List<DrawerItemBase>
}
