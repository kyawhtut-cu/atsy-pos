package com.kyawhtut.pos.ui.welcome

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.observe
import com.kyawhtut.fontchooserlib.util.FontUtils
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivity
import com.kyawhtut.pos.phone.home.PhoneHomeActivity
import com.kyawhtut.pos.ui.home.HomeActivity
import com.kyawhtut.pos.utils.gone
import com.kyawhtut.pos.utils.isTablet
import com.kyawhtut.pos.utils.startActivity
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.activity_welcome.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : BaseActivity(R.layout.activity_welcome) {

    private val viewModel by viewModel<WelcomeViewModel>()

    override fun setup(savedInstanceState: Bundle?, bundle: Bundle) {
        if (!FontUtils.Builder(this).build().isFirst) {
            if (!viewModel.isDeviceCheck) {
                loading_view.gone()
                gp_check_device.visible()
                gp_error.gone()
            } else {
                viewModel.check()
            }
        }

        viewModel.getState().observe(this) {
            when (it) {
                is WelcomeViewModel.State.LOADING -> {
                    loading_view.visible()
                    gp_check_device.gone()
                    gp_error.gone()
                }
                is WelcomeViewModel.State.FINISH -> {
                    finish()
                    if (isTablet()) startActivity<HomeActivity>()
                    else startActivity<PhoneHomeActivity>()
                }
                is WelcomeViewModel.State.ERROR -> {
                    loading_view.gone()
                    gp_check_device.gone()
                    gp_error.visible()
                    tv_error_message.text = "%s%s%s".format(it.status, "\n", it.error)
                }
            }
        }

        ed_phone_number.setText(viewModel.phone)
        btn_check.isEnabled = viewModel.isDeviceCheck

        ed_phone_number.addTextChangedListener {
            btn_check.isEnabled = ed_phone_number.text.toString().isNotEmpty()
            viewModel.phone = it.toString().takeIf { it.isNotEmpty() } ?: ""
        }

        btn_check.setOnClickListener {
            viewModel.check()
        }

        btn_retry.setOnClickListener {
            loading_view.gone()
            gp_check_device.visible()
            gp_error.gone()
        }
    }
}
