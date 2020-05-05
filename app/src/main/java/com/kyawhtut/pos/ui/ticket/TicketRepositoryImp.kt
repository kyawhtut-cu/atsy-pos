package com.kyawhtut.pos.ui.ticket

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.*
import com.kyawhtut.pos.data.db.entity.*
import com.kyawhtut.pos.data.vo.*

class TicketRepositoryImp(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val sellDao: SellDao,
    private val ticketDao: TicketDao,
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val customerDao: CustomerDao
) : BaseRepositoryImpl(sh, rootUser), TicketRepository {

    override fun getCustomerList(): List<CustomerEntity> {
        customerDao.get().toMutableList().run {
            add(
                customer {
                    id = 0
                    customerName = "Other"
                    customerAddress = "-"
                }
            )
            return this
        }
    }

    override fun getCartList(ticketId: String): List<PrintVO> = cartDao.get(ticketId).run {
        this?.toPrintVOList() ?: printVOList { }
    }

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

    override fun getProductIdByProductCode(productCode: String): Int =
        productDao.getProductIdByProductCode(productCode)

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

    override fun saveCacheCart(ticketID: String, list: List<PrintVO>, taxAmount: Int) {
        deleteItemById(ticketID)
        if (list.isNotEmpty()) {
            val headerId: Int
            (list.first().data as PrintHeader).also {
                headerId = cartDao.insertCartHeader(
                    cartHeader {
                        customerID = it.customerID
                        customerName = it.customerName
                        customerPhone = it.customerPhone
                        ticketId = it.ticketID
                        saleManId = it.waiterID
                        saleName = it.waiterName
                    }
                ).toInt()
            }
            list.filter { it.type is PrintType.ITEMS }.forEach {
                (it.data as ProductSellVO).also {
                    cartDao.insert(
                        cart {
                            cartHeaderId = headerId
                            productId = it.productID
                            productQty = it.qty
                        }
                    )
                }
            }
        }
    }

    override fun getDraftCart(): LiveData<List<CartWithHeader>> = cartDao.get()

    @Transaction
    override fun deleteItemById(id: String) {
        cartDao.deleteCartByCartHeaderID(cartDao.getCartHeaderIDByTicketID(id))
        cartDao.deleteCartHeaderByTicketID(id)
    }
}