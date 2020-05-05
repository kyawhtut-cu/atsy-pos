package com.kyawhtut.pos.ui.welcome

import android.os.Bundle
import android.os.Handler
import com.kyawhtut.fontchooserlib.util.FontUtils
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseActivity
import com.kyawhtut.pos.phone.home.PhoneHomeActivity
import com.kyawhtut.pos.ui.home.HomeActivity
import com.kyawhtut.pos.utils.isTablet
import com.kyawhtut.pos.utils.startActivity

class WelcomeActivity : BaseActivity(R.layout.activity_welcome) {

    override fun setup(savedInstanceState: Bundle?, bundle: Bundle) {
        if (!FontUtils.Builder(this).build().isFirst) {
            Handler().postDelayed(
                {
                    finish()
                    if (isTablet()) startActivity<HomeActivity>()
                    else startActivity<PhoneHomeActivity>()
                },
                500
            )
        }
    }
}
