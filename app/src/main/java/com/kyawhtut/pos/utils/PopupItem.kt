package com.kyawhtut.pos.utils

import androidx.annotation.DrawableRes

class PopupItemList : ArrayList<PopupItem>() {
    fun popupItem(block: PopupItem.Builder.() -> Unit) {
        add(PopupItem.Builder().apply(block).build())
    }
}

data class PopupItem private constructor(
    val title: String,
    @DrawableRes val icon: Int?
) {

    class Builder {
        var title: String = ""
        @DrawableRes
        var icon: Int? = null

        fun build() = PopupItem(title, icon)
    }
}

fun popupItem(block: PopupItem.Builder.() -> Unit): PopupItem =
    PopupItem.Builder().apply(block).build()