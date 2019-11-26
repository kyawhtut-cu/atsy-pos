package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kyawhtut.pos.data.db.entity.CustomerEntity

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg customer: CustomerEntity)

    @Query("select * from customer_table")
    fun get(): List<CustomerEntity>

    @Query("select * from customer_table")
    fun liveData(): LiveData<List<CustomerEntity>>

    @Query("select * from customer_table where customer_id = :customerId")
    fun get(customerId: Int): CustomerEntity

    @Query("delete from customer_table where customer_id = :customerId")
    fun delete(customerId: Int)

    @Query("delete from customer_table")
    fun delete()

    @Delete
    fun delete(customer: CustomerEntity)
}