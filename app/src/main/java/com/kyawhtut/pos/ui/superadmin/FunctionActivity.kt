package com.kyawhtut.pos.ui.superadmin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.amulyakhare.textdrawable.TextDrawable
import com.kyawhtut.lib.rv.GridSpacingItemDecoration
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.update
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivity
import com.kyawhtut.pos.data.vo.FunctionVO
import com.kyawhtut.pos.ui.home.HomeActivity
import com.kyawhtut.pos.ui.table.TableActivity
import com.kyawhtut.pos.ui.table.TableType
import com.kyawhtut.pos.utils.getColorValue
import com.kyawhtut.pos.utils.popupDialogBuilder
import com.kyawhtut.pos.utils.startActivity
import com.kyawhtut.pos.utils.startActivityForResult
import kotlinx.android.synthetic.main.activity_super_admin.*
import kotlinx.android.synthetic.main.item_function.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FunctionActivity : BaseActivity<FunctionViewModel>(R.layout.activity_super_admin) {

    companion object {
        private const val SALE_RESULT = 0x001
    }

    private var saleObject: FunctionVO = FunctionVO.Builder().build()
    private var saleIndex: Int = 0

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
                    checkLogin()
                }
            }
        }
    }

    override fun setup(bundle: Bundle) {

        popupDialog.view = iv_current_user

        iv_current_user.setImageDrawable(
            TextDrawable.builder().buildRound(
                viewModel.getCurrentUser()?.displayName?.get(0).toString(),
                getColorValue(R.color.colorAccent)
            )
        )

        iv_current_user.setOnClickListener {
            popupDialog.bind().show()
        }

        rv_function.apply {
            bind(viewModel.getFunctionList(), R.layout.item_function) { item, pos ->
                this.item_cl.setOnClickListener {
                    if (item.functionAvailable) {
                        when (item.functionTitle) {
                            "Sale" -> startActivityForResult<HomeActivity>(SALE_RESULT).also {
                                saleIndex = pos
                                saleObject = item
                            }
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
                        }
                    }
                }
                this.view_permission_denied.visibility =
                    if (item.functionAvailable) View.GONE else View.VISIBLE
                this.tv_function_title.mText = item.functionTitle
                this.tv_function_description.mText = item.functionDescription
                this.iv_warning.visibility = if (!item.status) View.GONE else View.VISIBLE
            }.layoutManager(GridLayoutManager(this@FunctionActivity, 2))
            addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SALE_RESULT) {
            rv_function.update(saleIndex, saleObject.apply {
                status = resultCode != Activity.RESULT_OK
            })
        }
    }
}