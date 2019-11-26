package com.kyawhtut.pos.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.getInflateView

abstract class BaseDialogFragment(@LayoutRes private val layoutId: Int, private val fullScreen: Boolean = true) :
    DialogFragment() {

    abstract fun setup(bundle: Bundle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (fullScreen) setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context.getInflateView(layoutId, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(arguments ?: Bundle())
    }
}