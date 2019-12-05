package com.kyawhtut.pos.utils.listeners

import androidx.recyclerview.widget.RecyclerView
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.listener.ITableViewListener
import com.kyawhtut.pos.adapter.viewholder.TableColumnHeaderViewHolder

class TableViewClickListener(
    private val table: ITableView,
    private val callback: (Int) -> Unit = {}
) :
    ITableViewListener {
    override fun onCellLongPressed(p0: RecyclerView.ViewHolder, p1: Int, p2: Int) {
    }

    override fun onColumnHeaderLongPressed(columnHeaderView: RecyclerView.ViewHolder, p1: Int) {
        if (columnHeaderView is TableColumnHeaderViewHolder) {
            val popup = ColumnHeaderLongPressPopup(
                columnHeaderView as TableColumnHeaderViewHolder?, table
            )
            popup.show()
        }
    }

    override fun onRowHeaderClicked(p0: RecyclerView.ViewHolder, p1: Int) {
    }

    override fun onColumnHeaderClicked(columnHeaderView: RecyclerView.ViewHolder, p1: Int) {
    }

    override fun onCellClicked(p0: RecyclerView.ViewHolder, col: Int, row: Int) {
        callback(col)
    }

    override fun onRowHeaderLongPressed(p0: RecyclerView.ViewHolder, p1: Int) {
    }
}