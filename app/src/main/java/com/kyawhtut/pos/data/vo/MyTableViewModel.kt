package com.kyawhtut.pos.data.vo

import android.view.Gravity

class MyTableViewModel {

    companion object {
        const val GENDER_TYPE = 1
        const val MONEY_TYPE = 2
    }

    private val _columnHeaderList = mutableListOf<ColumnHeaderVO>()
    private val _rowHeaderList = mutableListOf<RowHeaderVO>()
    private val _cellList = mutableListOf<MutableList<CellVO>>()

    fun getColumnHeaderList() = _columnHeaderList
    fun getRowHeaderList() = _rowHeaderList
    fun getCellList() = _cellList

    fun getCellItemViewType(column: Int): Int = when (column) {
        5 -> GENDER_TYPE
        8 -> MONEY_TYPE
        else -> 0
    }

    fun getColumnTextAlign(column: Int): Int = when (column) {
        1, 2, 3, 7, 11 -> Gravity.START
        12, 13, 14 -> Gravity.END
        else -> Gravity.CENTER
    }

    private fun createColumnHeaderList() {
        _columnHeaderList.clear()
        _columnHeaderList.add(ColumnHeaderVO("Id"))
        _columnHeaderList.add(ColumnHeaderVO("Name"))
        _columnHeaderList.add(ColumnHeaderVO("Nickname"))
        _columnHeaderList.add(ColumnHeaderVO("Email"))
        _columnHeaderList.add(ColumnHeaderVO("Birthday"))
        _columnHeaderList.add(ColumnHeaderVO("Sex"))
        _columnHeaderList.add(ColumnHeaderVO("Age"))
        _columnHeaderList.add(ColumnHeaderVO("Job"))
        _columnHeaderList.add(ColumnHeaderVO("Salary"))
        _columnHeaderList.add(ColumnHeaderVO("CreatedAt"))
        _columnHeaderList.add(ColumnHeaderVO("UpdatedAt"))
        _columnHeaderList.add(ColumnHeaderVO("Address"))
        _columnHeaderList.add(ColumnHeaderVO("Zip Code"))
        _columnHeaderList.add(ColumnHeaderVO("Phone"))
        _columnHeaderList.add(ColumnHeaderVO("Fax"))
    }

    private fun createCellList() {
        _cellList.clear()
        (0 until 50).forEach {
            val cell = mutableListOf<CellVO>()
            cell.add(CellVO("${it + 1}", "Id - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Name - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Nickname - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Email - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Birthday - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Sex - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Age - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Job - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Salary - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "CreatedAt - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "UpdatedAt - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Address - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Zip Code - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Phone - ${it + 1}"))
            cell.add(CellVO("${it + 1}", "Fax - ${it + 1}"))
            _cellList.add(cell)
        }
    }

    private fun createRowHeaderList() {
        _rowHeaderList.clear()
        (0 until 50).forEach {
            _rowHeaderList.add(RowHeaderVO("${it + 1}"))
        }
    }

    fun generateListForTableView() {
        createColumnHeaderList()
        createRowHeaderList()
        createCellList()
    }
}