package com.kyawhtut.pos.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.commit
import com.ferfalk.simplesearchview.SimpleSearchView
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivity
import com.kyawhtut.pos.ui.category.CategoryFragment
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialog
import com.kyawhtut.pos.ui.product.ProductAddDialog
import com.kyawhtut.pos.ui.ticket.TicketFragment
import kotlinx.android.synthetic.main.activity_main.*
import moe.feng.common.view.breadcrumbs.DefaultBreadcrumbsCallback
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeActivity : BaseActivity<HomeViewModel>(
    R.layout.activity_main,
    R.menu.menu_main
) {

    override val viewModel: HomeViewModel by viewModel()
    var onNavigationItemClick: (BreadcrumbItem, Int) -> Unit = { item, pos -> }

    val ticketFragment: TicketFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.right_panel) as TicketFragment
    }

    override fun setup(bundle: Bundle) {
        supportFragmentManager.commit {
            replace(R.id.left_panel, CategoryFragment())
        }

        breadcrumbs_view.setItems(mutableListOf(BreadcrumbItem.createSimpleItem("Items")))

        breadcrumbs_view.setCallback(object : DefaultBreadcrumbsCallback<BreadcrumbItem>() {
            override fun onNavigateBack(item: BreadcrumbItem, position: Int) {
                onNavigationItemClick(item, position)
            }

            override fun onNavigateNewLocation(newItem: BreadcrumbItem, changedPosition: Int) {
                Timber.d("onNavigateNewLocation -> %s %s", newItem.items, changedPosition)
            }
        })

        iv_search.setOnClickListener {
            if (search_view.isSearchOpen) search_view.closeSearch() else search_view.showSearch()
        }

        iv_add_category.setOnClickListener {
            if (breadcrumbs_view.items.size > 1) ProductAddDialog.show(supportFragmentManager)
            else CategoryAddDialog.show(supportFragmentManager)
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        search_view.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Timber.d("onQueryTextSubmit %s", query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Timber.d("onQueryTextChange %s", newText)
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                Timber.d("onQueryTextCleared")
                return false
            }
        })
    }

    fun nextIndex(title: String, subList: MutableList<String> = mutableListOf()) {
        if (breadcrumbs_view.items.size > 1) breadcrumbs_view.removeLastItem()
        breadcrumbs_view.addItem(BreadcrumbItem(subList.apply { add(0, title) }))
        onNavigationItemClick(
            BreadcrumbItem(subList.apply { add(0, title) }),
            breadcrumbs_view.items.size
        )
    }

    override fun onClickMenu(id: Int) {
        when (id) {
            R.id.action_settings -> viewModel.logout().run {
                finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (search_view.onActivityResult(requestCode, resultCode, data)) return
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (search_view.onBackPressed()) return
        if (breadcrumbs_view.items.size > 1) breadcrumbs_view.removeLastItem().run {
            onNavigationItemClick(
                breadcrumbs_view.items.first() as BreadcrumbItem,
                0
            )
            return
        }
        if (ticketFragment.isOrder()) AlertDialog.Builder(this).apply {
            setTitle("Draft")
            setMessage("Are you want to draft this order")
            setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
                setResult(Activity.RESULT_CANCELED)
                super.onBackPressed()
            }
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
        }.show().run {
            return
        }
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
    }
}
