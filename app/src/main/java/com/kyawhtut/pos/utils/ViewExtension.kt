package com.kyawhtut.pos.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.kyawhtut.pos.data.adapter.PopupWindowAdapter

object ViewExtension

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context?.getInflateView(
    @LayoutRes layoutId: Int, container: ViewGroup? = null,
    attachToRoot: Boolean = false
) = LayoutInflater.from(this).inflate(layoutId, container, attachToRoot)

fun Context.measureContentWidth(adapter: PopupWindowAdapter): Int {
    var viewGroup: ViewGroup? = null
    var maxWidth = 0
    val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    var itemView: View? = null
    var itemType = 0
    for (i in 0 until adapter.count) {
        with(adapter.getItemViewType(i)) {
            if (this != itemType) {
                itemType = this
                itemView = null
            }
        }
        if (viewGroup == null) {
            viewGroup = FrameLayout(this)
        }

        itemView = adapter.getView(i, itemView, viewGroup)
        itemView?.measure(widthMeasureSpec, heightMeasureSpec)
        with(itemView?.measuredWidth ?: 0) {
            if (this > maxWidth) {
                maxWidth = this
            }
        }
    }
    return maxWidth
}