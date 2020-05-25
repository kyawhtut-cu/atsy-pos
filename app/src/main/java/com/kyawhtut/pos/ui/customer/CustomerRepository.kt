package com.kyawhtut.pos.ui.customer

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.CustomerBuilder
import com.kyawhtut.pos.data.db.entity.CustomerEntity
import com.kyawhtut.pos.data.db.entity.CustomerTable
import com.kyawhtut.pos.data.db.entity.CustomerWithTicket

interface CustomerRepository : BaseRepository {

    val isDeleteAllow: Boolean

    fun getCustomerById(id: Int): CustomerEntity

    fun insertCustomer(block: CustomerBuilder.() -> Unit)

    fun updateCustomer(block: CustomerBuilder.() -> Unit)

    fun addPayMoney(id: Int, amount: Long)

    fun canDeleteCustomer(id: Int): Boolean

    fun getCustomerList(): LiveData<List<CustomerTable>>

    fun getCustomerDetail(cId: Int): LiveData<CustomerWithTicket>
}
