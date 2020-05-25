package com.kyawhtut.pos.base

import com.kyawhtut.pos.data.vo.TableColumnHeaderVO
import com.kyawhtut.pos.data.vo.columnHeaderList
import java.util.*

abstract class BaseColumn(
    private vararg val columnList: String
) {

    var datePosition: Int = -1
    var colorPosition: Int = -1
    var textColorPosition: Int = -1
    var activePosition: Int = -1

    init {
        columnList.forEachIndexed { index, s ->
            if (s.toLowerCase(Locale.getDefault()) == "updated date") datePosition = index
            if (s.toLowerCase(Locale.getDefault()) == "color") colorPosition = index
            if (s.toLowerCase(Locale.getDefault()) == "text color") textColorPosition = index
            if (s.toLowerCase(Locale.getDefault()) == "status") activePosition = index
        }
    }

    fun getColumnHeaderList(): List<TableColumnHeaderVO> {
        columnHeaderList {
            columnList.forEach {
                columnHeader {
                    data = it
                }
            }
        }.run {
            return this
        }
    }
}