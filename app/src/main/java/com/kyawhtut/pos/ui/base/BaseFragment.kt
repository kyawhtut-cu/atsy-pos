package com.kyawhtut.pos.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.kyawhtut.pos.utils.getInflateView

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment() {

    abstract fun onViewCreated(bundle: Bundle)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context.getInflateView(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(arguments ?: Bundle())
    }
}