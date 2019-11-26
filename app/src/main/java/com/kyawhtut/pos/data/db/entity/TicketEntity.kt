package com.kyawhtut.pos.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "ticket_table")
data class TicketEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ticket_id")
    val ticketId: String,
    @ColumnInfo(name = "customer_id")
    @ForeignKey(
        entity = CustomerEntity::class,
        parentColumns = ["customer_id"],
        childColumns = ["customer_id"]
    )
    val customerId: Int,
    @ColumnInfo(name = "total_price")
    val totalPrice: Long,
    @ColumnInfo(name = "pay_amount")
    val payAmount: Long,
    @ColumnInfo(name = "created_user_id")
    val createdUserId: Int,
    @ColumnInfo(name = "updated_user_id")
    val updatedUserId: String,
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: String
)