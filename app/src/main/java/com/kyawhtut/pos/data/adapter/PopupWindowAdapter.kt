package com.kyawhtut.pos.data.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kyawhtut.pos.R
import com.kyawhtut.pos.utils.PopupItem
import com.kyawhtut.pos.utils.getInflateView
import com.kyawhtut.pos.utils.invisible
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.item_popup_menu.view.*

class PopupWindowAdapter(private val data: MutableList<PopupItem>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        (convertView ?: parent.context.getInflateView(R.layout.item_popup_menu, parent)).run {
            bindData(position, this)
            return this
        }
    }

    private fun bindData(position: Int, view: View) {
        val d = data[position]
        if (d.icon == null) view.menu_icon.invisible()
        else view.menu_icon.apply {
            visible()
            setImageResource(d.icon)
        }
        view.tv_menu_title.text = d.title

    }

    override fun getItem(position: Int): PopupItem {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}