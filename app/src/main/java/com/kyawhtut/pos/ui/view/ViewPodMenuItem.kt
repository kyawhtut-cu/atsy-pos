package com.kyawhtut.pos.ui.view

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
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
            iconTint = a.getResourceId(R.styleable.ViewPodMenuItem_iconTintColor, iconTint)
            background = a.getResourceId(R.styleable.ViewPodMenuItem_background, background)
        } finally {
            a.recycle()
        }

//        iv_background.setOnClickListener { }
    }

    var icon: Int = R.drawable.ic_delete_black
        set(value) {
            field = value
            iv_icon.setImageResource(value)
        }

    var iconTint: Int = R.color.colorWhite
        set(value) {
            field = value
            iv_icon.setColorFilter(
                ContextCompat.getColor(context, value),
                PorterDuff.Mode.SRC_IN
            )
        }

    var background: Int = R.drawable.ic_circle_accent
        set(value) {
            field = value
            iv_icon.setBackgroundResource(value)
        }
}
