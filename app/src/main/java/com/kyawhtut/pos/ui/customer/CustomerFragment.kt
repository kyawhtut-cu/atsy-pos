package com.kyawhtut.pos.ui.customer

import android.os.Bundle
import androidx.lifecycle.observe
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.button.MaterialButton
import com.kyawhtut.lib.rv.DividerItemDecoration
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.update
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragment
import com.kyawhtut.pos.data.db.entity.CustomerTable
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.fragment_customer.*
import kotlinx.android.synthetic.main.item_customer.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author kyawhtut
 * @date 06/05/2020
 */
class CustomerFragment : BaseFragment(R.layout.fragment_customer) {

    private val viewModel: CustomerViewModel by viewModel()

    override fun onViewCreated(bundle: Bundle) {

        viewModel.getCustomerList().observe(this) {
            rv_customer.update(it.toMutableList())
        }

        rv_customer.apply {
            bind(emptyList(), CustomerTable.diffUtil, R.layout.item_customer) { item, pos ->
                this.view_content.setOnClickListener {
                    CustomerDetailDialog.show(item.customer.id, childFragmentManager)
                }
                this.tv_customer_name.text = "%s - %s".format(
                    getString(R.string.lbl_hint_user_display_name),
                    item.customer.customerName
                )
                this.tv_customer_debt.text =
                    "%s - %s %s".format(
                        getString(R.string.lbl_debt),
                        (item.ticketEntities.sumByDouble { it.totalPrice.toDouble() } - item.ticketEntities.sumByDouble { it.payAmount.toDouble() } - item.customer.customerDebit).toThousandSeparator(),
                        getString(R.string.lbl_kyat)
                    )
                this.btn_edit.setOnClickListener {
                    CustomerDialog.show(parentFragmentManager, item.customer.id)
                }
                this.btn_delete.setOnClickListener {
                    if (viewModel.canDeleteCustomer(item.customer.id)) {
                        requireContext().showDialog(
                            getString(R.string.lbl_delete_title),
                            getString(R.string.lbl_delete_message, item.customer.customerName),
                            onClickPositive = {
                                text = getString(R.string.lbl_btn_ok)
                                onClick = {
                                    viewModel.delete(item.customer.id)
                                    it.dismiss()
                                }
                            },
                            onClickNegative = {
                                text = getString(R.string.lbl_btn_cancel)
                                onClick = {
                                    it.dismiss()
                                }
                            }
                        )
                    } else {
                        requireContext().showDialog(
                            getString(R.string.lbl_delete_fail_title),
                            getString(R.string.lbl_delete_fail_message, item.customer.customerName),
                            onClickPositive = {
                                text = getString(R.string.lbl_btn_ok)
                                onClick = {
                                    it.dismiss()
                                }
                            }
                        )
                    }
                }
                this.rv_customer_phone.apply {
                    isNestedScrollingEnabled = false
                    bind(
                        item.customer.customerPhone.split(","),
                        R.layout.item_customer_phone
                    ) { item, pos ->
                        with(this as MaterialButton) {
                            text = item
                            setOnClickListener {
                                requireContext().makeCall(item)
                            }
                        }
                    }.layoutManager(
                        FlexboxLayoutManager(
                            requireContext(),
                            FlexDirection.ROW,
                            FlexWrap.WRAP
                        ).apply {
                            alignItems = AlignItems.CENTER
                        }
                    )
                }
            }.itemChange {
                if (it == 0) empty_view.visible() else empty_view.gone()
            }
            addItemDecoration(DividerItemDecoration(requireContext(), ignoreLastItem = true))
        }
    }
}
