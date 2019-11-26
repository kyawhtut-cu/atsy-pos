package com.kyawhtut.pos.ui.base

import com.kyawhtut.pos.data.db.entity.UserEntity

interface BaseRepository {

    fun getCurrentUser(): UserEntity?

    fun isLogin(): Boolean

    fun logout()
}