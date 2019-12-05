package com.kyawhtut.pos.utils

import android.view.Gravity
import com.kyawhtut.pos.data.db.entity.CategoryColumn
import com.kyawhtut.pos.data.db.entity.CustomerColumn
import com.kyawhtut.pos.data.db.entity.ProductColumn
import com.kyawhtut.pos.data.db.entity.UserColumn
import com.kyawhtut.pos.ui.table.TableType

object TableUtil

fun getDatePosition(type: TableType) = when (type) {
    is TableType.ITEMS -> CategoryColumn().datePosition
    is TableType.USERS -> UserColumn().datePosition
    is TableType.PRODUCTS -> ProductColumn().datePosition
    is TableType.CUSTOMER -> CustomerColumn().datePosition
}

fun getColorPosition(type: TableType) = when (type) {
    is TableType.ITEMS -> CategoryColumn().colorPosition
    is TableType.USERS -> UserColumn().colorPosition
    is TableType.PRODUCTS -> ProductColumn().colorPosition
    is TableType.CUSTOMER -> CustomerColumn().colorPosition
}

fun getTextColorPosition(type: TableType) = when (type) {
    is TableType.ITEMS -> CategoryColumn().textColorPosition
    is TableType.USERS -> UserColumn().textColorPosition
    is TableType.PRODUCTS -> ProductColumn().textColorPosition
    is TableType.CUSTOMER -> CustomerColumn().textColorPosition
}

fun getActivePosition(type: TableType) = when (type) {
    is TableType.ITEMS -> CategoryColumn().activePosition
    is TableType.USERS -> UserColumn().activePosition
    is TableType.PRODUCTS -> ProductColumn().activePosition
    is TableType.CUSTOMER -> CustomerColumn().activePosition
}

private val COLUMN_TEXT_ALIGNS = intArrayOf( // Id
    Gravity.CENTER,  // Name
    Gravity.START,  // Nickname
    Gravity.START,  // Email
    Gravity.START,  // BirthDay
    Gravity.CENTER,  // Gender (Sex)
    Gravity.CENTER,  // Age
    Gravity.CENTER,  // Job
    Gravity.START,  // Salary
    Gravity.CENTER,  // CreatedAt
    Gravity.CENTER,  // UpdatedAt
    Gravity.CENTER,  // Address
    Gravity.START,  // Zip Code
    Gravity.END,  // Phone
    Gravity.END,  // Fax
    Gravity.END
)

fun getAlign(position: Int) = COLUMN_TEXT_ALIGNS[position] or Gravity.CENTER_VERTICAL
