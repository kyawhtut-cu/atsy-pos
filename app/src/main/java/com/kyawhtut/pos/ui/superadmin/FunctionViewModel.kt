package com.kyawhtut.pos.ui.superadmin

import android.content.Context
import com.kyawhtut.pos.base.BaseViewModel

class FunctionViewModel(private val context: Context, private val repo: FunctionRepository) :
    BaseViewModel(repo) {

    fun getFunctionList() = repo.getFunctionList(context)
}
