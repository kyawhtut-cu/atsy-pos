package com.kyawhtut.pos.data.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.adapter.viewholder.*
import com.kyawhtut.pos.data.vo.CellVO
import com.kyawhtut.pos.data.vo.ColumnHeaderVO
import com.kyawhtut.pos.data.vo.MyTableViewModel
import com.kyawhtut.pos.data.vo.RowHeaderVO
import com.kyawhtut.pos.utils.getInflateView

class UserTableAdapter(private val ctx: Context) :
    AbstractTableAdapter<ColumnHeaderVO, RowHeaderVO, CellVO>(ctx) {

    private val mytableViewModel = MyTableViewModel()

    override fun onCreateCellViewHolder(parent: ViewGroup?, viewType: Int): AbstractViewHolder {
        return when (viewType) {
            MyTableViewModel.GENDER_TYPE -> GenderCellViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_gender_cell_layout,
                    parent,
                    false
                )
            )
            MyTableViewModel.MONEY_TYPE -> MoneyCellViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_money_cell_layout,
                    parent,
                    false
                )
            )
            else -> CellViewHolder(
                ctx.getInflateView(
                    R.layout.tableview_cell_layout,
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
            is CellViewHolder -> {
                holder.setCellModel(cellItemModel as CellVO, columnPosition)
            }
            is GenderCellViewHolder -> {
                holder.setCellModel(cellItemModel as CellVO)
            }
            is MoneyCellViewHolder -> {
                holder.setCellModel(cellItemModel as CellVO)
            }
        }
    }

    override fun onCreateColumnHeaderViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): AbstractViewHolder {
        return ColumnHeaderViewHolder(
            ctx.getInflateView(
                R.layout.tableview_column_header_layout,
                parent,
                false
            ),
            tableView
        )
    }

    override fun onBindColumnHeaderViewHolder(
        holder: AbstractViewHolder?,
        columnHeaderItemModel: Any?,
        columnPosition: Int
    ) {
        (holder as ColumnHeaderViewHolder).setColumnHeaderModel(
            columnHeaderItemModel as ColumnHeaderVO,
            columnPosition
        )
    }

    override fun onCreateRowHeaderViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): AbstractViewHolder {
        return RowHeaderViewHolder(
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
        (holder as RowHeaderViewHolder).row_header_textview.setText((rowHeaderItemModel as RowHeaderVO).data)
    }

    override fun onCreateCornerView(): View {
        return ctx.getInflateView(
            R.layout.tableview_corner_layout, null
        )
    }

    override fun getColumnHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getRowHeaderItemViewType(position: Int): Int {
        return 0
    }

    override fun getCellItemViewType(position: Int): Int {
        return mytableViewModel.getCellItemViewType(position)
    }

    fun setData() {
        mytableViewModel.generateListForTableView()
        setAllItems(
            mytableViewModel.getColumnHeaderList(),
            mytableViewModel.getRowHeaderList(),
            mytableViewModel.getCellList()
        )
    }
}