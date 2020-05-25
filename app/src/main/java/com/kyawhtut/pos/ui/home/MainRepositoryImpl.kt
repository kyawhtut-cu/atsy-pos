package com.kyawhtut.pos.ui.home

import android.content.SharedPreferences
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.gson.Gson
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.AppDatabase
import com.kyawhtut.pos.data.db.entity.TicketEntity
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.utils.toColor
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*

/**
 * @author kyawhtut
 * @date 14/05/2020
 */
class MainRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val db: AppDatabase
) : BaseRepositoryImpl(sh, rootUser), MainRepository {

    override val totalWarningCount: Int
        get() = db.productDao().getLowerItemCount(limitAmount)

    override val totalCustomerCount: Int
        get() = db.customerDao().count(SimpleSQLiteQuery("select count(*) from customer_table"))

    override val totalProductCount: Int
        get() = db.productDao().count()

    override val totalTicketCount: Int
        get() = db.ticketDao().count(SimpleSQLiteQuery("select count(*) from ticket_table"))

    override val mostSellingProductList: Triple<List<Float>, List<Int>, List<String>>
        get() {
            val data = mutableListOf<Float>()
            val color = mutableListOf<Int>()
            val title = mutableListOf<String>()
            db.productDao()
                .get(SimpleSQLiteQuery("select * from product_table where product_sell_count <> 0"))
                .forEach {
                    data.add(it.productSellCount.toFloat())
                    color.add(it.productColor.toColor())
                    title.add("%s(%s)".format(it.productName, it.productSellCount))
                }
            return Triple(data, color, title)
        }

    private fun getDateList(filterType: MainViewModel.TicketFilterType): List<String> {
        val date = DateTime.now()
        return when (filterType) {
            MainViewModel.TicketFilterType.ALL -> (0 until 0)
            MainViewModel.TicketFilterType.ONE_WEEK -> (0 until 7)
            MainViewModel.TicketFilterType.ONE_MONTH -> (0 until 31)
            MainViewModel.TicketFilterType.ONE_YEAR -> (0 until 365)
        }.map {
            date.minusDays(it).toString("dd-MM-yyyy", Locale.ENGLISH)
        }
    }

    override fun getTicketList(filterType: MainViewModel.TicketFilterType): List<TicketEntity> =
        db.ticketDao().get().filter { ticket ->
            getDateList(filterType).any { filterDate ->
                ticket.createdDate.contains(filterDate).also {
                    Timber.d("Filter %s %s", filterDate, ticket.createdDate)
                }
            }.takeIf { filterType != MainViewModel.TicketFilterType.ALL } ?: true
        }.also {
            Timber.d("getTicketList => %s", Gson().toJson(it))
        }
}
