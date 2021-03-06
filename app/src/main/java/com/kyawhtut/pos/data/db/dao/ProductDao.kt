package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.ProductEntity
import com.kyawhtut.pos.data.db.entity.ProductTable
import com.kyawhtut.pos.data.db.entity.SellEntity
import io.reactivex.Flowable

@Dao
abstract class ProductDao : BaseDao<ProductEntity> {

    @Query("select count(*) from product_table")
    abstract fun count(): Int

    @Query("select * from product_table")
    abstract fun get(): List<ProductEntity>

    @Query("select * from product_table")
    abstract fun liveData(): LiveData<List<ProductEntity>>

    @Query("select * from product_table where category_id = :categoryId and product_available = 1")
    abstract fun liveData(categoryId: Int): LiveData<List<ProductEntity>>

    @Query("select * from product_table where product_available = 1")
    abstract fun flowable(): Flowable<List<ProductEntity>>

    @Transaction
    @Query("select * from product_table")
    abstract fun getProductTable(): Flowable<List<ProductTable>>

    @Query("select * from product_table where product_id = :productId")
    abstract fun get(productId: Int): ProductEntity?

    @Query("select product_id from product_table where product_code = :productCode")
    abstract fun getProductIdByProductCode(productCode: String): Int

    @Query("update product_table set product_count = product_count - :productCount, product_sell_count = product_sell_count + :productCount where product_id = :productId")
    abstract fun updateQuality(productCount: Int, productId: Int)

    fun updateQuality(list: List<SellEntity>) {
        list.forEach {
            updateQuality(it.productQuality, it.productId)
        }
    }

    @Query("select count(*) from sell_table where product_id = :productId")
    abstract fun canDeleteProductById(productId: Int): Int

    @Query("select * from product_table where show_alert_remain_amount = 1 and product_count <= :limitAmount")
    abstract fun getLowerItemList(limitAmount: Int): Flowable<List<ProductEntity>>

    @Query("select count(*) from product_table where show_alert_remain_amount = 1 and product_count <= :limitAmount")
    abstract fun getLowerItemCount(limitAmount: Int): Int

    @Query("delete from product_table where product_id = :productId")
    abstract fun delete(productId: Int)

    @Query("delete from product_table")
    abstract fun delete()
}