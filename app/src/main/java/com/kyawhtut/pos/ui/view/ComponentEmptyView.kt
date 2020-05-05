package com.kyawhtut.pos.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.getInflateView
import kotlinx.android.synthetic.main.component_empty_view.view.*

/**
 * @author kyawhtut
 * @date 02/05/2020
 */
class ComponentEmptyView : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(context.getInflateView(R.layout.component_empty_view, this, false))
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.ComponentEmptyView, defStyleAttr, 0)
        try {
            text = a.getString(R.styleable.ComponentEmptyView_android_text) ?: ""
        } finally {
            a.recycle()
        }
    }

    var text: String = ""
        set(value) {
            field = value
            tv_message.mText = value
        }
}
