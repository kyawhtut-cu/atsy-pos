package com.kyawhtut.pos.ui.customer

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import com.amulyakhare.textdrawable.TextDrawable
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.button.MaterialButton
import com.kyawhtut.lib.rv.DividerItemDecoration
import com.kyawhtut.lib.rv.bind
import com.kyawhtut.lib.rv.update
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseBottomSheetDialog
import com.kyawhtut.pos.data.db.entity.TicketWithSell
import com.kyawhtut.pos.data.db.entity.toPrintVOList
import com.kyawhtut.pos.data.vo.PrintHeader
import com.kyawhtut.pos.data.vo.PrintTotalVO
import com.kyawhtut.pos.data.vo.PrintType
import com.kyawhtut.pos.data.vo.ProductSellVO
import com.kyawhtut.pos.utils.color.RandomColor
import com.kyawhtut.pos.utils.getInflateView
import com.kyawhtut.pos.utils.makeCall
import com.kyawhtut.pos.utils.putArg
import com.kyawhtut.pos.utils.showDialog
import kotlinx.android.synthetic.main.dialog_add_payment.view.*
import kotlinx.android.synthetic.main.dialog_customer_detail.*
import kotlinx.android.synthetic.main.item_ticket_content_view.view.*
import kotlinx.android.synthetic.main.item_ticket_header_layout.view.*
import kotlinx.android.synthetic.main.item_ticket_total_layout.view.*
import net.cachapa.expandablelayout.ExpandableLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author kyawhtut
 * @date 07/05/2020
 */
