package com.kyawhtut.pos.base

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes

abstract class BaseActivityViewModel<V : BaseViewModel>(
    @LayoutRes private val layoutId: Int,
    @MenuRes private val menuId: Int? = null,
    @IdRes private val toolbar: Int? = null,
    isBackActivity: Boolean = false
) : BaseActivity(layoutId, menuId, toolbar, isBackActivity) {

    abstract val viewModel: V
}
