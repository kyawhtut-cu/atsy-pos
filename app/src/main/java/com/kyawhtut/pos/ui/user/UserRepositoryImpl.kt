package com.kyawhtut.pos.ui.user

import android.content.SharedPreferences
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.entity.UserEntity

class UserRepositoryImpl(sh: SharedPreferences, rootUser: UserEntity) :
    BaseRepositoryImpl(sh, rootUser),
    UserRepository {
}