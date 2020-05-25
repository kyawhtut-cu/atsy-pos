package com.kyawhtut.pos.adapter.viewholder

import android.view.View
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.utils.toBoolean
import kotlinx.android.synthetic.main.tableview_cell_status_layout.view.*

class TableCellStatusViewHolder(private val view: View) : AbstractViewHolder(view) {

    fun bind(data: TableCellVO) {
        view.iv_active.isSelected = (data.data as Int).toBoolean()
    }
}