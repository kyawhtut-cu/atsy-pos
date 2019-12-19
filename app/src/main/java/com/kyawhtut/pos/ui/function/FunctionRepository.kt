package com.kyawhtut.pos.ui.function

import android.content.Context
import androidx.lifecycle.LiveData
import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.vo.FunctionVO

interface FunctionRepository : BaseRepository {

    val isCartDataHas: LiveData<Boolean>

    fun getFunctionList(
        context: Context,
        removeUnavailableFunction: Boolean = false
    ): List<FunctionVO>
}