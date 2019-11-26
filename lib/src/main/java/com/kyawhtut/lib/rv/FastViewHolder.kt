package com.kyawhtut.lib.rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class FastViewHolder<T>(
        override val containerView: View,
        val holderType: Int
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(entry: T, func: View.(item: T, pos: Int) -> Unit) {
        containerView.apply {
            func(entry, adapterPosition)
        }
    }
}