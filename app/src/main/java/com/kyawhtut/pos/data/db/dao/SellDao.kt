package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kyawhtut.pos.data.db.entity.SellEntity

@Dao
interface SellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg sell: SellEntity)

    @Query("select * from sell_table")
    fun get(): List<SellEntity>

    @Query("select * from sell_table where sell_id = :sellId")
    fun get(sellId: Int): SellEntity

    @Query("select * from sell_table")
    fun liveData(): LiveData<List<SellEntity>>

    @Query("delete from sell_table where sell_id = :sellId")
    fun delete(sellId: Int)

    @Query("delete from sell_table")
    fun delete()

    @Delete
    fun delete(sell: SellEntity)
}