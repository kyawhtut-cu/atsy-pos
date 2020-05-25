package com.kyawhtut.pos.ui.report

import android.os.Bundle
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toZawgyi
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_report.*

/**
 * @author kyawhtut
 * @date 15/05/2020
 */
class ReportFragment : BaseFragment(R.layout.fragment_report) {

    companion object {
        fun newInstance() = ReportFragment()
    }

    override fun onViewCreated(bundle: Bundle) {
        btn_report_one.setOnClickListener {
            showFeatureNotAvailable()
        }
        btn_report_two.setOnClickListener {
            showFeatureNotAvailable()
        }
        btn_report_three.setOnClickListener {
            showFeatureNotAvailable()
        }
        btn_report_four.setOnClickListener {
            showFeatureNotAvailable()
        }
        btn_report_five.setOnClickListener {
            showFeatureNotAvailable()
        }
    }

    private fun showFeatureNotAvailable() {
        Toasty.warning(requireContext(), "ယခု Version တွင်မရနိုင်သေးပါ။".run {
            if (FontChoose.isUnicode())
                this
            else this.toZawgyi()
        }, Toasty.LENGTH_LONG).show()
    }
}
