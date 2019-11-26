package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kyawhtut.pos.data.db.entity.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg product: ProductEntity)

    @Query("select * from product_table")
    fun get(): List<ProductEntity>

    @Query("select * from product_table")
    fun liveData(): LiveData<List<ProductEntity>>

    @Query("select * from product_table where category_id = :categoryId")
    fun liveData(categoryId: Int): LiveData<List<ProductEntity>>

    @Query("select * from product_table where product_id = :productId")
    fun get(productId: Int): ProductEntity

    @Query("delete from product_table where product_id = :productId")
    fun delete(productId: Int)

    @Query("delete from product_table")
    fun delete()

    @Delete
    fun delete(product: ProductEntity)
}