package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kyawhtut.pos.data.db.entity.RoleEntity

@Dao
interface RoleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg role: RoleEntity)

    @Query("select count(*) from role_table")
    fun count(): Int

    @Query("select * from role_table")
    fun get(): List<RoleEntity>

    @Query("select * from role_table")
    fun liveData(): LiveData<List<RoleEntity>>

    @Query("select * from role_table where role_id = :roleId limit 1")
    fun get(roleId: Int): RoleEntity

    @Query("delete from role_table")
    fun delete()

    @Query("delete from role_table where role_id = :roleId")
    fun delete(roleId: Int)

    @Delete
    fun delete(role: RoleEntity)
}