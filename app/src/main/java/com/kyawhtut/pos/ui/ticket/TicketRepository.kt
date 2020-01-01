package com.kyawhtut.pos.ui.ticket

import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.*
import com.kyawhtut.pos.data.vo.PrintVO

interface TicketRepository : BaseRepository {

    fun getCustomerList(): List<CustomerEntity>

    fun getCartList(ticketId: String): List<PrintVO>

    fun getProductById(role: Int, pId: Int): PrintVO

    fun getProductIdByProductCode(productCode: String): Int

    fun insertTicket(block: TicketBuilder.() -> Unit)

    fun insertSell(block: SellList.() -> Unit)

    fun insertSell(sellList: List<SellEntity>)

    @Transaction
    fun saveCacheCart(ticketID: String, list: List<PrintVO>, taxAmount: Int)

    fun getDraftCart(): LiveData<List<CartWithHeader>>
}