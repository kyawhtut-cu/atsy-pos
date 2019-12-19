package com.kyawhtut.pos.ui.ticket

import androidx.lifecycle.MutableLiveData
import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.data.vo.PrintType
import com.kyawhtut.pos.data.vo.PrintVO
import com.kyawhtut.pos.data.vo.ProductSellVO
import org.joda.time.DateTime

class TicketViewModel(private val repo: TicketRepository) : BaseViewModel(repo) {

    var isAddedHeader = false

    var isAddedTotalVO = false

    private var productCount = 1

    var ticketID = String.format("TICKET-%s", DateTime.now().millis.toString().substring(6))

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

    fun getProductById(pId: Int) = repo.getProductById(productCount++, pId)

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

    fun insertOrder() {
        repo.insertTicket {
            ticketId = ticketID
            customerId = 1
            totalPrice = getTotalAmount()
            payAmount = 100
            createdUserId = getCurrentUser()?.id ?: 0
            updatedUserId = getCurrentUser()?.id ?: 0
        }
        repo.insertSell(PrintVO.printVo2SellList(_cartList, ticketID))
    }

    fun saveCacheCart() {
        repo.saveCacheCart(ticketID, _cartList, 5)
    }

    fun deleteVoucherByTicketID(ticketId: String) {
        repo.deleteItemById(ticketId)
    }

    fun getDraftCartList() = repo.getDraftCart()
}