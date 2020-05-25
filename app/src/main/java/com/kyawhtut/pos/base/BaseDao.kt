package com.kyawhtut.pos.base

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T)

    @RawQuery
    fun get(query: SupportSQLiteQuery): List<T>

    @RawQuery
    fun count(query: SimpleSQLiteQuery): Int

    @RawQuery
    fun delete(query: SimpleSQLiteQuery): T

    @Update
    fun update(d: T)

    @Delete
    fun delete(d: T)
}