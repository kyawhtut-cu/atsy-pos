package com.kyawhtut.pos.ui.ticket

import android.content.SharedPreferences
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.ProductDao
import com.kyawhtut.pos.data.db.dao.SellDao
import com.kyawhtut.pos.data.db.dao.TicketDao
import com.kyawhtut.pos.data.db.entity.SellEntity
import com.kyawhtut.pos.data.db.entity.SellList
import com.kyawhtut.pos.data.db.entity.TicketBuilder
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.data.vo.PrintVO
import com.kyawhtut.pos.data.vo.printVO
import com.kyawhtut.pos.data.vo.productSell

class TicketRepositoryImp(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val sellDao: SellDao,
    private val ticketDao: TicketDao,
    private val productDao: ProductDao
) : BaseRepositoryImpl(sh, rootUser), TicketRepository {

    override fun getProductById(role: Int, pId: Int): PrintVO = productDao.get(pId).run {
        return printVO {
            data = productSell {
                count = role
                productID = id
                name = productName
                qty = 1
                price = productRetailPrice
            }
        }
    }

    override fun insertTicket(block: TicketBuilder.() -> Unit) {
        ticketDao.insert(TicketBuilder().apply(block).build())
    }

    override fun insertSell(sellList: List<SellEntity>) {
        productDao.updateQuality(sellList)
        sellList.forEach {
            sellDao.insert(it)
        }
    }

    override fun insertSell(block: SellList.() -> Unit) {
        insertSell(SellList().apply(block))
    }
}