package com.kyawhtut.pos.base

import android.text.Spannable
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.ui.table.TableType

interface BaseRepository {

    var taxAmount: Int

    var limitAmount: Int

    val printHeader: Spannable

    val printFooter: Spannable

    fun deleteItemById(id: Int) {}

    fun deleteItemById(id: String) {}

    fun deleteItemById(id: Int, tableType: TableType) {}

    fun deleteItemById(id: String, tableType: TableType) {}

    fun getCurrentUser(): UserEntity?

    fun isLogin(): Boolean

    fun logout()
}