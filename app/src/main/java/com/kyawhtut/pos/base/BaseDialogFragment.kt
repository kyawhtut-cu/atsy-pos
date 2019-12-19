package com.kyawhtut.pos.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.getInflateView
import com.kyawhtut.pos.utils.isTablet

abstract class BaseDialogFragment(
    @LayoutRes private val layoutId: Int,
    private val fullScreen: Boolean = true
) : DialogFragment() {

    abstract fun setup(bundle: Bundle)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (fullScreen && context!!.isTablet()) setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialogStyle
        )
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

    override fun onStart() {
        super.onStart()
        if (!context!!.isTablet())
            dialog?.window?.let {
                it.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
    }
}