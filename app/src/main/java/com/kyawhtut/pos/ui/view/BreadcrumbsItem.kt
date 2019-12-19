package com.kyawhtut.pos.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.layout_breadcrumbs_view.view.*

class BreadcrumbsItem : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(context.getInflateView(R.layout.layout_breadcrumbs_view, this, false))
    }

    fun setItem(title: String, icon: Int = 0, isHome: Boolean, isSelected: Boolean) {
        if (icon == 0) iv_icon.invisible() else iv_icon.visible()
        if (icon != 0) iv_icon.setImageResource(icon)
        tv_title.apply {
            text = title
            setTextColor(
                context.getColorValue(
                    if (isSelected) R.color.colorAccent else R.color.default_textColor
                )
            )
        }
        if (isHome) divider_view.gone() else divider_view.visible()
    }
}