package com.kyawhtut.pos.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import com.ferfalk.simplesearchview.SimpleSearchView
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivityViewModel
import com.kyawhtut.pos.phone.home.PhoneHomeActivity
import com.kyawhtut.pos.ui.login.LoginFragment
import com.kyawhtut.pos.ui.category.CategoryFragment
import com.kyawhtut.pos.ui.category.dialog.CategoryAddDialog
import com.kyawhtut.pos.ui.product.ProductAddDialog
import com.kyawhtut.pos.ui.sale.SaleFragment
import com.kyawhtut.pos.ui.table.TableFragment
import com.kyawhtut.pos.ui.table.TableType
import com.kyawhtut.pos.utils.isTablet
import com.kyawhtut.pos.utils.startActivity
import kotlinx.android.synthetic.main.activity_home.*
import moe.feng.common.view.breadcrumbs.DefaultBreadcrumbsCallback
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivityViewModel<HomeViewModel>(
    R.layout.activity_home,
    R.menu.menu_main,
    toolbar = R.id.toolbar,
    isBackActivity = false
) {

    override val viewModel: HomeViewModel by viewModel()
    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.content_home)

    override fun setup(savedInstanceState: Bundle?, bundle: Bundle) {
        if (!isTablet()) finish().run {
            startActivity<PhoneHomeActivity>()
        }
        if (savedInstanceState == null)
            supportFragmentManager.commit {
                add(R.id.content_home, MainFragment.createInstance(viewModel.isLogin()))
            }

        viewModel.getLoginState().observe(this) {
            // fixme: need to change drawer disable
            supportFragmentManager.commit {
                if (!it)
                    replace(R.id.content_home, LoginFragment())
                else replace(R.id.content_home, MainFragment.createInstance(it))
            }
            mini_drawer.drawerMenuList = viewModel.getDrawerMenu(this)

            if (savedInstanceState == null) setBreadCrumbsTitle(1)
        }

        viewModel.isCartDataHas.observe(this) {
            if (viewModel.getIndex(
                    this,
                    "Sale"
                ) != -1
            ) mini_drawer.setBadge(viewModel.getIndex(this, "Sale"), it, it != 0)
        }

        breadcrumbs_view.setCallback(object : DefaultBreadcrumbsCallback<BreadcrumbItem>() {
            override fun onNavigateBack(item: BreadcrumbItem, position: Int) {
                when (currentFragment) {
                    is SaleFragment -> (currentFragment as SaleFragment).changeCategoryFragment(
                        item,
                        position
                    )
                    is CategoryFragment -> (currentFragment as CategoryFragment).changeData(
                        item,
                        position
                    )
                }
            }

            override fun onNavigateNewLocation(newItem: BreadcrumbItem, changedPosition: Int) {
            }
        })

        search_view.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                when (currentFragment) {
                    is TableFragment -> {
                        (currentFragment as TableFragment).setFilter(query)
                    }
                    is SaleFragment -> {
                        (currentFragment as SaleFragment).filter(query)
                    }
                    is CategoryFragment -> {
                        (currentFragment as CategoryFragment).filter(query)
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                when (currentFragment) {
                    is TableFragment -> {
                        (currentFragment as TableFragment).setFilter(newText)
                    }
                    is SaleFragment -> {
                        (currentFragment as SaleFragment).filter(newText)
                    }
                    is CategoryFragment -> {
                        (currentFragment as CategoryFragment).filter(newText)
                    }
                }
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                when (currentFragment) {
                    is TableFragment -> {
                        (currentFragment as TableFragment).setFilter("")
                    }
                    is SaleFragment -> {
                        (currentFragment as SaleFragment).filter("")
                    }
                    is CategoryFragment -> {
                        (currentFragment as CategoryFragment).filter("")
                    }
                }
                return false
            }
        })

        mini_drawer.onMenuItemClick = { type, pos ->
            changeFragmentByTitle(pos)
        }
    }

    private fun changeFragmentByTitle(pos: Int) {
        when (mini_drawer.getTitle(pos)) {
            "Home" -> openScreen(MainFragment.createInstance(viewModel.isLogin()), pos)
            "Sale" -> openScreen(SaleFragment(), pos)
            "Logout" -> viewModel.logout()
            else -> openScreen(
                TableFragment.createInstance(
                    when (mini_drawer.getTitle(pos)) {
                        "Items" -> TableType.ITEMS
                        "Products" -> TableType.PRODUCTS
                        "Users" -> TableType.USERS
                        "Customer" -> TableType.CUSTOMER
                        else -> TableType.DEFAULT
                    }
                ), pos
            )
        }
    }

    private fun setBreadCrumbsTitle(pos: Int = -1, title: String = "") {
        breadcrumbs_view.setItems(
            mutableListOf(
                BreadcrumbItem.createSimpleItem(
                    if (pos != -1) mini_drawer.getTitle(
                        pos
                    ) else title
                )
            )
        )
    }

    fun openScreen(fragment: Fragment, pos: Int = -1, title: String = "") {
        setBreadCrumbsTitle(pos, title)
        supportFragmentManager.commit {
            replace(R.id.content_home, fragment)
        }
    }

    fun nextIndex(title: String, subList: MutableList<String> = mutableListOf()) {
        if (breadcrumbs_view.items.size > 1) breadcrumbs_view.removeLastItem()
        breadcrumbs_view.addItem(BreadcrumbItem(subList.apply { add(0, title) }))
        when (currentFragment) {
            is SaleFragment -> (currentFragment as SaleFragment).changeCategoryFragment(
                BreadcrumbItem(subList.apply { add(0, title) }),
                breadcrumbs_view.items.size
            )
            is CategoryFragment -> (currentFragment as CategoryFragment).changeData(
                BreadcrumbItem(subList.apply { add(0, title) }),
                breadcrumbs_view.items.size
            )
        }
    }

    fun goToLogin() {
        openScreen(LoginFragment(), title = "Login")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (search_view.onActivityResult(requestCode, resultCode, data)) return
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClickMenu(id: Int) {
        when (id) {
            R.id.action_search -> {
                if (search_view.isSearchOpen) search_view.closeSearch() else search_view.showSearch()
            }
            R.id.action_add -> {
                when (currentFragment) {
                    is SaleFragment -> {
                        if (breadcrumbs_view.items.size > 1) ProductAddDialog.show(
                            supportFragmentManager
                        )
                        else CategoryAddDialog.show(supportFragmentManager)
                    }
                    is CategoryFragment -> {
                        if (breadcrumbs_view.items.size > 1) ProductAddDialog.show(
                            supportFragmentManager
                        )
                        else CategoryAddDialog.show(supportFragmentManager)
                    }
                    is TableFragment -> (currentFragment as TableFragment).addNewData()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (search_view.onBackPressed()) return
        if (mini_drawer.onBackPressed()) return
        if (breadcrumbs_view.items.size > 1) {
            breadcrumbs_view.removeLastItem().run {
                when (currentFragment) {
                    is SaleFragment -> {
                        (currentFragment as SaleFragment).changeCategoryFragment(
                            breadcrumbs_view.items.first() as BreadcrumbItem,
                            0
                        )
                    }
                    is CategoryFragment -> {
                        (currentFragment as CategoryFragment).changeData(
                            breadcrumbs_view.items.first() as BreadcrumbItem,
                            0
                        )
                    }
                }
                return
            }
        }
        super.onBackPressed()
    }
}