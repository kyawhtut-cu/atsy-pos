package com.kyawhtut.pos.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kyawhtut.lib.minidrawer.DrawerItem
import com.kyawhtut.lib.minidrawer.DrawerItemType
import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.utils.Constants

class HomeViewModel(private val repo: HomeRepository) : BaseViewModel(repo) {

    private val sharedPreferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == Constants.KEY_LOGIN)
                _loginState.postValue(repo.isLogin())
        }

    init {
        repo.getSharedPreference()
            .registerOnSharedPreferenceChangeListener(sharedPreferenceListener)
    }

    private val _loginState = MutableLiveData<Boolean>(isLogin())
    fun getLoginState() = _loginState

    fun getDrawerMenu(context: Context) = repo.getDrawerMenuList(context, true)

    fun getIndex(context: Context, title: String) = getDrawerMenu(context).indexOfFirst {
        var data: DrawerItem? = null
        if (it.type is DrawerItemType.ITEM)
            data = it.data as DrawerItem
        data != null && data.title == title
    }

    val isCartDataHas: LiveData<Int> = repo.isCartDataHas

    override fun onCleared() {
        super.onCleared()
        repo.getSharedPreference()
            .unregisterOnSharedPreferenceChangeListener(sharedPreferenceListener)
    }
}