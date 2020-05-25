package com.kyawhtut.pos.ui.home

import com.kyawhtut.pos.base.BaseViewModel

/**
 * @author kyawhtut
 * @date 14/05/2020
 */
class MainViewModel(private val repo: MainRepository) : BaseViewModel(repo) {

    val totalTicketCount: String = repo.totalTicketCount.toString()
    val totalProductCount: String = repo.totalProductCount.toString()
    val totalWarningCount: String = repo.totalWarningCount.toString()
    val totalCustomerCount: String = repo.totalCustomerCount.toString()
    val mostSellingProductList = repo.mostSellingProductList

    fun getTicket(filterType: String) = repo.getTicketList(TicketFilterType.getType(filterType))

    enum class TicketFilterType {
        ALL, ONE_WEEK, ONE_MONTH, ONE_YEAR;

        companion object {
            fun getType(value: String): TicketFilterType {
                return when (value) {
                    "One Week" -> ONE_WEEK
                    "One Month" -> ONE_MONTH
                    "One Year" -> ONE_YEAR
                    else -> ALL
                }
            }
        }
    }
}
