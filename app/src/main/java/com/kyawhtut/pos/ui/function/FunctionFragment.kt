package com.kyawhtut.pos.ui.function

/*
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.amulyakhare.textdrawable.TextDrawable
import com.kyawhtut.lib.rv.GridSpacingItemDecoration
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.get
import com.kyawhtut.lib.rv.update
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivityViewModel
import com.kyawhtut.pos.base.BaseFragmentViewModel
import com.kyawhtut.pos.data.vo.FunctionVO
import com.kyawhtut.pos.ui.home.HomeActivity
import com.kyawhtut.pos.ui.setting.SettingActivity
import com.kyawhtut.pos.ui.table.TableActivity
import com.kyawhtut.pos.ui.table.TableType
import com.kyawhtut.pos.utils.getColorValue
import com.kyawhtut.pos.utils.popupDialogBuilder
import com.kyawhtut.pos.utils.startActivity
import kotlinx.android.synthetic.main.activity_super_admin.*
import kotlinx.android.synthetic.main.item_function.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FunctionFragment :
    BaseFragmentViewModel<FunctionViewModel>(R.layout.activity_super_admin) {

    override val viewModel: FunctionViewModel by viewModel { parametersOf(this) }
    private val popupDialog = popupDialogBuilder {
        popupItemList {
            popupItem {
                title = "Logout"
                icon = R.drawable.ic_logout_white
            }
        }
        callback = {
            when (title) {
                "Logout" -> viewModel.logout().run {
                }
            }
        }
    }

    override fun onViewCreated(bundle: Bundle) {
        popupDialog.view = iv_current_user

        iv_current_user.setImageDrawable(
            TextDrawable.builder().buildRound(
                viewModel.getCurrentUser()?.displayName?.get(0).toString(),
                context.getColorValue(R.color.colorAccent)
            )
        )

        iv_current_user.setOnClickListener {
            popupDialog.bind().show()
        }

        rv_function.apply {
            bind(viewModel.getFunctionList(), R.layout.item_function) { item, pos ->
                this.item_cl.setOnClickListener {
                    if (item.functionAvailable) {
                        */
/*when (item.functionTitle) {
                            "Sale" -> startActivity<HomeActivity>()
                            "Items" -> startActivity<TableActivity>(
                                TableActivity.extraTableType to TableType.ITEMS
                            )
                            "Users" -> startActivity<TableActivity>(
                                TableActivity.extraTableType to TableType.USERS
                            )
                            "Products" -> startActivity<TableActivity>(
                                TableActivity.extraTableType to TableType.PRODUCTS
                            )
                            "Customer" -> startActivity<TableActivity>(
                                TableActivity.extraTableType to TableType.CUSTOMER
                            )
                            "Setting" -> startActivity<SettingActivity>()
                        }*//*

                    }
                }
                this.view_permission_denied.visibility =
                    if (item.functionAvailable) View.GONE else View.VISIBLE
                this.tv_function_title.mText = item.functionTitle
                this.tv_function_description.mText = item.functionDescription
                this.iv_warning.visibility = if (!item.status) View.GONE else View.VISIBLE
            }.layoutManager(GridLayoutManager(this@FunctionFragment, 2))
            addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        }

        viewModel.isCartDataHas.observe(this) {
            rv_function.update(
                viewModel.getTitleIndex("Sale"),
                rv_function.get<FunctionVO>(viewModel.getTitleIndex("Sale")).apply {
                    status = it
                })
        }
    }
}*/
