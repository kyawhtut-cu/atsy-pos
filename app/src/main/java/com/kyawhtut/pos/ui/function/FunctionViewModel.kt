package com.kyawhtut.pos.ui.function

import android.content.Context
import com.kyawhtut.pos.base.BaseViewModel

class FunctionViewModel(private val context: Context, private val repo: FunctionRepository) :
    BaseViewModel(repo) {

    val isCartDataHas = repo.isCartDataHas
    private var removeUnavailableFunction = false
    var oldTitle: String = "Sale"

    fun getFunctionList(removeUnavailableFunction: Boolean = this.removeUnavailableFunction) =
        repo.getFunctionList(context, removeUnavailableFunction).also {
            this.removeUnavailableFunction = removeUnavailableFunction
        }

    fun getTitleIndex(title: String) =
        getFunctionList(removeUnavailableFunction).indexOfFirst { it.functionTitle == title }
}
