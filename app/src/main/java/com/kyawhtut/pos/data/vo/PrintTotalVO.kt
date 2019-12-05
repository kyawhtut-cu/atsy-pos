package com.kyawhtut.pos.data.vo

data class PrintTotalVO private constructor(
    var totalAmount: Long,
    var tax: Int,
    var paidAmount: Long,
    var totalQty: Int
) {
    fun getTotalPrice(prefix: String = "") = String.format("%s %s", totalAmount, prefix)

    fun getTaxAmount(prefix: String = "") =
        String.format("%s %s", (totalAmount * tax / 100f).toInt(), prefix)

    fun getTotalNetAmount(prefix: String = "") =
        String.format("%s %s", totalAmount + (totalAmount * tax / 100f).toInt(), prefix)

    fun getTotalChangeAmount(prefix: String = "") =
        String.format(
            "%s %s",
            totalAmount + (totalAmount * tax / 100f).toInt() - paidAmount,
            prefix
        )

    fun getTotalPaidAmount(prefix: String = "") = String.format("%s %s", paidAmount, prefix)

    class Builder {
        var totalAmount: Long = 0
        var tax = 0
        var paidAmount = 0L
        var totalQty = 0

        fun build() = PrintTotalVO(totalAmount, tax, paidAmount, totalQty)
    }
}

fun printTotal(block: PrintTotalVO.Builder.() -> Unit) = PrintTotalVO.Builder().apply(block).build()