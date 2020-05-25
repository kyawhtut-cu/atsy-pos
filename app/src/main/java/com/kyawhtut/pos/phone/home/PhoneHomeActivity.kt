package com.kyawhtut.pos.phone.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kyawhtut.fontchooserlib.component.MMTextInputEditText
import com.kyawhtut.lib.minidrawer.UIUtils
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivityViewModel
import com.kyawhtut.pos.ui.category.CategoryFragment
import com.kyawhtut.pos.ui.customer.CustomerDialog
import com.kyawhtut.pos.ui.customer.CustomerFragment
import com.kyawhtut.pos.ui.home.HomeViewModel
import com.kyawhtut.pos.ui.home.MainFragment
import com.kyawhtut.pos.ui.login.LoginFragment
import com.kyawhtut.pos.ui.report.ReportFragment
import com.kyawhtut.pos.ui.sale.SaleFragment
import com.kyawhtut.pos.ui.setting.SettingActivity
import com.kyawhtut.pos.ui.table.TableFragment
import com.kyawhtut.pos.ui.table.TableType
import com.kyawhtut.pos.ui.ticket.TicketFragment
import com.kyawhtut.pos.utils.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_sheet_layout.*
import kotlinx.android.synthetic.main.dialog_code_add.view.*
import moe.feng.common.view.breadcrumbs.DefaultBreadcrumbsCallback
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhoneHomeActivity : BaseActivityViewModel<HomeViewModel>(
    R.layout.activity_home,
    R.menu.menu_main,
    toolbar = R.id.toolbar,
    isBackActivity = false
) {

    override val viewModel: HomeViewModel by viewModel()
    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.content_home)
    private val ticketFragmentBottomSheet by lazy {
        BottomSheetBehavior.from(cv_bottom_sheet)
    }
    private val ticketFragment: TicketFragment
        get() = supportFragmentManager.findFragmentById(R.id.bottom_sheet_frame) as TicketFragment

    override fun setup(savedInstanceState: Bundle?, bundle: Bundle) {
        if (savedInstanceState == null)
            supportFragmentManager.commit {
                add(R.id.content_home, MainFragment.createInstance(viewModel.isLogin()))
            }

        scanResult = {
            ticketFragment.addProduct(it)
        }

        mini_drawer.setMenuWidth(UIUtils.convertDpToPixel(260f, this))

        viewModel.getLoginState().observe(this) {
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

        viewModel.isLowerItem.observe(this) {
            if (viewModel.getIndex(
                    this,
                    "Products"
                ) != -1
            ) mini_drawer.setBadge(viewModel.getIndex(this, "Products"), it, it != 0)
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

        iv_menu.setOnClickListener {
            if (mini_drawer.isDrawerOpen) mini_drawer.closeDrawer()
            else mini_drawer.openDrawer()
        }
    }

    private fun changeFragmentByTitle(pos: Int) {
        when (mini_drawer.getTitle(pos)) {
            "Setting", "Sale" -> {
            }
            else -> ticketFragmentBottomSheet.apply {
                peekHeight = 0
                isHideable = true
                hide()
            }
        }
        when (mini_drawer.getTitle(pos)) {
            "Home" -> openScreen(MainFragment.createInstance(viewModel.isLogin()), pos).run {
                hideAllMenuItem()
            }
            "Sale" -> ticketFragmentBottomSheet.apply {
                peekHeight = UIUtils.convertDpToPixel(56f, this@PhoneHomeActivity).toInt()
                isHideable = false
            }.run {
                showAllMenuItem()
                openScreen(CategoryFragment().apply {
                    addProduct = {
                        ticketFragment.addProduct(it)
                    }
                }, pos)
            }
            "Logout" -> viewModel.logout().run {
                hideAllMenuItem()
            }
            "Customer" -> openScreen(CustomerFragment(), pos).run {
                showAllMenuItem()
                menuCamera?.isVisible = false
            }
            "Report" -> openScreen(ReportFragment.newInstance(), pos).run {
                hideAllMenuItem()
            }
            "Setting" -> {
                if (!viewModel.isLogin()) Toasty.error(
                    this,
                    "ကျေးဇူပြု၍ အကောင့်ဝင်ပေးပါ။",
                    Toasty.LENGTH_LONG
                ).show().also {
                    return
                }
                startActivity<SettingActivity>()
            }
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
            ).run {
                showAllMenuItem()
                menuCamera?.isVisible = false
            }
        }
    }

    private fun setBreadCrumbsTitle(pos: Int = -1, title: String = "") {
        breadcrumbs_view.setItems(
            mutableListOf(
                BreadcrumbItem.createSimpleItem(
                    getString(R.string.lbl_btn_login).takeUnless { viewModel.isLogin() }
                        ?: mini_drawer.getTitle(
                            pos
                        ).takeIf { pos != -1 } ?: title
                )
            )
        )
    }

    private fun openScreen(fragment: Fragment, pos: Int = -1, title: String = "") {
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
                    is TableFragment -> (currentFragment as TableFragment).addNewData()
                    is CustomerFragment -> CustomerDialog.show(supportFragmentManager)
                    else -> {
                        var edtCode: MMTextInputEditText? = null
                        showDialog(
                            getInflateView(R.layout.dialog_code_add),
                            bindView = {
                                edtCode = this.ed_product_code
                            },
                            onClickPositive = {
                                text = "Ok"
                                onClick = {
                                    ticketFragment.addProduct(edtCode?.mText ?: "")
                                }
                            },
                            onClickNegative = {
                                text = "Cancel"
                                onClick = {
                                    it.dismiss()
                                }
                            })
                    }
                }
            }
            R.id.action_camera -> openScanner()
        }
    }

    override fun onBackPressed() {
        if (ticketFragmentBottomSheet.isOpen()) {
            ticketFragmentBottomSheet.close()
            return
        }
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
