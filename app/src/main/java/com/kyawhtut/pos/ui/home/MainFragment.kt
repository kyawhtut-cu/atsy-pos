package com.kyawhtut.pos.ui.home

import android.os.Bundle
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toZawgyi
import com.kyawhtut.lib.rv.DividerItemDecoration
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.update
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragment
import com.kyawhtut.pos.utils.gone
import com.kyawhtut.pos.utils.putArg
import com.kyawhtut.pos.utils.toThousandSeparator
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_ticket.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    private val viewModel by viewModel<MainViewModel>()

    override fun onViewCreated(bundle: Bundle) {
        btn_go_to_login.isEnabled = !bundle.getBoolean(extraLoginStatus)
        if (!bundle.getBoolean(extraLoginStatus)) {
            view_home.gone()
            view_login.visible()
            return
        }
        view_home.visible()
        view_login.gone()

        tv_total_sale_count.text = viewModel.totalTicketCount
        tv_total_item_count.text = viewModel.totalProductCount
        tv_total_customer_count.text = viewModel.totalCustomerCount
        tv_total_item_remain_count.text = viewModel.totalWarningCount

        sp_ticket_filter_type.apply {
            attachDataSource(resources.getStringArray(R.array.filter_array).toList().run {
                if (FontChoose.isUnicode()) this
                else this.toZawgyi()
            })
            setOnSpinnerItemSelectedListener { _, _, _, _ ->
                rv_ticket.update(viewModel.getTicket(selectedItem as String).toMutableList())
            }
        }

        rv_ticket.apply {
            bind(viewModel.getTicket("All"), R.layout.item_ticket) { item, pos ->
                this.tv_ticket_date.text = item.createdDate
                this.tv_ticket_id.text = item.ticketId
                this.tv_ticket_total_amount.text = "%s %s".format(
                    item.totalPrice.toThousandSeparator(),
                    getString(R.string.lbl_kyat)
                )
            }.itemChange {
                if (it == 0) empty_view_ticket.visible()
                else empty_view_ticket.gone()
            }
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    ignoreLastItem = true,
                    padding = 16
                )
            )
        }

        view_graph_for_product.apply {
            with(viewModel.mostSellingProductList) {
                setData(first, second, third)
            }
        }

        btn_go_to_login.setOnClickListener {
            (activity as HomeActivity).goToLogin()
        }
    }
}
