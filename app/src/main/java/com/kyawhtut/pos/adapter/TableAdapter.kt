package com.kyawhtut.pos.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kyawhtut.pos.R
import com.kyawhtut.pos.adapter.viewholder.*
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.TableColumnHeaderVO
import com.kyawhtut.pos.data.vo.TableRowHeaderVO
import com.kyawhtut.pos.utils.getInflateView

class TableAdapter(
    private val ctx: Context,
    private var lastDatePosition: Int = -1,
    private var colorPosition: Int = -1,
    private var textColorPosition: Int = -1,
    private var activePosition: Int = -1,
    private val callAdd: () -> Unit = {},
    private val callbackEdit: (Int) -> Unit = {},
    private val callbackDelete: Int.(Int) -> Unit = {}
) : AbstractTableAdapter<TableColumnHeaderVO, TableRowHeaderVO, TableCellVO>(ctx) {

    companion object {
        const val DATE_TYPE = 1
        const val ACTION_TYPE = 2
        const val COLOR_TYPE = 3
        const val ACTIVE_TYPE = 4
    }

    override fun onCreateCellViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        return when (viewType) {
            DATE_TYPE -> TableCellDateViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_cell_date_layout,
                    parent,
                    false
                )
            )
            ACTION_TYPE -> TableCellActionViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_cell_action_layout,
                    parent,
                    false
                ),
                callbackEdit,
                callbackDelete
            )
            COLOR_TYPE -> TableCellColorViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_cell_color_layout,
                    parent,
                    false
                )
            )
            ACTIVE_TYPE -> TableCellStatusViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_cell_status_layout,
                    parent,
                    false
                )
            )
            else -> TableCellItemViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_cell_item_layout,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindCellViewHolder(
        holder: AbstractViewHolder,
        cellItemModel: Any?,
        columnPosition: Int,
        rowPosition: Int
    ) {
        when (holder) {
            is TableCellItemViewHolder -> {
                holder.bind(cellItemModel as TableCellVO, columnPosition)
            }
            is TableCellDateViewHolder -> {
                holder.bind(cellItemModel as TableCellVO)
            }
            is TableCellColorViewHolder -> {
                holder.bind(cellItemModel as TableCellVO)
            }
            is TableCellActionViewHolder -> {
                holder.bind(cellItemModel as TableCellVO, rowPosition)
            }
            is TableCellStatusViewHolder -> {
                holder.bind(cellItemModel as TableCellVO)
            }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): AbstractViewHolder {
        return when (viewType) {
            ACTION_TYPE -> TableColumnHeaderActionViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_column_header_action_layout,
                    parent,
                    false
                ),
                callAdd
            )
            else -> TableColumnHeaderViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_column_header_layout,
                    parent,
                    false
                ),
                tableView
            )
        }
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder?,
        columnHeaderItemModel: Any?,
        columnPosition: Int
    ) {
        when (holder) {
            is TableColumnHeaderActionViewHolder -> holder.bind()
            is TableColumnHeaderViewHolder -> holder.bind(
                columnHeaderItemModel as TableColumnHeaderVO,
                columnPosition
            )
        }
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): AbstractViewHolder {
        return TableRowHeaderViewHolder(
            ctx.getInflateView(
                R.layout.tableview_row_header_layout,
                parent,
                false
            )
        )
    }

    override fun onBindRowHeaderViewHolder(
        holder: AbstractViewHolder?,
        rowHeaderItemModel: Any?,
        rowPosition: Int
    ) {
        (holder as TableRowHeaderViewHolder).bind(rowHeaderItemModel as TableRowHeaderVO)
    }

    override fun onCreateCornerView(): View {
        return ctx.getInflateView(
            R.layout.tableview_corner_layout, null
        )
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return getColumnHeaderType(position)
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getCellItemViewType(position: Int): Int {
        return getCellViewType(position)
    }

    private fun getColumnHeaderType(column: Int): Int = when (column) {
        lastDatePosition + 1 -> ACTION_TYPE
        else -> 0
    }

    private fun getCellViewType(column: Int): Int = when (column) {
        lastDatePosition, lastDatePosition - 1 -> DATE_TYPE
        lastDatePosition + 1 -> ACTION_TYPE
        colorPosition, textColorPosition -> COLOR_TYPE
        activePosition -> ACTIVE_TYPE
        else -> 0
    }
}