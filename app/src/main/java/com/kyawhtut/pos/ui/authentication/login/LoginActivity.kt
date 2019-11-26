package com.kyawhtut.pos.ui.authentication.login

import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.db.entity.RoleEntity
import com.kyawhtut.pos.ui.base.BaseActivity
import com.kyawhtut.pos.ui.superadmin.FunctionActivity
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LoginActivity : BaseActivity<LoginViewModel>(
    R.layout.activity_login,
    checkLogin = false
) /*ServiceConnection*/ {

    companion object {
        const val extraLoginType = "extra.loginType"
    }

    override val viewModel: LoginViewModel by viewModel()
    private var roleList = mutableListOf<RoleEntity>()
    /*private var localWordService: LocalWordService? = null*/

    override fun setup(bundle: Bundle) {
        if (bundle.getBoolean(extraLoginType)) {
            darkTheme()
        } else {
            lightTheme()
        }
        /*startService<LocalWordService>()

        if (UStats.getUsageStatsList(this).isEmpty()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }*/

        viewModel.getRoleData().observe(this) {
            roleList.apply {
                clear()
                addAll(it)
                removeAt(0)
            }.map {
                it.roleName
            }.run {
                sp_create_user_role.attachDataSource(LinkedList(this))
            }
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
                    startActivity<FunctionActivity>()
                    finish()
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
            viewModel.createUser {
                if (it) btn_login.longSnackBar("Account create successful.").run { lightTheme() }
                else btn_login.longSnackBar("User name or password already exist.").run { createDataClear() }
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

    override fun onResume() {
        super.onResume()
        /*val service = intentFor<LocalWordService>()
        bindService(service, this, Context.BIND_AUTO_CREATE)

        Timber.d(
            "onResume -> %s",
            localWordService?.runningList
        )*/
    }

    override fun onPause() {
        super.onPause()
        /*unbindService(this)*/
    }

    /*override fun onServiceDisconnected(name: ComponentName?) {
        localWordService = null
        Timber.d("onServiceDisconnected")
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        val b: LocalWordService.MyBinder = service as LocalWordService.MyBinder
        localWordService = b.service
        Timber.d("onServiceConnected")
    }*/
}