package com.kyawhtut.pos.ui.table

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.TableColumnHeaderVO
import com.kyawhtut.pos.data.vo.TableRowHeaderVO

interface TableRepository : BaseRepository {

    fun getCategoryList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>>

    fun getUserList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>>

    fun getProductList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>>

    fun getCustomerList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>>
}