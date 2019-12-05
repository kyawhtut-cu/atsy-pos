package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.CustomerEntity
import com.kyawhtut.pos.data.db.entity.CustomerTable
import io.reactivex.Flowable

@Dao
abstract class CustomerDao : BaseDao<CustomerEntity> {

    @Query("select * from customer_table")
    abstract fun get(): List<CustomerEntity>

    @Query("select * from customer_table")
    abstract fun liveData(): LiveData<List<CustomerEntity>>

    @Query("select * from customer_table where customer_id = :customerId")
    abstract fun get(customerId: Int): CustomerEntity

    @Transaction
    @Query("select * from customer_table")
    abstract fun getCustomerTable(): Flowable<List<CustomerTable>>

    @Query("select count(*) from ticket_table where customer_id = :customerId")
    abstract fun canDeleteCustomer(customerId: Int): Int

    @Query("delete from customer_table where customer_id = :customerId")
    abstract fun delete(customerId: Int)

    @Query("delete from customer_table")
    abstract fun delete()
}