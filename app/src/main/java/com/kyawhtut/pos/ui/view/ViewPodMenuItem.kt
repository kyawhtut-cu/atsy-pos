package com.kyawhtut.pos.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.kyawhtut.pos.R
import kotlinx.android.synthetic.main.view_pod_menu_item.view.*

class ViewPodMenuItem : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(LayoutInflater.from(context).inflate(R.layout.view_pod_menu_item, this, false))
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewPodMenuItem, defStyleAttr, 0)
        try {
            icon = a.getResourceId(R.styleable.ViewPodMenuItem_icon, icon)
            background = a.getResourceId(R.styleable.ViewPodMenuItem_background, background)
        } finally {
            a.recycle()
        }
    }

    var icon: Int = R.drawable.ic_delete_black
        set(value) {
            field = value
            iv_icon.setImageResource(value)
        }
    var background: Int = R.drawable.bg_outline_box_active
        set(value) {
            field = value
            iv_background.setBackgroundResource(value)
        }
}