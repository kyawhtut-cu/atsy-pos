package com.kyawhtut.pos.ui.user

import android.content.SharedPreferences
import com.kyawhtut.pos.ui.base.BaseRepositoryImpl

class UserRepositoryImpl(private val sh: SharedPreferences) : BaseRepositoryImpl(sh),
    UserRepository {
}