package com.kyawhtut.pos.ui.superadmin

import android.content.Context
import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.vo.FunctionVO

interface FunctionRepository : BaseRepository {

    fun getFunctionList(context: Context): List<FunctionVO>
}