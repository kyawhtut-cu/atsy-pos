package com.kyawhtut.pos.ui.sale

import android.os.Bundle
import androidx.fragment.app.commit
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragmentViewModel
import com.kyawhtut.pos.ui.ticket.TicketFragment
import com.kyawhtut.pos.ui.category.CategoryFragment
import com.kyawhtut.pos.ui.home.HomeActivity
import moe.feng.common.view.breadcrumbs.model.BreadcrumbItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class SaleFragment : BaseFragmentViewModel<SaleViewModel>(R.layout.fragment_sale) {

    override val viewModel: SaleViewModel by viewModel()
    private val categoryFragment: CategoryFragment = CategoryFragment()

    private val ticketFragment: TicketFragment =
        TicketFragment()

    override fun onViewCreated(bundle: Bundle) {
        categoryFragment.itemClick = {
            (activity as HomeActivity).nextIndex(it)
        }
        categoryFragment.addProduct = {
            ticketFragment.addProduct(it)
        }

        parentFragmentManager.commit {
            replace(R.id.left_panel, categoryFragment)
            replace(R.id.right_panel, ticketFragment)
        }
    }

    fun changeCategoryFragment(item: BreadcrumbItem, pos: Int) {
        categoryFragment.changeData(item, pos)
    }

    fun filter(query: String) {
        categoryFragment.filter(query)
    }
}
