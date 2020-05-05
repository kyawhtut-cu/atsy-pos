package com.kyawhtut.pos.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kyawhtut.pos.utils.getCurrentTimeString
import org.joda.time.DateTime
import java.util.*

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
    @ColumnInfo(name = "discount_amount")
    val discountAmount: Long,
    @ColumnInfo(name = "created_user_id")
    @ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["created_user_id"]
    )
    val createdUserId: Int,
    @ColumnInfo(name = "updated_user_id")
    @ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["updated_user_id"]
    )
    val updatedUserId: Int,
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: String
)

class TicketBuilder {
    var ticketId: String = ""
    var customerId: Int = 0
    var totalPrice: Long = 0
    var payAmount: Long = 0
    var discountAmount: Long = 0L
    var createdUserId: Int = 0
    var updatedUserId: Int = 0
    var createdDate: String = getCurrentTimeString()

    fun build() = TicketEntity(
        ticketId,
        customerId,
        totalPrice,
        payAmount,
        discountAmount,
        createdUserId,
        updatedUserId,
        createdDate,
        getCurrentTimeString()
    )
}

data class TicketWithProductList(
    @Embedded
    @SerializedName("header")
    val ticketEntity: TicketEntity,
    @Relation(entity = SellEntity::class, parentColumn = "ticket_id", entityColumn = "ticket_id")
    @SerializedName("sellEntities")
    val list: List<SellEntity>
)

fun ticket(block: TicketBuilder.() -> Unit) = TicketBuilder().apply(block).build()