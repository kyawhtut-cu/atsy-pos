package com.kyawhtut.pos.data.vo

data class TableRowHeaderVO private constructor(
    val data: String
) {

    class Builder {
        var data: String = ""

        fun build() = TableRowHeaderVO(data)
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