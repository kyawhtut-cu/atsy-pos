package com.kyawhtut.pos.adapter.viewholder

import android.view.View
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kyawhtut.pos.data.vo.TableCellVO
import kotlinx.android.synthetic.main.tableview_cell_color_layout.view.*

class TableCellColorViewHolder(private val view: View) : AbstractViewHolder(view) {

    fun bind(data: TableCellVO) {
        with(data.data as Int) {
            if (this != 0) view.iv_color.setCardBackgroundColor(this)
        }
    }
}