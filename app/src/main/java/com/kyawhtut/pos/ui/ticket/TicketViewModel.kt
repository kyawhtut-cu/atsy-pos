package com.kyawhtut.pos.ui.ticket

import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.data.vo.PrintType
import com.kyawhtut.pos.data.vo.PrintVO
import com.kyawhtut.pos.data.vo.ProductSellVO
import org.joda.time.DateTime

class TicketViewModel(private val repo: TicketRepository) : BaseViewModel(repo) {

    var isAddedHeader = false
    var isAddedTotalVO = false
    var productCount = 1
    val cartList: MutableList<PrintVO> = mutableListOf()
    fun setProduct(pos: Int, data: PrintVO) {
        cartList.add(pos, data)
    }

    fun clearCartList() {
        cartList.clear()
        isAddedHeader = false
        isAddedTotalVO = false
        productCount = 1
    }

    fun getProductById(role: Int, pId: Int) = repo.getProductById(role, pId)

    fun getTotalAmount(list: List<PrintVO> = cartList) = list.filter {
        it.type is PrintType.ITEMS
    }.sumByDouble {
        (it.data as ProductSellVO).run {
            price * qty
        }.toDouble()
    }.toLong()

    fun getTotalQty(list: List<PrintVO> = cartList) = list.filter {
        it.type is PrintType.ITEMS
    }.sumBy {
        (it.data as ProductSellVO).qty
    }

    fun insertOrder(list: List<PrintVO> = cartList) {
        val ticketID = String.format("TICKET-%s", DateTime.now().millis)
        repo.insertTicket {
            ticketId = ticketID
            customerId = 1
            totalPrice = getTotalAmount(list)
            payAmount = 100
            createdUserId = getCurrentUser()?.id ?: 0
            updatedUserId = getCurrentUser()?.id ?: 0
        }
        repo.insertSell(PrintVO.printVo2SellList(cartList, ticketID))
    }
}