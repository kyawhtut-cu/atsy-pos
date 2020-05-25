package com.kyawhtut.pos.ui.sale

import android.content.SharedPreferences
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.entity.UserEntity

class SaleRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity
) : BaseRepositoryImpl(sh, rootUser),
    SaleRepository {
}
