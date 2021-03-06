package com.kyawhtut.pos.ui.user

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toDisplay
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseDialogFragment
import com.kyawhtut.pos.data.db.AppDatabase
import com.kyawhtut.pos.data.db.entity.RoleEntity
import com.kyawhtut.pos.ui.login.LoginViewModel
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.dialog_user_add.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class UserAddDialog private constructor() : BaseDialogFragment(R.layout.dialog_user_add, true) {

    companion object {
        private const val extraUserID = "extra.userID"

        fun show(fm: FragmentManager, userID: Int = 0) {
            UserAddDialog().apply {
                putArg(
                    extraUserID to userID
                )
            }.show(fm, UserAddDialog::class.java.name)
        }
    }

    private val viewModel: LoginViewModel by viewModel()
    private val db: AppDatabase by inject()
    private var roleList = mutableListOf<RoleEntity>()

    override fun setup(bundle: Bundle) {

        viewModel.getRoleData().observe(this) {
            roleList.apply {
                clear()
                addAll(it)
            }.map {
                it.roleName
            }.run {
                sp_create_user_role.attachDataSource(LinkedList(this))
                with(roleList.indexOfFirst { it.id == viewModel.userRole }) {
                    if (this != -1) sp_create_user_role.selectedIndex = this
                    else viewModel.userRole = roleList.first().id
                }
            }
        }

        with(bundle.getInt(extraUserID)) {
            if (this != 0) {
                tv_dialog_title.mText = getString(R.string.lbl_user_edit)
                with(viewModel.canDeleteUserById(this)) {
                    btn_delete.isEnabled = this
                    if (this) gp_delete_unavailable.gone() else gp_delete_unavailable.visible()
                }
                viewModel.getUserById(this).run {
                    viewModel.userRole = roleId
                    viewModel.userDisplayName = displayName
                    viewModel.userName = userName
                    viewModel.createdDate = createdDate
                    viewModel.createdUserId = createdUserId
                    viewModel.userPassword = password
                    viewModel.userId = id
                    ed_create_user_name.setText(userName)
                    ed_create_user_name.isEnabled = false
                    ed_create_user_display_name.setText(displayName.toDisplay(FontChoose.isUnicode()))
                    ed_create_user_password.setText(password)
                }
            } else {
                btn_delete.gone()
                gp_delete_unavailable.gone()
            }
        }

        sp_create_user_role.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            viewModel.userRole = roleList[position].id
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

        btn_delete.setOnClickListener {
            viewModel.deleteUser()
            dismiss()
        }

        btn_ok.setOnClickListener {
            if (viewModel.userDisplayName.isEmpty()) edt_create_user_display_name.setError(
                "Please enter display user name"
            ).run {
                return@setOnClickListener
            }
            if (viewModel.userName.isEmpty()) edt_create_user_name.setError("Please enter user name")
                .run { return@setOnClickListener }
            if (viewModel.userPassword.isEmpty()) edt_create_user_password.setError(
                "Please enter user password"
            ).run { return@setOnClickListener }
            if (viewModel.userId == 0)
                viewModel.createUser {
                    if (it) btn_ok.longSnackBar("Account create successful.").run { dismiss() }
                    else btn_ok.longSnackBar("User name or password already exist.")
                        .run { createDataClear() }
                }
            else
                viewModel.updateUser {
                    Timber.d("update user success")
                    dismiss()
                }
        }

        btn_cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun createDataClear() {
        ed_create_user_display_name.text?.clear()
        ed_create_user_password.text?.clear()
        ed_create_user_name.text?.clear()
    }
}