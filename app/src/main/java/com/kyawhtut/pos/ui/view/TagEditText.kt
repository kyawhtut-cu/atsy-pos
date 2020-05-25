package com.kyawhtut.pos.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.widget.FrameLayout
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.getItems
import com.kyawhtut.lib.rv.remove
import com.kyawhtut.lib.rv.update
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.getInflateView
import kotlinx.android.synthetic.main.item_tag_view.view.*
import kotlinx.android.synthetic.main.viewpod_tag_edittext.view.*

class TagEditText : FrameLayout {

    private var tagsList = mutableListOf<String>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(context.getInflateView(R.layout.viewpod_tag_edittext, this, false))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        rv_tags.apply {
            bind(emptyList<String>(), R.layout.item_tag_view) { item, pos ->
                this.tv_tag.text = item
                this.tv_tag.setOnCloseIconClickListener {
                    rootView.rv_tags.remove(item)
                }
            }.layoutManager(FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP).apply {
                alignItems = AlignItems.CENTER
            })
        }

        ed_tag.apply {
            setOnKeyListener { _, keyCode, event ->
                if (ed_tag.text.toString().isNotEmpty() && keyCode == 66 && event.action == KeyEvent.ACTION_UP) {
                    val tags = mutableListOf(ed_tag.text.toString())
                    tagList = tags
                    ed_tag.text.clear()
                    ed_tag.isEnabled = true
                    ed_tag.requestFocus()
                }
                false
            }
        }
    }

    var tagList = mutableListOf<String>()
        set(value) {
            field = value
            value.map {
                if (!tagsList.contains(it)) {
                    tagsList.add(it)
                    rv_tags.update(tagsList)
                }
            }
        }
        get() = rv_tags.getItems()
}
