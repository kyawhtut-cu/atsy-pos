package com.kyawhtut.pos.adapter.viewholder

import android.view.View
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.kyawhtut.fontchooserlib.component.MMTextView
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.vo.TableRowHeaderVO
import com.kyawhtut.pos.utils.getColorValue
import kotlinx.android.synthetic.main.tableview_row_header_layout.view.*

class TableRowHeaderViewHolder(private val view: View) : AbstractViewHolder(view) {

    private val tvRowData: MMTextView by lazy {
        view.tv_row_header
    }

    fun bind(data: TableRowHeaderVO) {
        tvRowData.apply {
            mText = data.data
            setBackgroundColor(context.getColorValue(data.color))
        }
    }

    override fun setSelected(selectionState: SelectionState?) {
        super.setSelected(selectionState)
        view.setBackgroundColor(
            view.context.getColorValue(
                when (selectionState) {
                    SelectionState.SELECTED -> R.color.selected_background_color
                    SelectionState.UNSELECTED -> R.color.unselected_header_background_color
                    else -> R.color.shadow_background_color
                }
            )
        )
        tvRowData.setTextColor(
            view.context.getColorValue(
                when (selectionState) {
                    SelectionState.SELECTED -> R.color.selected_text_color
                    else -> R.color.unselected_text_color
                }
            )
        )
    }
}