package com.kyawhtut.pos.ui.base

import android.content.SharedPreferences
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.sharedpreference.clear
import com.kyawhtut.pos.data.sharedpreference.get
import com.kyawhtut.pos.utils.Constants

abstract class BaseRepositoryImpl(private val sh: SharedPreferences) : BaseRepository {

    override fun isLogin() = sh.get(Constants.KEY_LOGIN, false)

    override fun logout() {
        sh.clear()
    }

    override fun getCurrentUser(): UserEntity? =
        UserEntity.toUserEntity(sh.get(Constants.KEY_LOGIN_USER, ""))
}