package com.kyawhtut.pos.ui.authentication.login

import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toDisplay
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragmentViewModel
import com.kyawhtut.pos.data.db.entity.RoleEntity
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class LoginFragment : BaseFragmentViewModel<LoginViewModel>(R.layout.fragment_login) {

    companion object {
        const val extraLoginType = "extra.loginType"
        const val extraUserID = "extra.userID"
    }

    override val viewModel: LoginViewModel by viewModel()
    private var roleList = mutableListOf<RoleEntity>()

    override fun onViewCreated(bundle: Bundle) {

        viewModel.initRoleData(context.getStringList(R.array.user_role).toList())

        viewModel.getRoleData().observe(this) {
            roleList.apply {
                clear()
                addAll(it)
                viewModel.userRole = it.first().id
            }.map {
                it.roleName
            }.run {
                sp_create_user_role.attachDataSource(LinkedList(this))
            }
        }

        if (bundle.getBoolean(extraLoginType)) {
            viewModel.getUserById(bundle.getInt(extraUserID)).run {
                if (id != 0) {
                    sp_create_user_role.selectedIndex = roleList.indexOfFirst { it.id == roleId }
                    viewModel.userRole = roleId
                    viewModel.createdDate = createdDate
                    viewModel.createdUserId = createdUserId
                    viewModel.userId = id
                    ed_create_user_name.setText(userName)
                    ed_create_user_name.isEnabled = false
                    ed_create_user_display_name.setText(displayName.toDisplay(FontChoose.isUnicode()))
                    ed_create_user_password.setText(password)
                }
            }
            darkTheme()
        } else {
            lightTheme()
        }

        sp_create_user_role.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            viewModel.userRole = roleList[position].id
        }

        tv_footer.setOnClickListener {
            if (cl_login.isGone) {
                lightTheme()
            } else darkTheme()
        }

        ed_user_name.addTextChangedListener {
            viewModel.userName = ed_user_name.text.toString()
            if (it?.length ?: 0 > 0) edt_user_name.error = null
        }
        ed_user_password.addTextChangedListener {
            viewModel.userPassword = ed_user_password.text.toString()
            if (it?.length ?: 0 > 0) edt_user_password.error = null
        }
        ed_create_user_display_name.addTextChangedListener {
            viewModel.userDisplayName = ed_create_user_display_name.mText
            if (it?.length ?: 0 > 0) edt_create_user_display_name.error = null
        }
        ed_create_user_name.addTextChangedListener {
            viewModel.userName = ed_create_user_name.text.toString()
            if (it?.length ?: 0 > 0) edt_create_user_name.error = null
        }
        ed_create_user_password.addTextChangedListener {
            viewModel.userPassword = ed_create_user_password.text.toString()
            if (it?.length ?: 0 > 0) edt_create_user_password.error = null
        }

        btn_login.setOnClickListener {
            if (viewModel.userName.isEmpty()) edt_user_name.setError("Please enter user name").run { return@setOnClickListener }
            if (viewModel.userPassword.isEmpty()) edt_user_password.setError("Please enter password").run { return@setOnClickListener }
            with(viewModel.loginUser()) {
                if (this == null) {
                    loginDataClear()
                    it.longSnackBar("User name or password incorrect")
                } else {
                    longToast("Login success")
                }
            }
        }

        btn_create.setOnClickListener {
            if (viewModel.userDisplayName.isEmpty()) edt_create_user_display_name.setError(
                "Please enter display user name"
            ).run {
                return@setOnClickListener
            }
            if (viewModel.userName.isEmpty()) edt_create_user_name.setError("Please enter user name").run { return@setOnClickListener }
            if (viewModel.userPassword.isEmpty()) edt_create_user_password.setError(
                "Please enter user password"
            ).run { return@setOnClickListener }
            if (viewModel.userId == 0)
                viewModel.createUser {
                    if (it) btn_login.longSnackBar("Account create successful.")
                    else btn_login.longSnackBar("User name or password already exist.").run { createDataClear() }
                }
            else
                viewModel.updateUser {
                    Timber.d("update user success")
                }
        }
    }

    private fun loginDataClear() {
        ed_user_name.text?.clear()
        ed_user_password.text?.clear()
    }

    private fun createDataClear() {
        ed_create_user_display_name.text?.clear()
        ed_create_user_password.text?.clear()
        ed_create_user_name.text?.clear()
    }

    private fun darkTheme() {
        cl_login.gone()
        iv_create_account.invisible()
        iv_login_account.visible()
        cl_create.visible()
        tv_footer.text = getString(R.string.lbl_btn_login)
        loginDataClear()
    }

    private fun lightTheme() {
        cl_login.visible()
        iv_create_account.visible()
        iv_login_account.invisible()
        cl_create.gone()
        tv_footer.text = getString(R.string.lbl_create_account)
        createDataClear()
    }
}