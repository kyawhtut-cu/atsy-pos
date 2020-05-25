package com.kyawhtut.lib.rv

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.kyawhtut.lib.minidrawer.UIUtils.convertDpToPixel

class DividerItemDecoration(
    private val context: Context,
    private vararg val ignorePosition: Int,
    private var divider: Drawable? = null,
    private val ignoreLastItem: Boolean = false,
    private val paddingTop: Int = 0,
    private val paddingBottom: Int = 0,
    private val paddingRight: Int = 0,
    private val paddingLeft: Int = 0,
    private val padding: Int = -1
) : ItemDecoration() {

    init {
        if (divider == null) {
            val styledAttributes = context.obtainStyledAttributes(ATTRS)
            divider = styledAttributes.getDrawable(0)
            styledAttributes.recycle()
        }
    }

    private fun isIgnorePosition(pos: Int) = ignorePosition.indexOfFirst { it == pos } != -1

    override fun onDraw(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val left = parent.paddingLeft + convertDpToPixel((padding.takeIf { it != -1 }
            ?: paddingLeft).toFloat(), parent.context).toInt()
        val right = parent.width - parent.paddingRight
        val childCount = if (ignoreLastItem) parent.childCount - 1 else parent.childCount
        loop@ for (i in 0 until childCount) {
            if (isIgnorePosition(i)) continue@loop
            val child = parent.getChildAt(i)
            val params =
                child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight
            divider!!.setBounds(
                left,
                top,
                right - convertDpToPixel((padding.takeIf { it != -1 } ?: paddingRight).toFloat(),
                    parent.context).toInt(),
                bottom
            )
            divider!!.draw(c)
        }
    }

    companion object {
        private val ATTRS = intArrayOf(R.attr.listDivider)
    }
}