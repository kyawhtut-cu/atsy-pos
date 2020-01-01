package com.kyawhtut.pos.base

import android.content.SharedPreferences
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.sharedpreference.clear
import com.kyawhtut.pos.data.sharedpreference.get
import com.kyawhtut.pos.data.sharedpreference.put
import com.kyawhtut.pos.utils.Constants

abstract class BaseRepositoryImpl(
    protected val sh: SharedPreferences,
    protected val rootUser: UserEntity
) : BaseRepository {

    override var taxAmount: Int
        get() = sh.getInt(Constants.KEY_TAX_AMOUNT, 5)
        set(value) {
            sh.put(Constants.KEY_TAX_AMOUNT, value)
        }

    override var limitAmount: Int
        get() = sh.get(Constants.KEY_LIMIT_AMOUNT, 5)
        set(value) {
            sh.put(Constants.KEY_LIMIT_AMOUNT, value)
        }

    override fun isLogin() = sh.get(Constants.KEY_LOGIN, false)

    override fun logout() {
        sh.clear()
        sh.put(Constants.KEY_LOGIN, false)
    }

    override fun getCurrentUser(): UserEntity? =
        UserEntity.toUserEntity(sh.get(Constants.KEY_LOGIN_USER, ""))
}