package com.kyawhtut.pos.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author kyawhtut
 * @date 07/05/2020
 */
abstract class BaseBottomSheetDialog(@LayoutRes private val layoutRes: Int) :
    BottomSheetDialogFragment() {

    open fun onViewCreated() {}
    open fun onSlide(slideOffset: Float = 0f) {}

    protected fun <T> get(key: String, default: T) = arguments?.get(key) as T ?: default
    open val layoutPercent: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            window?.setDimAmount(0f)
            setOnShowListener {
                setupBottomSheetDialog(this)
            }
            setOnKeyListener { _, _, event ->
                if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                    onSlide()
                    dismiss()
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    override fun onStart() {
        super.onStart()
        var bottomSheet: View? = null
        dialog?.let {
            bottomSheet =
                (it.findViewById(com.google.android.material.R.id.design_bottom_sheet) as View).apply {
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
        }
        view?.let {
            it.post {
                val parent = it.parent as View
                val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior as BottomSheetBehavior
                if (layoutPercent == 0f) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    behavior.peekHeight = (100 * layoutPercent).toInt()
                }
                (bottomSheet?.parent as View).setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun setupBottomSheetDialog(dialog: BottomSheetDialog) {
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                ?: return
        BottomSheetBehavior.from(bottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isHideable = true
            addBottomSheetCallback(getBottomSheetCallback())
        }
    }

    private fun getBottomSheetCallback(): BottomSheetBehavior.BottomSheetCallback {
        return object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                onSlide(slideOffset)
            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                }
            }
        }
    }
}
