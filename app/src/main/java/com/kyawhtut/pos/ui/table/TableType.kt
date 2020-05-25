package com.kyawhtut.pos.ui.table

import java.io.Serializable

sealed class TableType(val tableName: String, val primaryKey: String) : Serializable {
    object ITEMS : TableType("category_table", "category_id")
    object PRODUCTS : TableType("product_table", "product_id")
    object USERS : TableType("user_table", "user_id")
    object CUSTOMER : TableType("customer_table", "customer_id")
    object DEFAULT : TableType("", "")
}
