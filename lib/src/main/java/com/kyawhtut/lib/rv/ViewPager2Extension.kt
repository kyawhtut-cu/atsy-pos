package com.kyawhtut.lib.rv

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by kyawhtut on 2019-08-13
 */


fun <T> ViewPager2.bind(items: List<T>): FastAdapter<T> {
    return FastAdapter(items.toMutableList(), vpList = this)
}

fun <T> ViewPager2.bind(items: List<T>, diffUtil: DiffUtil.ItemCallback<T>): FastListAdapter<T> {
    return FastListAdapter(items.toMutableList(), diffUtil, vpList = this)
}

fun <T> ViewPager2.bind(items: List<T>, @LayoutRes singleLayout: Int = 0, singleBind: (View.(item: T, pos: Int) -> Unit)): FastAdapter<T> {
    return FastAdapter(items.toMutableList(), vpList = this
    ).map(singleLayout, { item: T, idx: Int -> true }, singleBind)
}

fun <T> ViewPager2.bind(items: List<T>, diffUtil: DiffUtil.ItemCallback<T>, @LayoutRes singleLayout: Int = 0, singleBind: (View.(item: T, pos: Int) -> Unit)): FastListAdapter<T> {
    return FastListAdapter(items.toMutableList(), diffUtil, vpList = this
    ).map(singleLayout, { item: T, idx: Int -> true }, singleBind)
}

/**
 * ViewPager2 update
 * Updates the list using DiffUtils.
 * @param newItems the new list which is to replace the old one.
 *
 * NOTICE: The comparator currently checks if items are literally the same. You can change that if you want,
 * by changing the lambda in the function
 */
fun <T> ViewPager2.update(newItems: MutableList<T>) {
    (adapter as? FastAdapter<T>)?.update(newItems)
}