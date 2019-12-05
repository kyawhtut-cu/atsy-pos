package com.kyawhtut.pos.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import kotlinx.android.synthetic.main.tableview_cell_action_layout.view.root_layout
import kotlinx.android.synthetic.main.tableview_column_header_action_layout.view.*

class TableColumnHeaderActionViewHolder(
    private val view: View,
    private val callAdd: () -> Unit = {}
) : AbstractViewHolder(view) {

    init {
        view.btn_add.setOnClickListener {
            callAdd()
        }
    }

    fun bind() {
        view.root_layout.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        view.btn_add.requestLayout()
    }
}