package com.kyawhtut.lib.pagination

import androidx.recyclerview.widget.DiffUtil

data class PaginationItem private constructor(
    val pageNumber: Int,
    val isCurrentPage: Boolean,
    val paginationType: PaginationType
) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<PaginationItem>() {
            override fun areItemsTheSame(
                oldItem: PaginationItem,
                newItem: PaginationItem
            ): Boolean {
                return oldItem.pageNumber == newItem.pageNumber
            }

            override fun areContentsTheSame(
                oldItem: PaginationItem,
                newItem: PaginationItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class Builder {
        var pageNumber = 0
        var isCurrentPage: Boolean = false
        var paginationType: PaginationType = PaginationType.NUMBER

        fun build() = PaginationItem(pageNumber, isCurrentPage, paginationType)
    }
}

fun pagination(block: PaginationItem.Builder.() -> Unit) =
    PaginationItem.Builder().apply(block).build()

sealed class PaginationType {
    object NUMBER : PaginationType()
    object DOT : PaginationType()
}