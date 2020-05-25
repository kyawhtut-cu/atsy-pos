package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.RoleEntity
import io.reactivex.Flowable

@Dao
abstract class RoleDao : BaseDao<RoleEntity> {

    @Query("select count(*) from role_table")
    abstract fun count(): Int

    @Query("select * from role_table")
    abstract fun get(): List<RoleEntity>

    @Query("select * from role_table")
    abstract fun liveData(): LiveData<List<RoleEntity>>

    @Query("select * from role_table")
    abstract fun flowable(): Flowable<List<RoleEntity>>

    @Query("select * from role_table where role_id = :roleId limit 1")
    abstract fun get(roleId: Int): RoleEntity

    @Query("delete from role_table")
    abstract fun delete()

    @Query("delete from role_table where role_id = :roleId")
    abstract fun delete(roleId: Int)
}