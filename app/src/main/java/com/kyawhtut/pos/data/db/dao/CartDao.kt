package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.CartEntity
import com.kyawhtut.pos.data.db.entity.CartHeaderEntity
import com.kyawhtut.pos.data.db.entity.CartWithHeader
import io.reactivex.Flowable

@Dao
abstract class CartDao : BaseDao<CartEntity> {

    @Query("select count(*) from cart_header_table")
    abstract fun getCartCount(): Flowable<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCartHeader(vararg data: CartHeaderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertCartHeader(data: CartHeaderEntity): Long

    @Transaction
    @Query("select * from cart_header_table where ticket_id = :ticketId")
    abstract fun get(ticketId: String = ""): CartWithHeader?

    @Transaction
    @Query("select * from cart_header_table")
    abstract fun get(): LiveData<List<CartWithHeader>>

    @Query("select cart_header_id from cart_header_table where ticket_id = :ticketID")
    abstract fun getCartHeaderIDByTikcetID(ticketID: String): Int

    @Query("delete from cart_table")
    abstract fun deleteCartByHeaderId()

    @Query("delete from cart_header_table where ticket_id = :ticketID")
    abstract fun deleteCartHeaderByTicketID(ticketID: String)

    @Query("delete from cart_table where cart_header_id = :cartHeaderID")
    abstract fun deleteCartByCartHeaderID(cartHeaderID: Int)

    @Query("delete from cart_header_table")
    abstract fun deleteCartHeader()
}