package com.kyawhtut.pos.ui.ticket

import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.CartWithHeader
import com.kyawhtut.pos.data.db.entity.SellEntity
import com.kyawhtut.pos.data.db.entity.SellList
import com.kyawhtut.pos.data.db.entity.TicketBuilder
import com.kyawhtut.pos.data.vo.PrintVO

interface TicketRepository : BaseRepository {

    fun getCartList(ticketId: String): List<PrintVO>

    fun getProductById(role: Int, pId: Int): PrintVO

    fun insertTicket(block: TicketBuilder.() -> Unit)

    fun insertSell(block: SellList.() -> Unit)

    fun insertSell(sellList: List<SellEntity>)

    @Transaction
    fun saveCacheCart(ticketID: String, list: List<PrintVO>, taxAmount: Int)

    fun getDraftCart(): LiveData<List<CartWithHeader>>
}