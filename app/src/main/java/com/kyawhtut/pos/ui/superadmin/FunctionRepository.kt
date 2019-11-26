package com.kyawhtut.pos.ui.superadmin

import android.content.Context
import com.kyawhtut.pos.data.vo.FunctionVO
import com.kyawhtut.pos.ui.base.BaseRepository

interface FunctionRepository : BaseRepository {

    fun getFunctionList(context: Context): List<FunctionVO>
}