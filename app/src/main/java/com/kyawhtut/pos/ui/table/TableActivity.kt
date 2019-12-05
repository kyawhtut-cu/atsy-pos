package com.kyawhtut.pos.ui.table

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.observe
import com.evrencoskun.tableview.filter.Filter
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.gson.Gson
import com.kyawhtut.pos.R
import com.kyawhtut.pos.adapter.TableAdapter
import com.kyawhtut.pos.base.BaseActivity
import com.kyawhtut.pos.ui.authentication.login.LoginActivity
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialog
import com.kyawhtut.pos.ui.customer.CustomerDialog
import com.kyawhtut.pos.ui.product.ProductAddDialog
import com.kyawhtut.pos.utils.*
import com.kyawhtut.pos.utils.listeners.TablePagination
import com.kyawhtut.pos.utils.listeners.TableViewClickListener
import kotlinx.android.synthetic.main.activity_table.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class TableActivity : BaseActivity<TableViewModel>(R.layout.activity_table) {

    companion object {
        const val extraTableType = "extra.tableType"
    }

    private var tableType: TableType = TableType.ITEMS
    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableFilter: Filter
    private lateinit var tablePagination: TablePagination

    override val viewModel: TableViewModel by viewModel()

    override fun setup(bundle: Bundle) {

        tableType = bundle.getSerializable(extraTableType) as TableType
        tableAdapter = TableAdapter(
            this,
            getDatePosition(tableType),
            getColorPosition(tableType),
            getTextColorPosition(tableType),
            getActivePosition(tableType),
            callAdd = {
                when (tableType) {
                    is TableType.ITEMS -> CategoryAddDialog.show(supportFragmentManager)
                    is TableType.USERS -> startActivity<LoginActivity>(
                        LoginActivity.extraLoginType to true
                    )
                    is TableType.PRODUCTS -> ProductAddDialog.show(supportFragmentManager)
                    is TableType.CUSTOMER -> CustomerDialog.show(supportFragmentManager)
                }
            },
            callbackEdit = {
                when (tableType) {
                    is TableType.ITEMS -> CategoryAddDialog.show(supportFragmentManager, it)
                    is TableType.USERS -> startActivity<LoginActivity>(
                        LoginActivity.extraLoginType to true,
                        LoginActivity.extraUserID to it
                    )
                    is TableType.PRODUCTS -> ProductAddDialog.show(supportFragmentManager, it)
                    is TableType.CUSTOMER -> CustomerDialog.show(supportFragmentManager, it)
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

        search_view.setKeepQuery(true)
        search_view.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setFilter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                setFilter(newText)
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                setFilter("")
                return false
            }
        })

        iv_search.setOnClickListener {
            search_view.showSearch()
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

        for (l in Locale.getAvailableLocales()) {
            Timber.d(
                "Locales %s_%s[%s] %s %s",
                l.language,
                l.country,
                l.displayName,
                l.displayCountry,
                l.displayLanguage
            )
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
        )*/
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
}