package com.kyawhtut.pos.ui.customer

import android.content.SharedPreferences
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.CustomerDao
import com.kyawhtut.pos.data.db.entity.CustomerBuilder
import com.kyawhtut.pos.data.db.entity.CustomerEntity
import com.kyawhtut.pos.data.db.entity.UserEntity

class CustomerRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val customerDao: CustomerDao
) : BaseRepositoryImpl(sh, rootUser), CustomerRepository {
    override fun getCustomerById(id: Int): CustomerEntity = customerDao.get(id)

    override fun insertCustomer(block: CustomerBuilder.() -> Unit) {
        customerDao.insert(CustomerBuilder().apply(block).build())
    }

    override fun updateCustomer(block: CustomerBuilder.() -> Unit) {
        customerDao.update(CustomerBuilder().apply(block).build())
    }

    override fun canDeleteCustomer(id: Int): Boolean = customerDao.canDeleteCustomer(id) == 0

    override fun deleteItemById(id: Int) {
        customerDao.delete(id)
    }
}