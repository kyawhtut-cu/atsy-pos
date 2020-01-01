package com.kyawhtut.pos.ui.table

import android.os.Bundle
import androidx.lifecycle.observe
import com.evrencoskun.tableview.filter.Filter
import com.google.gson.Gson
import com.kyawhtut.pos.R
import com.kyawhtut.pos.adapter.TableAdapter
import com.kyawhtut.pos.base.BaseFragmentViewModel
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialog
import com.kyawhtut.pos.ui.customer.CustomerDialog
import com.kyawhtut.pos.ui.product.ProductAddDialog
import com.kyawhtut.pos.ui.user.UserAddDialog
import com.kyawhtut.pos.utils.*
import com.kyawhtut.pos.utils.listeners.TablePagination
import com.kyawhtut.pos.utils.listeners.TableViewClickListener
import kotlinx.android.synthetic.main.fragment_table.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TableFragment private constructor() :
    BaseFragmentViewModel<TableViewModel>(R.layout.fragment_table) {

    companion object {
        const val extraTableType = "extra.tableType"

        fun createInstance(type: TableType): TableFragment {
            return TableFragment().apply {
                putArg(
                    extraTableType to type
                )
            }
        }
    }

    private var tableType: TableType = TableType.ITEMS
    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableFilter: Filter
    private lateinit var tablePagination: TablePagination

    override val viewModel: TableViewModel by viewModel()

    override fun onViewCreated(bundle: Bundle) {

        Timber.d("TicketList -> %s", Gson().toJson(viewModel.getTicketList()))

        tableType = bundle.getSerializable(extraTableType) as TableType
        if (tableType is TableType.DEFAULT) return
        tableAdapter = TableAdapter(
            context!!,
            getDatePosition(tableType),
            getColorPosition(tableType),
            getTextColorPosition(tableType),
            getActivePosition(tableType),
            callAdd = {
                addNewData()
            },
            callbackEdit = {
                when (tableType) {
                    is TableType.ITEMS -> CategoryAddDialog.show(parentFragmentManager, it)
                    is TableType.USERS -> UserAddDialog.show(parentFragmentManager, it)
                    is TableType.PRODUCTS -> ProductAddDialog.show(parentFragmentManager, it)
                    is TableType.CUSTOMER -> CustomerDialog.show(parentFragmentManager, it)
                }
            },
            callbackDelete = {
                viewModel.delete(it, tableType)
                tableAdapter.removeRow(this)
                tablePagination.init()
            }
        )
        viewModel.addSource(tableType)

        table_view.setHasFixedWidth(false)
        table_view.adapter = tableAdapter

        tableFilter = Filter(table_view)
        tablePagination = TablePagination(table_view, 5)
        tablePagination.setOnTableViewPageTurnedListener { numItems, itemsStart, itemsEnd ->
            tv_current_page.currentPage = tablePagination.currentPage
        }

        btn_next.setOnClickListener {
            tablePagination.nextPage()
        }

        btn_previous.setOnClickListener {
            tablePagination.previousPage()
        }

        tv_current_page.pageClick = {
            tablePagination.goToPage(it)
        }

        table_view.tableViewListener = TableViewClickListener(table_view) {
            table_view.remeasureColumnWidth(it)
        }

        viewModel.dataList.observe(this) {
            Timber.d("Query -> %s", Gson().toJson(it))
            tableAdapter.setAllItems(
                it.first,
                it.second,
                it.third
            ).run {
                tv_current_page.pageCount = tablePagination.pageCount
            }
        }
    }

    fun addNewData() {
        when (tableType) {
            is TableType.ITEMS -> CategoryAddDialog.show(parentFragmentManager)
            is TableType.USERS -> UserAddDialog.show(parentFragmentManager)
            is TableType.PRODUCTS -> ProductAddDialog.show(parentFragmentManager)
            is TableType.CUSTOMER -> CustomerDialog.show(parentFragmentManager)
        }
    }

    fun setFilter(query: String) {
        tableFilter.set(query)
        tv_current_page.pageCount = tablePagination.pageCount
    }
}

/*val num = 1323.526
        val formater = NumberFormat.getCurrencyInstance()
        Timber.d("Default format -> %s", formater.format(num))

        val locale = Locale("my", "MM")
        val myFormater = NumberFormat.getCurrencyInstance(locale)
        Timber.d(
            "Default Myanmar format -> %s %s",
            myFormater.format(num),
            getString(R.string.app_name)
        )*//*

    }

    private fun setFilter(query: String) {
        tableFilter.set(query)
        tv_current_page.pageCount = tablePagination.pageCount
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (search_view.onActivityResult(requestCode, resultCode, data)) return
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (search_view.onBackPressed()) return
        super.onBackPressed()
    }
}*/
