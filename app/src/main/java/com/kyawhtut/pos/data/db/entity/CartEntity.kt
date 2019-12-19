package com.kyawhtut.pos.data.db.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.kyawhtut.pos.data.vo.*

@Entity(tableName = "cart_header_table")
data class CartHeaderEntity(
    @ColumnInfo(name = "cart_header_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "customer_name")
    val customerName: String,
    @ColumnInfo(name = "customer_phone")
    val customerPhone: String,
    @ColumnInfo(name = "ticket_id")
    val ticketId: String,
    @ColumnInfo(name = "tax")
    val tax: Int,
    @ColumnInfo(name = "sale_man_id")
    val saleManId: Int,
    @ColumnInfo(name = "sale_name")
    val saleName: String
) {
    fun toPrintHeader() = printHeader {
        customerName = this@CartHeaderEntity.customerName
        customerPhone = this@CartHeaderEntity.customerPhone
        ticketID = this@CartHeaderEntity.ticketId
        waiterID = this@CartHeaderEntity.saleManId
        waiterName = this@CartHeaderEntity.saleName
    }
}

class CartHeaderBuilder {
    var id: Int = 0
    var customerName = ""
    var customerPhone = ""
    var ticketId = ""
    var tax: Int = 0
    var saleManId: Int = 0
    var saleName = ""

    fun build() =
        CartHeaderEntity(id, customerName, customerPhone, ticketId, tax, saleManId, saleName)
}

fun cartHeader(block: CartHeaderBuilder.() -> Unit) = CartHeaderBuilder().apply(block).build()

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cart_id")
    val id: Int,
    @ColumnInfo(name = "cart_header_id")
    @ForeignKey(
        entity = CartHeaderEntity::class,
        parentColumns = ["cart_header_id"],
        childColumns = ["cart_header_id"]
    )
    val cartHeaderId: Int,
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "product_qty")
    val productQty: Int
)

class CartBuilder {
    var id: Int = 0
    var cartHeaderId = 0
    var productId = 0
    var productQty = 1

    fun build() = CartEntity(id, cartHeaderId, productId, productQty)
}

class CartList : ArrayList<CartEntity>() {

    fun cart(block: CartBuilder.() -> Unit) {
        add(CartBuilder().apply(block).build())
    }
}

fun cart(block: CartBuilder.() -> Unit) = CartBuilder().apply(block).build()

fun cartList(block: CartList.() -> Unit) = CartList().apply(block)

data class CartWithProduct(
    @Embedded
    val cartEntity: CartEntity,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_id",
        entity = ProductEntity::class
    )
    var productEntity: ProductEntity
) {
    fun toPrintVO(index: Int) = productSell {
        count = index
        productID = productEntity.id
        name = productEntity.productName
        qty = cartEntity.productQty
        price = productEntity.productRetailPrice
    }
}

data class CartWithHeader(
    @Embedded
    val cartHeader: CartHeaderEntity,
    @Relation(
        parentColumn = "cart_header_id",
        entityColumn = "cart_header_id",
        entity = CartEntity::class
    )
    val cartList: List<CartWithProduct>
) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CartWithHeader>() {
            override fun areItemsTheSame(
                oldItem: CartWithHeader,
                newItem: CartWithHeader
            ): Boolean {
                return oldItem.cartHeader.id == newItem.cartHeader.id
            }

            override fun areContentsTheSame(
                oldItem: CartWithHeader,
                newItem: CartWithHeader
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    @Ignore
    val totalQty = cartList.sumBy { it.cartEntity.productQty }
    @Ignore
    private val totalAmount =
        cartList.sumByDouble { it.productEntity.productRetailPrice * it.cartEntity.productQty.toDouble() }
            .toLong()

    fun getTotalNetAmount(prefix: String = "") =
        String.format("%s %s", totalAmount, prefix)

    fun toPrintVOList() = printVOList {
        printVO {
            type = PrintType.HEADER
            data = cartHeader.toPrintHeader()
        }
        cartList.forEachIndexed { index, cartWithProduct ->
            printVO {
                type = PrintType.ITEMS
                data = cartWithProduct.toPrintVO(index + 1)
            }
        }
        printVO {
            type = PrintType.TOTAL
            data = printTotal {
                totalAmount = this@CartWithHeader.totalAmount
                tax = cartHeader.tax
                totalQty = this@CartWithHeader.totalQty
            }
        }
        printVO {
            type = PrintType.FOOTER
        }
    }
}
