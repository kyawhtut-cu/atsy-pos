package com.kyawhtut.pos.ui.function

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.CartDao
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.sharedpreference.get
import com.kyawhtut.pos.data.vo.FunctionVO
import com.kyawhtut.pos.data.vo.functionList
import com.kyawhtut.pos.utils.Constants
import com.kyawhtut.pos.utils.getIntList
import com.kyawhtut.pos.utils.getStringList

class FunctionRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val cartDao: CartDao
) : BaseRepositoryImpl(sh, rootUser),
    FunctionRepository {

    private var functionIconList = arrayOf(
        R.drawable.ic_database_black,
        R.drawable.ic_shopping_bag_black,
        R.drawable.ic_bundle_white,
        R.drawable.ic_worker_black,
        R.drawable.ic_analysis_black,
        R.drawable.ic_sale_black,
        R.drawable.ic_user_group_black,
        R.drawable.ic_setting_black
    )

    override val isCartDataHas: LiveData<Boolean> = cartDao.getCartCount().map {
        it != 0
    }.toLiveData()

    override fun getFunctionList(
        context: Context,
        removeUnavailableFunction: Boolean
    ): List<FunctionVO> {
        val role = when (UserEntity.toUserEntity(sh.get(Constants.KEY_LOGIN_USER, ""))?.roleId) {
            1 -> R.array.super_admin_permission
            2, 3 -> R.array.admin_permission
            4 -> R.array.assistant_permission
            else -> R.array.sale_permission
        }
        functionList {
            context.getStringList(R.array.function).forEachIndexed { index, s ->
                function {
                    functionTitle = s
                    functionDescription = context.getStringList(R.array.function_description)[index]
                    functionAvailable = context.getIntList(role)[index] == 1
                    functionIcon = functionIconList[index]
                }
            }
        }.run {
            sortByDescending { it.functionAvailable }
            if (removeUnavailableFunction) this.filter {
                it.functionAvailable
            }.run {
                return this
            }
            return this
        }
    }
}