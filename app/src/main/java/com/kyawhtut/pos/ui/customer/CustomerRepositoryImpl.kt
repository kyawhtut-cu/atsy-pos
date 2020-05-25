package com.kyawhtut.pos.ui.customer

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.CustomerDao
import com.kyawhtut.pos.data.db.entity.*

class CustomerRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val customerDao: CustomerDao
) : BaseRepositoryImpl(sh, rootUser), CustomerRepository {

    override val isDeleteAllow: Boolean
        get() = when (getCurrentUser()?.roleId ?: 5) {
            -1, 1, 2, 3, 4 -> true
            else -> false
        }

    override fun getCustomerById(id: Int): CustomerEntity = customerDao.get(id)

    override fun insertCustomer(block: CustomerBuilder.() -> Unit) {
        customerDao.insert(CustomerBuilder().apply(block).build())
    }

    override fun updateCustomer(block: CustomerBuilder.() -> Unit) {
        customerDao.update(CustomerBuilder().apply(block).build())
    }

    override fun canDeleteCustomer(id: Int): Boolean =
        customerDao.canDeleteCustomer(id) == 0 && isDeleteAllow

    override fun deleteItemById(id: Int) {
        customerDao.delete(id)
    }

    override fun getCustomerList(): LiveData<List<CustomerTable>> = customerDao.getCustomerTable()
        .map { data ->

            data.map {
                if (it.createdUser == null && it.customer.createdUserId == -1) it.createdUser =
                    rootUser.displayName
                if (it.updatedUser == null && it.customer.updatedUserId == -1) it.updatedUser =
                    rootUser.displayName
            }
            data
        }.toLiveData()

    override fun getCustomerDetail(cId: Int): LiveData<CustomerWithTicket> =
        customerDao.getCustomerTableByCustomerId(cId)

    override fun addPayMoney(id: Int, amount: Long) {
        customerDao.addPayMoney(id, amount)
    }
}
