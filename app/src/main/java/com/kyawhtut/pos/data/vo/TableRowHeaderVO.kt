package com.kyawhtut.pos.data.vo

data class TableRowHeaderVO private constructor(
    val data: String,
    val color: Int
) {

    class Builder {
        var data: String = ""
        var color: Int = android.R.color.transparent

        fun build() = TableRowHeaderVO(data, color)
    }
}

class TableRowHeaderVOList : ArrayList<TableRowHeaderVO>() {

    fun rowHeader(block: TableRowHeaderVO.Builder.() -> Unit) {
        add(TableRowHeaderVO.Builder().apply(block).build())
    }
}

fun rowHeaderList(block: TableRowHeaderVOList.() -> Unit) = TableRowHeaderVOList().apply(block)

fun rowHeader(block: TableRowHeaderVO.Builder.() -> Unit) =
    TableRowHeaderVO.Builder().apply(block).build()
