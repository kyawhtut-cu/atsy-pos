package com.kyawhtut.pos.data.vo

import com.evrencoskun.tableview.sort.ISortableModel

data class CellVO(
    val cellId: String,
    val data: Any
) : ISortableModel {
    override fun getContent(): Any {
        return data
    }

    override fun getId(): String {
        return cellId
    }
}