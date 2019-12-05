package com.kyawhtut.pos.ui.home

import android.content.SharedPreferences
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.entity.UserEntity

class HomeRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity
) : BaseRepositoryImpl(sh, rootUser),
    HomeRepository {
}
