package com.kyawhtut.pos.data.vo

data class TableColumnHeaderVO private constructor(
    val data: String
) {

    class Builder {
        var data: String = ""

        fun build() = TableColumnHeaderVO(data)
    }
}

class TableColumnHeaderVOList : ArrayList<TableColumnHeaderVO>() {

    fun columnHeader(block: TableColumnHeaderVO.Builder.() -> Unit) {
        add(TableColumnHeaderVO.Builder().apply(block).build())
    }
}

fun columnHeaderList(block: TableColumnHeaderVOList.() -> Unit) =
    TableColumnHeaderVOList().apply(block)

fun columnHeader(block: TableColumnHeaderVO.Builder.() -> Unit) =
    TableColumnHeaderVO.Builder().apply(block).build()