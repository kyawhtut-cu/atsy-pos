package com.kyawhtut.pos.ui.home

import android.content.SharedPreferences
import com.kyawhtut.pos.ui.base.BaseRepositoryImpl

class HomeRepositoryImpl(private val sh: SharedPreferences) : BaseRepositoryImpl(sh),
    HomeRepository {
}
