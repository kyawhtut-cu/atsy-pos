package com.kyawhtut.pos.ui.superadmin

import android.content.Context
import com.kyawhtut.pos.ui.base.BaseViewModel
import timber.log.Timber

class FunctionViewModel(private val context: Context, private val repo: FunctionRepository) :
    BaseViewModel(repo) {

    fun getFunctionList() = repo.getFunctionList(context).also {
        Timber.d("getFunctionList() -> %s", it)
    }
}