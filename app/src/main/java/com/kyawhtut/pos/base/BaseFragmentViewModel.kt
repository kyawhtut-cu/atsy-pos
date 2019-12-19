package com.kyawhtut.pos.base

import androidx.annotation.LayoutRes

abstract class BaseFragmentViewModel<V : BaseViewModel>(@LayoutRes private val layoutId: Int) :
    BaseFragment(layoutId) {

    abstract val viewModel: V
}