package com.kyawhtut.pos.data.vo

data class PrintTotalVO private constructor(
    var totalAmount: Long,
    var paidAmount: Long,
    var totalQty: Int,
    var discountAmount: Long
) {
    fun getTotalPrice(prefix: String = "") = String.format("%s %s", totalAmount, prefix)

    fun getTaxAmount(tax: Int, prefix: String = "") =
        String.format("%s %s", (totalAmount * tax / 100f).toInt(), prefix)

    fun getTotalNetAmount(tax: Int, prefix: String = "") =
        String.format(
            "%s %s",
            totalAmount + (totalAmount * tax / 100f).toInt() - discountAmount,
            prefix
        )

    fun getTotalChangeAmount(tax: Int, prefix: String = "") =
        String.format(
            "%s %s",
            totalAmount + (totalAmount * tax / 100f).toInt() - paidAmount - discountAmount,
            prefix
        )

    fun getTotalPaidAmount(prefix: String = "") = String.format("%s %s", paidAmount, prefix)

    fun getDiscountAmount(prefix: String = "") = String.format("%s %s", discountAmount, prefix)

    class Builder {
        var totalAmount: Long = 0
        var paidAmount = 0L
        var totalQty = 0
        var discountAmount: Long = 0L

        fun build() = PrintTotalVO(totalAmount, paidAmount, totalQty, discountAmount)
    }
}

fun printTotal(block: PrintTotalVO.Builder.() -> Unit) = PrintTotalVO.Builder().apply(block).build()