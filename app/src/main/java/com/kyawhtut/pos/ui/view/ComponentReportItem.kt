package com.kyawhtut.pos.ui.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.getInflateView
import kotlinx.android.synthetic.main.component_report_item.view.*

/**
 * @author kyawhtut
 * @date 15/05/2020
 */
class ComponentReportItem : FrameLayout {

    private var onClickListener: OnClickListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(context.getInflateView(R.layout.component_report_item, this))
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.ComponentReportItem, defStyleAttr, 0)
        try {
            text = a.getString(R.styleable.ComponentReportItem_android_text) ?: ""
            setImageResource(
                a.getResourceId(
                    R.styleable.ComponentReportItem_android_icon,
                    R.drawable.ic_analysis_black
                )
            )
        } finally {
            a.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        iv_icon_open.setOnClickListener(onClickListener)
    }

    var text: String = ""
        set(value) {
            field = value
            tv_title.mText = value
        }

    fun setImageResource(resID: Int) {
        iv_icon.setImageResource(resID)
    }

    fun setImageDrawable(drawable: Drawable) {
        iv_icon.setImageDrawable(drawable)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        onClickListener = l
        super.setOnClickListener(l)
    }
}
