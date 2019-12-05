package com.kyawhtut.pos.ui.ticket

import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.SellEntity
import com.kyawhtut.pos.data.db.entity.SellList
import com.kyawhtut.pos.data.db.entity.TicketBuilder
import com.kyawhtut.pos.data.vo.PrintVO

interface TicketRepository : BaseRepository {

    fun getProductById(role: Int, pId: Int): PrintVO

    fun insertTicket(block: TicketBuilder.() -> Unit)

    fun insertSell(block: SellList.() -> Unit)

    fun insertSell(sellList: List<SellEntity>)
}