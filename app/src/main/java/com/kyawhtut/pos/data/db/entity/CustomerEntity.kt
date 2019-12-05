package com.kyawhtut.pos.data.db.entity

import androidx.room.*
import com.kyawhtut.pos.base.BaseColumn
import com.kyawhtut.pos.data.vo.TableCellVO
import com.kyawhtut.pos.data.vo.rowHeader
import com.kyawhtut.pos.data.vo.tableCellList
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
                data = table.ticketCount.size
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
        entity = TicketEntity::class,
        projection = ["ticket_id"]
    )
    val ticketCount: List<String>
)

fun customer(block: CustomerBuilder.() -> Unit) = CustomerBuilder().apply(block).build()