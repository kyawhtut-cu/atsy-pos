package com.kyawhtut.pos.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kyawhtut.pos.base.BaseDao
import com.kyawhtut.pos.data.db.entity.TicketEntity
import com.kyawhtut.pos.data.db.entity.TicketWithProductList

@Dao
abstract class TicketDao : BaseDao<TicketEntity> {

    @Query("select * from ticket_table")
    abstract fun get(): List<TicketEntity>

    @Transaction
    @Query("select * from ticket_table")
    abstract fun getTicketWithSellList(): List<TicketWithProductList>

    @Query("select * from ticket_table where ticket_id = :ticketId")
    abstract fun get(ticketId: String): TicketEntity

    @Query("select * from ticket_table")
    abstract fun liveData(): LiveData<List<TicketEntity>>

    @Query("delete from ticket_table where ticket_id = :ticketId")
    abstract fun delete(ticketId: String)

    @Query("delete from ticket_table")
    abstract fun delete()
}