package com.kyawhtut.pos.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.evrencoskun.tableview.ITableView
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder
import com.evrencoskun.tableview.sort.SortState
import com.kyawhtut.fontchooserlib.component.MMTextView
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.vo.TableColumnHeaderVO
import com.kyawhtut.pos.utils.getColorValue
import com.kyawhtut.pos.utils.invisible
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.tableview_column_header_layout.view.*

class TableColumnHeaderViewHolder(private val view: View, private val table: ITableView) :
    AbstractSorterViewHolder(view) {

    private val container: ConstraintLayout by lazy {
        view.column_header_container
    }
    private val btnSort: ImageView by lazy {
        view.column_header_sort_imageButton
    }
    private val tvCellData: MMTextView by lazy {
        view.column_header_textView
    }

    init {
        btnSort.setOnClickListener {
            table.sortColumn(
                adapterPosition,
                when (sortState) {
                    SortState.DESCENDING -> SortState.ASCENDING
                    else -> SortState.DESCENDING
                }
            )
        }
    }

    fun bind(data: TableColumnHeaderVO, columnPosition: Int) {
        tvCellData.apply {
            mText = data.data
        }

        container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        requestLayout()
    }

    private fun requestLayout() {
        tvCellData.requestLayout()
        btnSort.requestLayout()
        container.requestLayout()
        view.requestLayout()
    }

    override fun setSelected(selectionState: SelectionState?) {
        super.setSelected(selectionState)

        container.setBackgroundColor(
            view.context.getColorValue(
                when (selectionState) {
                    SelectionState.SELECTED -> R.color.selected_background_color
                    SelectionState.UNSELECTED -> R.color.unselected_background_color
                    else -> R.color.shadow_background_color
                }
            )
        )
        tvCellData.setTextColor(
            view.context.getColorValue(
                when (selectionState) {
                    SelectionState.SELECTED -> R.color.selected_text_color
                    else -> R.color.unselected_text_color
                }
            )
        )
    }

    override fun onSortingStatusChanged(pSortState: SortState) {
        super.onSortingStatusChanged(pSortState)
        container.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT

        controlSortState(pSortState)

        requestLayout()
    }

    private fun controlSortState(stortState: SortState) {
        btnSort.apply {
            visible()
            when (stortState) {
                SortState.ASCENDING -> {
                    setImageResource(R.drawable.ic_keyboard_arrow_down_black)
                }
                SortState.DESCENDING -> {
                    setImageResource(R.drawable.ic_keyboard_arrow_up_white)
                }
                else -> invisible()
            }
        }
    }
}