package com.kyawhtut.pos.data.vo

data class ProductSellVO private constructor(
    var productID: Int = 0,
    var count: Int = 1,
    var name: String = "",
    var qty: Int = 0,
    var price: Long = 0L
) {
    fun getTotalPrice(prefix: String = "") = String.format("%s %s", qty * price, prefix)

    class Builder {
        var productID: Int = 0
        var count: Int = 1
        var name: String = ""
        var qty: Int = 0
        var price: Long = 0L

        fun build() = ProductSellVO(productID, count, name, qty, price)
    }
}

fun productSell(block: ProductSellVO.Builder.() -> Unit) =
    ProductSellVO.Builder().apply(block).build()
