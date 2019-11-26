package com.kyawhtut.pos.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "customer_table")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_id")
    val id: Int,
    @ColumnInfo(name = "customer_name")
    val customerName: String,
    @ColumnInfo(name = "customer_address")
    val customerAddress: String,
    @ColumnInfo(name = "customer_phone")
    val customerPhone: String,
    @ColumnInfo(name = "customer_debit")
    val customerDebit: Long,
    @ColumnInfo(name = "customer_available")
    val customerAvailable: Int,
    @ColumnInfo(name = "created_user_id")
    val createdUserId: Int,
    @ColumnInfo(name = "updated_user_id")
    val updatedUserId: Int,
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: String
)

class CustomerBuilder {
    var id: Int = 0
    var customerName: String = ""
    var customerAddress: String = ""
    var customerPhone: String = ""
    var customerDebit: Long = 0
    var customerAvailable: Int = 0
    var createdUserId: Int = 0
    var updatedUserId: Int = 0
    var createdDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)
    var updatedDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)

    fun build() = CustomerEntity(
        id,
        customerName,
        customerAddress,
        customerPhone,
        customerDebit,
        customerAvailable,
        createdUserId,
        updatedUserId,
        createdDate,
        updatedDate
    )
}

fun customer(block: CustomerBuilder.() -> Unit) = CustomerBuilder().apply(block).build()