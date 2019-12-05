package com.kyawhtut.pos.utils

import android.view.View
import android.view.ViewTreeObserver
import android.widget.ListPopupWindow
import com.kyawhtut.pos.R
import com.kyawhtut.pos.adapter.PopupWindowAdapter

class PopupDialog private constructor(
    private val view: View,
    data: MutableList<PopupItem>,
    private val callback: PopupItem.() -> Unit = {}
) {

    init {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                popupWindow.verticalOffset = offsetY - view.measuredHeight
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private val context = view.context
    private val offsetY =
        context.resources.getDimensionPixelOffset(R.dimen.dropdown_offset_y_fix_value)
    private val adapter = PopupWindowAdapter(data)
    private val popupWindow: ListPopupWindow = ListPopupWindow(context).apply {
        anchorView = view
        setAdapter(adapter)
        width = 400
        setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position).callback()
            dismiss()
        }
    }

    fun show() = popupWindow.show()
    fun dismiss() = popupWindow.dismiss()

    class Builder {
        var view: View? = null
        private var data = mutableListOf<PopupItem>()
        fun popupItemList(block: PopupItemList.() -> Unit) {
            data.addAll(PopupItemList().apply(block))
        }

        var callback: PopupItem.() -> Unit = {}

        fun bind() = PopupDialog(view!!, data, callback)
    }
}

fun popupDialogBuilder(block: PopupDialog.Builder.() -> Unit) =
    PopupDialog.Builder().apply(block)

fun popupDialog(block: PopupDialog.Builder.() -> Unit) = popupDialogBuilder(block).bind()