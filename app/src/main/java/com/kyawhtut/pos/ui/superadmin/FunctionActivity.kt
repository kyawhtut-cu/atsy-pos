package com.kyawhtut.pos.ui.superadmin

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.amulyakhare.textdrawable.TextDrawable
import com.kyawhtut.lib.rv.GridSpacingItemDecoration
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.pos.R
import com.kyawhtut.pos.ui.authentication.login.LoginActivity
import com.kyawhtut.pos.ui.base.BaseActivity
import com.kyawhtut.pos.ui.home.HomeActivity
import com.kyawhtut.pos.ui.user.UserActivity
import com.kyawhtut.pos.utils.getColorValue
import com.kyawhtut.pos.utils.popupDialogBuilder
import com.kyawhtut.pos.utils.startActivity
import kotlinx.android.synthetic.main.activity_super_admin.*
import kotlinx.android.synthetic.main.item_function.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FunctionActivity : BaseActivity<FunctionViewModel>(R.layout.activity_super_admin) {

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
                        when (pos) {
                            1 -> startActivity<HomeActivity>()
                            2 -> startActivity<UserActivity>()
                            3 -> startActivity<LoginActivity>(
                                LoginActivity.extraLoginType to true
                            )
                        }
                    }
                }
                this.view_permission_denied.visibility =
                    if (item.functionAvailable) View.GONE else View.VISIBLE
                this.tv_function_title.mText = item.functionTitle
                this.tv_function_description.mText = item.functionDescription
            }.layoutManager(GridLayoutManager(this@FunctionActivity, 2))
            addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        }
    }
}