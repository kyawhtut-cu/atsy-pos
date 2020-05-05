package com.kyawhtut.pos.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.kyawhtut.pos.utils.getCurrentTimeString
import org.joda.time.DateTime
import java.util.*

@Entity(tableName = "sell_table")
data class SellEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sell_id")
    val id: Int,
    @ColumnInfo(name = "product_id")
    @ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["product_id"],
        childColumns = ["product_id"]
    )
    val productId: Int,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "product_price")
    val productPrice: Long,
    @ColumnInfo(name = "ticket_id")
    @ForeignKey(
        entity = TicketEntity::class,
        parentColumns = ["ticket_id"],
        childColumns = ["ticket_id"]
    )
    val ticketId: String,
    @ColumnInfo(name = "product_quality")
    val productQuality: Int,
    @ColumnInfo(name = "created_date")
    val createdDate: String,
    @ColumnInfo(name = "updated_date")
    val updateDate: String
)

class SellBuilder {
    var id: Int = 0
    var productId: Int = 0
    var productName: String = ""
    var productPrice: Long = 0
    var ticketId: String = ""
    var productQuality: Int = 0
    var createdDate: String = getCurrentTimeString()

    fun build() =
        SellEntity(
            id,
            productId,
            productName,
            productPrice,
            ticketId,
            productQuality,
            createdDate,
            getCurrentTimeString()
        )
}

class SellList : ArrayList<SellEntity>() {

    fun sell(block: SellBuilder.() -> Unit) {
        add(SellBuilder().apply(block).build())
    }
}

fun sell(block: SellBuilder.() -> Unit) = SellBuilder().apply(block).build()