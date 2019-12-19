package com.kyawhtut.pos.ui.home

import android.os.Bundle
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragment
import com.kyawhtut.pos.utils.putArg
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment private constructor() : BaseFragment(R.layout.fragment_main) {

    companion object {
        private const val extraLoginStatus = "extra.loginStatus"

        fun createInstance(isLogin: Boolean = false): MainFragment {
            return MainFragment().apply {
                putArg(
                    extraLoginStatus to isLogin
                )
            }
        }
    }

    override fun onViewCreated(bundle: Bundle) {
        btn_go_to_login.isEnabled = !bundle.getBoolean(extraLoginStatus)

        btn_go_to_login.setOnClickListener {
            (activity as HomeActivity).goToLogin()
        }
    }
}