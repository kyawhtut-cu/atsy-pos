package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kyawhtut.pos.data.db.entity.CategoryEntity
import com.kyawhtut.pos.data.db.entity.CategoryWithProduct
import io.reactivex.Flowable

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg category: CategoryEntity)

    @Query("select * from category_table order by category_sell_count desc")
    fun get(): List<CategoryEntity>

    @Query("select * from category_table where category_id = :categoryId limit 1")
    fun get(categoryId: Int): CategoryEntity

    @Query("select * from category_table")
    fun liveData(): LiveData<List<CategoryEntity>>

    @Transaction
    @Query("select * from category_table")
    fun flowable(): Flowable<List<CategoryWithProduct>>

    @Query("delete from category_table where category_id = :categoryId")
    fun delete(categoryId: Int)

    @Query("delete from category_table")
    fun delete()

    @Delete
    fun delete(category: CategoryEntity)
}