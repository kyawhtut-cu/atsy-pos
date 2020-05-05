package com.kyawhtut.pos.ui.ticket

import android.text.Spannable
import androidx.lifecycle.MutableLiveData
import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.data.db.entity.customer
import com.kyawhtut.pos.data.vo.*
import org.joda.time.DateTime

class TicketViewModel(private val repo: TicketRepository) : BaseViewModel(repo) {

    var customerEntity = customer {
        id = -1
    }

    var isAddedHeader = false

    var isAddedTotalVO = false

    private var productCount = 1

    var ticketID = String.format("TICKET-%s", DateTime.now().millis.toString().substring(6))

    val printFooter: Spannable
        get() = repo.printFooter

    val printHeader: Spannable
        get() = repo.printHeader

    fun generateNewTicketID(): String {
        ticketID = String.format("TICKET-%s", DateTime.now().millis.toString().substring(6))
        return ticketID
    }

    val _cartList: MutableList<PrintVO> = mutableListOf()
    val cartList: MutableLiveData<MutableList<PrintVO>> = MutableLiveData()

    fun getDraftCarListByTicketId(ticketId: String) {
        ticketID = ticketId
        _cartList.clear()
        with(repo.getCartList(ticketId)) {
            if (this.isNotEmpty())
                customerEntity = customer {
                    with(this@with.first { it.type == PrintType.HEADER }.data as PrintHeader) {
                        this@customer.id = customerID
                        this@customer.customerName = customerName
                        this@customer.customerPhone = customerPhone
                    }
                }
            _cartList.addAll(this)
            cartList.postValue(this.toMutableList())
            isAddedHeader = this.isNotEmpty()
            isAddedTotalVO = this.isNotEmpty()
            productCount = if (this.isNotEmpty()) this.size - 2 else 1
        }
    }

    val isHasItem: Boolean
        get() = _cartList.any { it.type is PrintType.ITEMS }

    fun setProduct(pos: Int, data: PrintVO) {
        _cartList.add(pos, data)
    }

    fun clearCartList() {
        _cartList.clear()
        isAddedHeader = false
        isAddedTotalVO = false
        productCount = 1
    }

    fun getCustomerList() = repo.getCustomerList()

    fun getProductById(pId: Int) = repo.getProductById(productCount++, pId)

    fun getProductIdByProductCode(productCode: String) = repo.getProductIdByProductCode(productCode)

    fun getTotalAmount() = _cartList.filter {
        it.type is PrintType.ITEMS
    }.sumByDouble {
        (it.data as ProductSellVO).run {
            price * qty
        }.toDouble()
    }.toLong()

    fun getTotalQty() = _cartList.filter {
        it.type is PrintType.ITEMS
    }.sumBy {
        (it.data as ProductSellVO).qty
    }

    fun getValue(type: Int) = _cartList.filter {
        it.type is PrintType.TOTAL
    }.run {
        return@run if (isEmpty()) 0 else if (type == 0) (first().data as PrintTotalVO).discountAmount else (first().data as PrintTotalVO).paidAmount
    }

    fun insertOrder() {
        repo.insertTicket {
            ticketId = ticketID
            customerId = customerEntity.id
            totalPrice = getTotalAmount() + (getTotalAmount() * taxAmount / 100f).toInt()
            discountAmount = getValue(0)
            payAmount = getValue(1)
            createdUserId = getCurrentUser()?.id ?: 0
            updatedUserId = getCurrentUser()?.id ?: 0
        }
        repo.insertSell(PrintVO.printVo2SellList(_cartList, ticketID))
        deleteVoucherByTicketID(ticketID)
    }

    fun saveCacheCart() {
        repo.saveCacheCart(ticketID, _cartList, repo.taxAmount)
    }

    fun deleteVoucherByTicketID(ticketId: String) {
        repo.deleteItemById(ticketId)
    }

    fun getDraftCartList() = repo.getDraftCart()
}