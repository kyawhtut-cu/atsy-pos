package com.kyawhtut.pos.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kyawhtut.pos.data.vo.TableCellVO
import kotlinx.android.synthetic.main.tableview_cell_date_layout.view.*

class TableCellDateViewHolder(private val view: View) : AbstractViewHolder(view) {

    fun bind(data: TableCellVO) {
        view.tv_date.text = data.data as String
        view.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        view.tv_date.requestLayout()
    }
}