package com.kyawhtut.pos.ui.customer

import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.CustomerBuilder
import com.kyawhtut.pos.data.db.entity.CustomerEntity

interface CustomerRepository : BaseRepository {

    fun getCustomerById(id: Int): CustomerEntity

    fun insertCustomer(block: CustomerBuilder.() -> Unit)

    fun updateCustomer(block: CustomerBuilder.() -> Unit)

    fun canDeleteCustomer(id: Int): Boolean
}