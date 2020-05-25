package com.kyawhtut.pos.data.vo

import com.kyawhtut.pos.data.db.entity.SellEntity
import com.kyawhtut.pos.data.db.entity.sell

sealed class PrintType {
    object HEADER : PrintType()
    object ITEMS : PrintType()
    object FOOTER : PrintType()
    object TOTAL : PrintType()
}

data class PrintHeader private constructor(
    val customerID: Int,
    val customerName: String,
    val customerPhone: String,
    val ticketID: String,
    val waiterName: String,
    val waiterID: Int,
    val ticketDate: String = ""
) {

    class Builder {
        var customerID: Int = 0
        var customerName: String = ""
        var customerPhone: String = ""
        var ticketID: String = ""
        var waiterName: String = ""
        var waiterID: Int = 0
        var ticketDate: String = ""

        fun build() =
            PrintHeader(
                customerID,
                customerName,
                customerPhone,
                ticketID,
                waiterName,
                waiterID,
                ticketDate
            )
    }
}

fun printHeader(block: PrintHeader.Builder.() -> Unit) = PrintHeader.Builder().apply(block).build()

data class PrintVO private constructor(
    var type: PrintType = PrintType.ITEMS,
    var data: Any = Object()
) {

    companion object {
        fun printVo2SellList(list: List<PrintVO>, ticketID: String): List<SellEntity> {
            list.filter {
                it.type == PrintType.ITEMS
            }.map {
                val data = it.data as ProductSellVO
                sell {
                    productId = data.productID
                    productName = data.name
                    ticketId = ticketID
                    productQuality = data.qty
                    productPrice = data.price
                }
            }.run {
                return this
            }
        }
    }

    class Builder {
        var type: PrintType = PrintType.ITEMS
        var data: Any = Object()

        fun build() = PrintVO(type, data)
    }
}

class PrintVOList : ArrayList<PrintVO>() {
    fun printVO(block: PrintVO.Builder.() -> Unit) {
        add(PrintVO.Builder().apply(block).build())
    }
}

fun printVOList(block: PrintVOList.() -> Unit) = PrintVOList().apply(block)

fun printVO(block: PrintVO.Builder.() -> Unit) = PrintVO.Builder().apply(block).build()