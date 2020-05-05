package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.CategoryEntity
import com.kyawhtut.pos.data.db.entity.CategoryTable
import com.kyawhtut.pos.data.db.entity.CategoryWithProduct
import io.reactivex.Flowable

@Dao
abstract class CategoryDao : BaseDao<CategoryEntity> {

    @Query("select * from category_table order by category_sell_count desc")
    abstract fun get(): List<CategoryEntity>

    @Query("select * from category_table where category_id = :categoryId limit 1")
    abstract fun get(categoryId: Int): CategoryEntity?

    @Query("select * from category_table")
    abstract fun liveData(): LiveData<List<CategoryEntity>>

    @Transaction
    @Query("select * from category_table where category_available = 1")
    abstract fun flowable(): Flowable<List<CategoryWithProduct>>

    @Transaction
    @Query("select * from category_table")
    abstract fun getCategoryTable(): Flowable<List<CategoryTable>>

    @Query("select count(*) from product_table where category_id = :categoryId")
    abstract fun canDeleteCategoryById(categoryId: Int): Int

    @Query("delete from category_table where category_id = :categoryId")
    abstract fun delete(categoryId: Int)

    @Query("delete from category_table")
    abstract fun delete()
}