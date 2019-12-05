package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.SellEntity

@Dao
abstract class SellDao: BaseDao<SellEntity> {

    @Query("select * from sell_table")
    abstract fun get(): List<SellEntity>

    @Query("select * from sell_table where sell_id = :sellId")
    abstract fun get(sellId: Int): SellEntity

    @Query("select * from sell_table")
    abstract fun liveData(): LiveData<List<SellEntity>>

    @Query("delete from sell_table where sell_id = :sellId")
    abstract fun delete(sellId: Int)

    @Query("delete from sell_table")
    abstract fun delete()
}