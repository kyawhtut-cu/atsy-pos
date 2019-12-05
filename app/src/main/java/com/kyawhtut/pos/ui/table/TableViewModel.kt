package com.kyawhtut.pos.ui.table

import androidx.lifecycle.MediatorLiveData
import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.TableColumnHeaderVO
import com.kyawhtut.pos.data.vo.TableRowHeaderVO

class TableViewModel(private val repo: TableRepository) : BaseViewModel(repo) {

    private val _categoryTable = repo.getCategoryList()
    private val _userTable = repo.getUserList()
    private val _productTable = repo.getProductList()
    private val _customerTable = repo.getCustomerList()
    val dataList =
        MediatorLiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>>()

    private fun removeSource() {
        dataList.removeSource(_categoryTable)
        dataList.removeSource(_userTable)
        dataList.removeSource(_productTable)
    }

    fun addSource(tableType: TableType) {
        removeSource()
        dataList.addSource(
            when (tableType) {
                is TableType.ITEMS -> _categoryTable
                is TableType.PRODUCTS -> _productTable
                is TableType.USERS -> _userTable
                is TableType.CUSTOMER -> _customerTable
            }
        ) {
            dataList.postValue(it)
        }
    }

    fun delete(id: Int, tableType: TableType) {
        repo.deleteItemById(id, tableType)
    }
}