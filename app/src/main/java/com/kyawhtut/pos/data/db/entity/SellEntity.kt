package com.kyawhtut.pos.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
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
    @ColumnInfo(name = "ticket_id")
    val ticketId: Int,
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
    var ticketId: Int = 0
    var productQuality: Int = 0
    var createdDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)
    val updateDate: String = DateTime.now().toString("dd-MM-yyyy", Locale.ENGLISH)

    fun build() = SellEntity(id, productId, ticketId, productQuality, createdDate, updateDate)
}

fun sell(block: SellBuilder.() -> Unit) = SellBuilder().apply(block).build()