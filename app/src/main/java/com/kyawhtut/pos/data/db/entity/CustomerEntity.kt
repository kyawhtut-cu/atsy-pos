package com.kyawhtut.pos.data.db.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.kyawhtut.pos.base.BaseColumn
import com.kyawhtut.pos.data.vo.*
import com.kyawhtut.pos.utils.getCurrentTimeString
import com.kyawhtut.pos.utils.toThousandSeparator

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
    var createdDate: String = getCurrentTimeString()

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
        getCurrentTimeString()
    )
}

class CustomerColumn : BaseColumn(
    "Customer Name",
    "Customer Address",
    "Customer Phone",
    "Customer Debit",
    "Status",
    "Created User",
    "Updated User",
    "Created Date",
    "Updated Date",
    "Action"
) {
    companion object {
        fun getRowHeaderList(list: List<CustomerTable>) = list.map {
            rowHeader {
                data = "${it.customer.id}"
            }
        }

        private fun getTableCellList(table: CustomerTable): List<TableCellVO> = tableCellList {
            tableCell {
                cellId = "customer_name"
                data = table.customer.customerName
            }
            tableCell {
                cellId = "customer_address"
                data = table.customer.customerAddress
            }
            tableCell {
                cellId = "customer_phone"
                data = table.customer.customerPhone
            }
            tableCell {
                cellId = "customer_debit"
                data = "${table.customer.customerDebit}"
            }
            tableCell {
                cellId = "customer_status"
                data = table.customer.customerAvailable
            }
            tableCell {
                cellId = "created_user"
                data = table.createdUser ?: ""
            }
            tableCell {
                cellId = "updated_user"
                data = table.updatedUser ?: ""
            }
            tableCell {
                cellId = "created_date"
                data = table.customer.createdDate
            }
            tableCell {
                cellId = "customer_updateDate"
                data = table.customer.updatedDate
            }
            tableCell {
                cellId = "${table.customer.id}"
                data = table.ticketEntities.size
            }
        }

        fun getTableCellList(list: List<CustomerTable>) = list.map {
            getTableCellList(it)
        }
    }
}

data class CustomerTable(
    @Embedded
    val customer: CustomerEntity,
    @Relation(
        parentColumn = "created_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var createdUser: String?,
    @Relation(
        parentColumn = "updated_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var updatedUser: String?,
    @Relation(
        parentColumn = "customer_id",
        entityColumn = "customer_id",
        entity = TicketEntity::class
    )
    val ticketEntities: List<TicketEntity>
) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CustomerTable>() {
            override fun areItemsTheSame(oldItem: CustomerTable, newItem: CustomerTable): Boolean {
                return oldItem.customer.id == newItem.customer.id
            }

            override fun areContentsTheSame(
                oldItem: CustomerTable,
                newItem: CustomerTable
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

data class TicketWithSell(
    @Embedded
    val ticketEntities: TicketEntity,
    @Relation(
        parentColumn = "ticket_id",
        entityColumn = "ticket_id",
        entity = SellEntity::class
    )
    val sellItemList: List<SellEntity>
)

data class CustomerWithTicket(
    @Embedded
    val customer: CustomerEntity,
    @Relation(
        parentColumn = "created_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var createdUser: String?,
    @Relation(
        parentColumn = "updated_user_id",
        entityColumn = "user_id",
        entity = UserEntity::class,
        projection = ["user_display_name"]
    )
    var updatedUser: String?,
    @Relation(
        parentColumn = "customer_id",
        entityColumn = "customer_id",
        entity = TicketEntity::class
    )
    val ticketWithSell: List<TicketWithSell>
) {
    val debt: String
        get() = (ticketWithSell.sumByDouble { it.ticketEntities.totalPrice.toDouble() } - ticketWithSell.sumByDouble { it.ticketEntities.payAmount.toDouble() } - customer.customerDebit).toThousandSeparator()
}

fun customer(block: CustomerBuilder.() -> Unit) = CustomerBuilder().apply(block).build()

fun List<SellEntity>.toPrintVOList(ticketEntities: TicketEntity) = printVOList {
    printVO {
        type = PrintType.HEADER
        data = printHeader {
            ticketID = ticketEntities.ticketId
            waiterID = ticketEntities.waiterID
            waiterName = ticketEntities.waiterName
            ticketDate = ticketEntities.createdDate
        }
    }
    this@toPrintVOList.mapIndexed { index, it ->
        printVO {
            type = PrintType.ITEMS
            data = productSell {
                productID = it.productId
                count = index + 1
                name = it.productName
                qty = it.productQuality
                price = it.productPrice
            }
        }
    }
    printVO {
        type = PrintType.TOTAL
        data = printTotal {
            totalAmount =
                this@toPrintVOList.sumByDouble { (it.productPrice * it.productQuality).toDouble() }
                    .toLong()
            paidAmount = ticketEntities.payAmount
            discountAmount = ticketEntities.discountAmount
            totalQty = this@toPrintVOList.sumByDouble { it.productQuality.toDouble() }.toInt()
            taxAmount = 100 - ((100 * totalAmount) / ticketEntities.totalPrice).toInt()
        }
    }
}
