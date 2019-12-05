package com.kyawhtut.pos.ui.user

import android.os.Bundle
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserActivity : BaseActivity<UserViewModel>(R.layout.activity_user) {

    override val viewModel: UserViewModel by viewModel()

    override fun setup(bundle: Bundle) {
    }
}