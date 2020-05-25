package com.kyawhtut.pos.ui.table

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.AppDatabase
import com.kyawhtut.pos.data.db.entity.*
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.TableColumnHeaderVO
import com.kyawhtut.pos.data.vo.TableRowHeaderVO
import com.kyawhtut.pos.utils.toBoolean
import timber.log.Timber

class TableRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val db: AppDatabase
) : BaseRepositoryImpl(sh, rootUser), TableRepository {

    override fun getCategoryList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>> =
        db.categoryDao().getCategoryTable()
            .map { data ->
                data.map {
                    if (it.createdUser == null && it.categoryEntity.createdUserId == -1) it.createdUser =
                        rootUser.displayName
                    if (it.updatedUser == null && it.categoryEntity.updatedUserId == -1) it.updatedUser =
                        rootUser.displayName
                }
                Triple(
                    CategoryColumn().getColumnHeaderList(),
                    CategoryColumn.getRowHeaderList(data),
                    CategoryColumn.getTableCellList(data)
                )
            }.toLiveData()

    override fun getUserList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>> =
        db.userDao().getUserTable()
            .map { data ->
                data.map {
                    if (it.createUserName == null && it.user.createdUserId == -1) it.createUserName =
                        rootUser.displayName
                    if (it.updatedUserName == null && it.user.updatedUserId == -1) it.updatedUserName =
                        rootUser.displayName
                }
                Triple(
                    UserColumn().getColumnHeaderList(),
                    UserColumn.getRowHeaderList(data),
                    UserColumn.getTableCellList(data)
                )
            }.toLiveData()

    override fun getProductList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>> =
        db.productDao().getProductTable()
            .map { data ->
                data.map {
                    if (it.createdUser == null && it.product.createdUserId == -1) it.createdUser =
                        rootUser.displayName
                    if (it.updatedUser == null && it.product.updatedUserId == -1) it.updatedUser =
                        rootUser.displayName
                }
                Triple(
                    ProductColumn().getColumnHeaderList(),
                    ProductColumn.getRowHeaderList(data, data.map {
                        it.product.productRemainAmountShow.toBoolean() && it.product.productCount <= limitAmount
                    }),
                    ProductColumn.getTableCellList(data)
                )
            }.toLiveData()

    override fun getCustomerList(): LiveData<Triple<List<TableColumnHeaderVO>, List<TableRowHeaderVO>, List<List<TableCellVO>>>> =
        db.customerDao().getCustomerTable()
            .map { data ->
                data.map {
                    if (it.createdUser == null && it.customer.createdUserId == -1) it.createdUser =
                        rootUser.displayName
                    if (it.updatedUser == null && it.customer.updatedUserId == -1) it.updatedUser =
                        rootUser.displayName
                }
                Triple(
                    CustomerColumn().getColumnHeaderList(),
                    CustomerColumn.getRowHeaderList(data),
                    CustomerColumn.getTableCellList(data)
                )
            }.toLiveData()

    override fun deleteItemById(id: Int, tableType: TableType) {
        db.categoryDao().delete(
            SimpleSQLiteQuery(
                "delete from ${tableType.tableName} where ${tableType.primaryKey} = ?".also {
                    Timber.d("Query -> %s", it)
                },
                arrayOf(id)
            )
        )
    }

    override fun deleteItemById(id: String, tableType: TableType) {
    }
}