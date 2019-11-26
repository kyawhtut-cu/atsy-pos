package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kyawhtut.pos.data.db.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: UserEntity)

    @Query("select * from user_table")
    fun get(): List<UserEntity>

    @Query("select * from user_table")
    fun liveData(): LiveData<List<UserEntity>>

    @Query("select * from user_table where user_id = :userId limit 1")
    fun get(userId: Int): UserEntity

    @Query("select * from user_table where user_name = :userName and user_password = :password")
    fun login(userName: String, password: String): UserEntity?

    @Query("delete from user_table")
    fun delete()

    @Query("delete from user_table where user_id = :userId")
    fun delete(userId: Int)

    @Query("delete from user_table where user_name = :userName")
    fun delete(userName: String)

    @Delete
    fun delete(user: UserEntity)
}