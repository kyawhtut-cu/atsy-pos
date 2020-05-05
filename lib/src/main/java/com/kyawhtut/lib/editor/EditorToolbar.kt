package com.kyawhtut.lib.editor

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.jkcarino.rtexteditorview.RTextEditorView
import com.kyawhtut.lib.R
import kotlinx.android.synthetic.main.view_editor_toolbar.view.*

/**
 * @author kyawhtut
 * @date 03/05/2020
 */
class EditorToolbar : FrameLayout {

    companion object {
        val INSERT_LINK = R.id.btn_insert_link
        val INSERT_TABLE = R.id.btn_insert_table
        val TEXT_BACK_COLOR = R.id.btn_text_back_color
        val TEXT_COLOR = R.id.btn_text_color
    }

    var textEditorView: RTextEditorView? = null

    var onClick: (Int) -> Unit = {}

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addView(LayoutInflater.from(context).inflate(R.layout.view_editor_toolbar, this, false))
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        init()
    }

    private fun init() {
        for (i in 0 until tool_bar.childCount) {
            val button = tool_bar.getChildAt(i)
            if (button is ImageView) {
                button.setOnClickListener {
                    Log.d("getFormat", it.contentDescription.toString() + "$textEditorView")
                    textEditorView?.setFormat(getFormat(button.contentDescription.toString()).also {
                        Log.d("getFormat", it.toString())
                    })
                    onClick(it.id)
                }
            }
        }
    }

    private fun getFormat(value: String): Int {
        Log.d("getFormat", value)
        return when (value) {
            "bold" -> 1
            "italic" -> 2
            "underline" -> 3
            "strikethrough" -> 4
            "removeFormat" -> 5
            "normal" -> 6
            "h1" -> 7
            "h2" -> 8
            "h3" -> 9
            "h4" -> 10
            "h5" -> 11
            "h6" -> 12
            "superscript" -> 13
            "subscript" -> 14
            "textForeColor" -> 15
            "textBackColor" -> 16
            "blockCode" -> 17
            "unordered" -> 18
            "ordered" -> 19
            "blockQuote" -> 20
            "alignLeft" -> 21
            "alignCenter" -> 22
            "alignRight" -> 23
            "alignJustify" -> 24
            "horizontalRule" -> 25
            "indent" -> 26
            "outdent" -> 27
            "table" -> 28
            "link" -> 29
            "unlink" -> 30
            "clear" -> 31
            "editHtml" -> 32
            else -> 0
        }
    }
}
