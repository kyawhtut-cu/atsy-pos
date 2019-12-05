package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.db.entity.UserTable
import io.reactivex.Flowable

@Dao
abstract class UserDao : BaseDao<UserEntity> {

    @Query("select * from user_table")
    abstract fun get(): List<UserEntity>

    @Query("select * from user_table")
    abstract fun liveData(): LiveData<List<UserEntity>>

    @Query("select * from user_table where user_id = :userId limit 1")
    abstract fun get(userId: Int): UserEntity

    @Query("select * from user_table where user_name = :userName and user_password = :password")
    abstract fun login(userName: String, password: String): UserEntity?

    @Transaction
    @Query("select * from user_table")
    abstract fun getUserTable(): Flowable<List<UserTable>>

    @Query("delete from user_table")
    abstract fun delete()

    @Query("delete from user_table where user_id = :userId")
    abstract fun delete(userId: Int)

    @Query("delete from user_table where user_name = :userName")
    abstract fun delete(userName: String)
}