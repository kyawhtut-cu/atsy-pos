package com.kyawhtut.pos.ui.superadmin

import android.content.Context
import android.content.SharedPreferences
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.sharedpreference.get
import com.kyawhtut.pos.data.vo.FunctionVO
import com.kyawhtut.pos.data.vo.functionList
import com.kyawhtut.pos.ui.base.BaseRepositoryImpl
import com.kyawhtut.pos.utils.Constants
import com.kyawhtut.pos.utils.getIntList
import com.kyawhtut.pos.utils.getStringList

class FunctionRepositoryImpl(private val sh: SharedPreferences) : BaseRepositoryImpl(sh),
    FunctionRepository {

    override fun getFunctionList(context: Context): List<FunctionVO> {
        val role = when (UserEntity.toUserEntity(sh.get(Constants.KEY_LOGIN_USER, ""))?.roleId) {
            1 -> R.array.super_admin_permission
            2, 3 -> R.array.admin_permission
            4 -> R.array.assistant_permission
            else -> R.array.sale_permission
        }
        return functionList {
            context.getStringList(R.array.function).forEachIndexed { index, s ->
                function {
                    functionTitle = s
                    functionDescription = context.getStringList(R.array.function_description)[index]
                    functionAvailable = context.getIntList(role)[index] == 1
                }
            }
        }
    }
}