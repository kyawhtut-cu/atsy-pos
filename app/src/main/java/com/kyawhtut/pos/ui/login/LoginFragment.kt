package com.kyawhtut.pos.ui.login

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragmentViewModel
import com.kyawhtut.pos.utils.longSnackBar
import com.kyawhtut.pos.utils.longToast
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragmentViewModel<LoginViewModel>(R.layout.fragment_login) {

    override val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(bundle: Bundle) {

        ed_user_name.addTextChangedListener {
            viewModel.userName = ed_user_name.text.toString()
            if (it?.length ?: 0 > 0) edt_user_name.error = null
        }
        ed_user_password.addTextChangedListener {
            viewModel.userPassword = ed_user_password.text.toString()
            if (it?.length ?: 0 > 0) edt_user_password.error = null
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
    }

    private fun loginDataClear() {
        ed_user_name.text?.clear()
        ed_user_password.text?.clear()
    }
}