class CustomerDetailDialog private constructor() :
    BaseBottomSheetDialog(R.layout.dialog_customer_detail) {

    companion object {
        private const val extraCustomerID = "extra.customerID"
        private const val UNSELECTED = -1

        fun show(cID: Int, fm: FragmentManager) {
            CustomerDetailDialog().putArg(
                extraCustomerID to cID
            ).show(fm, CustomerDetailDialog::class.java.name)
        }
    }

    private val viewModel by viewModel<CustomerViewModel>()
    private var selectedItem: Int = UNSELECTED
    private var payAmount: Int = 0

    override fun onViewCreated() {

        btn_close.setOnClickListener {
            dismissAllowingStateLoss()
        }

        btn_add_payment.setOnClickListener {
            requireContext().showDialog(
                requireContext().getInflateView(R.layout.dialog_add_payment),
                bindView = {
                    this.edt_pay_amount.apply {
                        addTextChangedListener {
                            payAmount = (this.text?.toString()
                                .takeIf { it != "0" && !it.isNullOrEmpty() } ?: "0").toInt()
                        }
                    }
                },
                onClickPositive = {
                    text = getString(R.string.lbl_btn_ok)
                    onClick = {
                        viewModel.addPayAmount(get(extraCustomerID, 0), payAmount)
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
        }

        view_scroll.setOnScrollChangeListener { view, x, y, oldX, oldY ->
            if (y > oldY) {
                btn_add_payment.shrink()
            } else {
                btn_add_payment.extend()
            }
        }

        rv_customer_phone.apply {
            bind(viewModel.phone, R.layout.item_customer_phone) { phone, _ ->
                (this as MaterialButton).apply {
                    text = phone
                    setOnClickListener {
                        requireContext().makeCall(phone)
                    }
                }
            }.layoutManager(
                FlexboxLayoutManager(
                    requireContext(),
                    FlexDirection.ROW,
                    FlexWrap.WRAP
                ).apply {
                    alignItems = AlignItems.CENTER
                })
        }

        rv_customer_ticket.apply {
            isNestedScrollingEnabled = false
            bind(
                emptyList<TicketWithSell>(),
                R.layout.item_ticket_content_view
            ) { ticketSell, pos ->
                this.tv_ticket_id.text = ticketSell.ticketEntities.ticketId
                this.tv_ticket_open_date.text = ticketSell.ticketEntities.createdDate
                this.tv_ticket_id.setOnClickListener {
                    val viewHolder = this@apply.findViewHolderForAdapterPosition(selectedItem)
                    viewHolder?.let {
                        viewHolder.itemView.view_expandable.collapse()
                        viewHolder.itemView.dropdown_menu.setImageResource(R.drawable.ic_keyboard_arrow_down_black)
                    }
                    selectedItem = if (pos == selectedItem) {
                        UNSELECTED
                    } else {
                        this.dropdown_menu.setImageResource(R.drawable.ic_keyboard_arrow_up_white)
                        this.view_expandable.expand()
                        pos
                    }
                }
                this.view_expandable.setOnExpansionUpdateListener { _, state ->
                    if (state == ExpandableLayout.State.EXPANDED) {
                        this@apply.smoothScrollToPosition(pos)
                    }
                }
                this.dropdown_menu.setOnClickListener {
                    this.tv_ticket_id.performClick()
                }
                this.rv_sell_item.apply {
                    isNestedScrollingEnabled = false
                    addItemDecoration(
                        DividerItemDecoration(
                            requireContext(),
                            ignoreLastItem = true,
                            padding = 0
                        )
                    )
                    bind(ticketSell.sellItemList.toPrintVOList(ticketSell.ticketEntities))
                        .map(
                            R.layout.item_ticket_customer_header_item,
                            { item, _ -> item.type is PrintType.HEADER }
                        ) { item, _ ->
                            val data = item.data as PrintHeader
                            this.tv_ticket_waiter.mText =
                                String.format("%s/%s", data.waiterID, data.waiterName)
                            this.tv_ticket_number.mText = data.ticketID
                            this.tv_ticket_date.mText = data.ticketDate
                        }
                        .map(
                            R.layout.item_ticket_customer_total_item,
                            { item, _ -> item.type is PrintType.TOTAL }
                        ) { item, _ ->
                            val data = item.data as PrintTotalVO
                            this.tv_total_product_qty.text = "${data.totalQty}"
                            this.tv_total_price.text = data.getTotalPrice("Ks")
                            this.tv_tax_amount_title.mText =
                                getString(R.string.lbl_tax_amount, data.taxAmount)
                            this.tv_total_tax_amount.text = data.getTaxAmount(data.taxAmount, "Ks")
                            this.tv_total_net_amount.text =
                                data.getTotalNetAmount(viewModel.taxAmount, "Ks")
                            this.tv_total_change_amount.text =
                                data.getTotalChangeAmount(data.taxAmount, "Ks")
                            this.tv_total_paid_amount.text = data.getTotalPaidAmount("Ks")
                            this.tv_total_discount_amount.text = data.getDiscountAmount("Ks")
                        }
                        .map(
                            R.layout.item_ticket_customer_sell_item,
                            { item, _ -> item.type is PrintType.ITEMS }
                        ) { item, pos ->
                            val data = item.data as ProductSellVO
                            this.tv_product_count.text = "${data.count}"
                            this.tv_product_name.mText = data.name
                            this.tv_product_qty.text = "${data.qty}"
                            this.tv_price.text = data.getTotalPrice("Ks")
                        }
                }
            }
        }

        viewModel.getCustomerDetail(get(extraCustomerID, 0)).observe(this) {
            iv_customer_image.setImageDrawable(
                TextDrawable.builder()
                    .buildRound(
                        it.customer.customerName.substring(0, 1),
                        RandomColor().randomColor()
                    )
            )
            rv_customer_phone.update(it.customer.customerPhone.split(",").toMutableList())
            tv_customer_debt.text = "%s - %s %s".format(
                getString(R.string.lbl_debt),
                it.debt,
                getString(R.string.lbl_kyat)
            )
            tv_customer_address.text = "%s - %s".format(
                getString(R.string.lbl_hint_user_address),
                it.customer.customerAddress
            )
            tv_customer_name.text = "%s - %s".format(
                getString(R.string.lbl_hint_user_display_name),
                it.customer.customerName
            )
            rv_customer_ticket.update(it.ticketWithSell.toMutableList())
        }
    }
}
