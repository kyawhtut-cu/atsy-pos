package com.kyawhtut.pos.ui.user

import android.os.Bundle
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.adapter.UserTableAdapter
import com.kyawhtut.pos.ui.base.BaseActivity
import com.kyawhtut.pos.utils.listeners.MyTableViewListener
import kotlinx.android.synthetic.main.activity_user.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserActivity : BaseActivity<UserViewModel>(R.layout.activity_user) {

    override val viewModel: UserViewModel by viewModel()

    override fun setup(bundle: Bundle) {

        val adapter = UserTableAdapter(this)

        content_container.adapter = adapter
        adapter.setData()

        content_container.tableViewListener = MyTableViewListener(content_container)
    }
}