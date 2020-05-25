package com.kyawhtut.pos.adapter.viewholder

import android.view.View
import android.widget.LinearLayout
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.utils.toBoolean
import kotlinx.android.synthetic.main.tableview_cell_action_layout.view.*

class TableCellActionViewHolder(
    private val view: View,
    private val callbackEdit: (Int) -> Unit = {},
    private val callbackDelete: Int.(Int) -> Unit = {}
) : AbstractViewHolder(view) {

    private var id: Int = 0
    private var row: Int = 0

    init {
        view.btn_edit.setOnClickListener {
            callbackEdit(id)
        }

        view.btn_delete.setOnClickListener {
            row.callbackDelete(id)
        }
    }

    fun bind(data: TableCellVO, row: Int = 0) {
        this.row = row
        id = data.cellId.toInt()
        view.btn_delete.isEnabled = !(data.data as Int).toBoolean()
        view.root_layout.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
    }
}