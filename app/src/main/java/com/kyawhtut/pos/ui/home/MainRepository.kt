package com.kyawhtut.pos.ui.home

import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.TicketEntity

/**
 * @author kyawhtut
 * @date 14/05/2020
 */
interface MainRepository : BaseRepository {

    val totalTicketCount: Int
    val totalProductCount: Int
    val totalWarningCount: Int
    val totalCustomerCount: Int
    val mostSellingProductList: Triple<List<Float>, List<Int>, List<String>>
    fun getTicketList(filterType: MainViewModel.TicketFilterType): List<TicketEntity>
}
