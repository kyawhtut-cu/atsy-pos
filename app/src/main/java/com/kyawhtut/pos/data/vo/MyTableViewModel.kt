package com.kyawhtut.pos.data.vo

/*
import android.view.Gravity

class MyTableViewModel {

    companion object {
        const val DATE_TYPE = 1
        const val ACTION_TYPE = 2
        const val COLUMN_HEADER_ACTION_TYPE = 1
    }

    private val _columnHeaderList = mutableListOf<TableColumnHeaderVO>()
    private val _rowHeaderList = mutableListOf<TableRowHeaderVO>()
    private val _cellList = mutableListOf<MutableList<TableCellVO>>()

    fun getColumnHeaderList() = _columnHeaderList
    fun getRowHeaderList() = _rowHeaderList
    fun getCellList() = _cellList

    fun getCellItemViewType(column: Int): Int = when (column) {
        5 -> DATE_TYPE
        15 -> ACTION_TYPE
        else -> 0
    }

    fun getColumnHeaderType(column: Int): Int = when (column) {
        15 -> COLUMN_HEADER_ACTION_TYPE
        else -> 0
    }

    fun getColumnTextAlign(column: Int): Int = when (column) {
        1, 2, 3, 7, 11 -> Gravity.START
        12, 13, 14 -> Gravity.END
        else -> Gravity.CENTER
    }

    private fun createColumnHeaderList() {
        _columnHeaderList.clear()
        _columnHeaderList.add(TableColumnHeaderVO("Id"))
        _columnHeaderList.add(TableColumnHeaderVO("Name"))
        _columnHeaderList.add(TableColumnHeaderVO("Nickname"))
        _columnHeaderList.add(TableColumnHeaderVO("Email"))
        _columnHeaderList.add(TableColumnHeaderVO("Birthday"))
        _columnHeaderList.add(TableColumnHeaderVO("Sex"))
        _columnHeaderList.add(TableColumnHeaderVO("Age"))
        _columnHeaderList.add(TableColumnHeaderVO("Job"))
        _columnHeaderList.add(TableColumnHeaderVO("Salary"))
        _columnHeaderList.add(TableColumnHeaderVO("CreatedAt"))
        _columnHeaderList.add(TableColumnHeaderVO("UpdatedAt"))
        _columnHeaderList.add(TableColumnHeaderVO("Address"))
        _columnHeaderList.add(TableColumnHeaderVO("Zip Code"))
        _columnHeaderList.add(TableColumnHeaderVO("Phone"))
        _columnHeaderList.add(TableColumnHeaderVO("Fax"))
        _columnHeaderList.add(TableColumnHeaderVO("Action"))
    }

    private fun createCellList() {
        _cellList.clear()
        (0 until 50).forEach {
            val cell = mutableListOf<TableCellVO>()
            cell.add(TableCellVO("${it + 1}", "Id - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Name - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Nickname - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Email - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Birthday - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Sex - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Age - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Job - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Salary - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "CreatedAt - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "UpdatedAt - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Address - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Zip Code - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Phone - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Fax - ${it + 1}"))
            cell.add(TableCellVO("${it + 1}", "Action"))
            _cellList.add(cell)
        }
    }

    private fun createRowHeaderList() {
        _rowHeaderList.clear()
        (0 until 50).forEach {
            _rowHeaderList.add(TableRowHeaderVO("${it + 1}"))
        }
    }

    fun generateListForTableView() {
        createColumnHeaderList()
        createRowHeaderList()
        createCellList()
    }
}*/
