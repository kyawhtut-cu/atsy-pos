package com.kyawhtut.pos.ui.ticket

import android.os.Bundle
import com.kyawhtut.lib.rv.*
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragment
import com.kyawhtut.pos.data.vo.*
import com.kyawhtut.pos.utils.gone
import com.kyawhtut.pos.utils.invisible
import com.kyawhtut.pos.utils.popupDialogBuilder
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.item_ticket_item_layout.view.*
import kotlinx.android.synthetic.main.item_ticket_total_layout.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TicketFragment : BaseFragment(R.layout.fragment_ticket) {

    private val viewModel: TicketViewModel by viewModel()

    private val popupDialog = popupDialogBuilder {
        popupItemList {
            popupItem {
                title = "Print"
                icon = R.drawable.ic_search_black_24dp
            }
            popupItem {
                title = "Cancel"
                icon = R.drawable.ic_search_black_24dp
            }
        }
        callback = {

        }
    }

    override fun onViewCreated(bundle: Bundle) {
        iv_menu.setOnClickListener {
            popupDialog.apply {
                view = iv_menu
                bind().show()
            }
        }

        rv_sell_item.bind(viewModel.cartList)
            .map(
                R.layout.item_ticket_header_layout,
                { item, idx -> item.type is PrintType.HEADER }
            ) { item, pos ->
            }
            .map(
                R.layout.item_ticket_footer_layout,
                { item, idx -> item.type is PrintType.FOOTER }
            ) { item, pos ->
            }
            .map(
                R.layout.item_ticket_total_layout,
                { item, idx -> item.type is PrintType.TOTAL }
            ) { item, pos ->
                val data = item.data as PrintTotalVO
                this.tv_total_product_qty.text = "${data.totalQty}"
                this.tv_total_price.text = data.getTotalPrice("Ks")
                this.tv_total_tax_amount.text = data.getTaxAmount("Ks")
                this.tv_total_net_amount.text = data.getTotalNetAmount("Ks")
                this.tv_total_change_amount.text = data.getTotalChangeAmount("Ks")
                this.tv_total_paid_amount.text = data.getTotalPaidAmount("Ks")
                this.tv_total_paid_amount.setOnClickListener {
                    it.invisible()
                    this.edt_paid_amount.visible()
                }
                this.edt_paid_amount.setOnKeyListener { _, keyCode, event ->
                    if (this.edt_paid_amount.text.toString().isNotEmpty() && keyCode == 66) {
                        this.edt_paid_amount.invisible()
                        this.tv_total_paid_amount.visible()
                        data.paidAmount = this.edt_paid_amount.text.toString().toLong()
                        item.data = data
                        rv_sell_item.update(pos, item)
                    }
                    false
                }
            }
            .map(R.layout.item_ticket_item_layout,
                { item, idx -> item.type is PrintType.ITEMS }
            ) { item, pos ->
                val data = item.data as ProductSellVO
                this.tv_product_count.text = "${data.count}"
                this.tv_product_name.mText = data.name
                this.tv_product_qty.text = "${data.qty}"
                this.edt_product_qty.setText("${data.qty}")
                this.tv_product_qty.setOnClickListener {
                    it.gone()
                    this.edt_product_qty.visible()
                }
                this.btn_product_delete.setOnClickListener {
                    rv_sell_item.remove(item)
                    viewModel.cartList.removeAt(pos)
                    updateTotalAmount()
                }
                this.edt_product_qty.setOnKeyListener { _, keyCode, event ->
                    if (this.edt_product_qty.text.toString().isNotEmpty() && keyCode == 66) {
                        this.edt_product_qty.gone()
                        this.tv_product_qty.visible()
                        data.qty = this.edt_product_qty.text.toString().toInt()
                        item.data = data
                        rv_sell_item.update(pos, item)
                        updateTotalAmount()
                    }
                    false
                }
                this.tv_price.text = data.getTotalPrice("Ks")
            }
    }

    fun addProduct(productId: Int) {
        if (!viewModel.isAddedHeader) {
            viewModel.isAddedHeader = true
            rv_sell_item.add(
                printVO {
                    type = PrintType.HEADER
                }
            )
            viewModel.setProduct(0, printVO {
                type = PrintType.HEADER
            })
            rv_sell_item.add(
                printVO {
                    type = PrintType.FOOTER
                }
            )
            viewModel.setProduct(1, printVO {
                type = PrintType.FOOTER
            })
        }
        with(viewModel.getProductById(viewModel.productCount++, productId)) {
            rv_sell_item.add(
                this,
                if (viewModel.isAddedTotalVO) rv_sell_item.size - 2 else rv_sell_item.size - 1
            )
            viewModel.setProduct(
                if (viewModel.isAddedTotalVO) viewModel.cartList.size - 2 else viewModel.cartList.size - 1,
                this
            )
        }
        if (!viewModel.isAddedTotalVO) {
            viewModel.isAddedTotalVO = true
            with(printVO {
                type = PrintType.TOTAL
                data = printTotal {
                    totalAmount = viewModel.getTotalAmount()
                    tax = 5
                    paidAmount = 0
                    totalQty = viewModel.getTotalQty()
                }
            }) {
                rv_sell_item.add(
                    this,
                    rv_sell_item.size - 1
                )

                viewModel.setProduct(
                    viewModel.cartList.size - 1,
                    this
                )
            }
        } else {
            updateTotalAmount()
        }
    }

    private fun updateTotalAmount() {
        val oldData = rv_sell_item.get<PrintVO>(rv_sell_item.size - 2)
        var taxAmount: Int
        var paidAmount: Long
        (oldData.data as PrintTotalVO).apply {
            taxAmount = tax
            paidAmount = this.paidAmount
        }
        rv_sell_item.update(
            rv_sell_item.size - 2,
            printVO {
                type = PrintType.TOTAL
                data = printTotal {
                    tax = taxAmount
                    this.paidAmount = paidAmount
                    totalAmount = viewModel.getTotalAmount(rv_sell_item.getItems())
                    totalQty = viewModel.getTotalQty(rv_sell_item.getItems())
                }
            }
        )
        viewModel.cartList.removeAt(viewModel.cartList.size - 2)
        viewModel.cartList.add(viewModel.cartList.size - 1, printVO {
            type = PrintType.TOTAL
            data = printTotal {
                tax = taxAmount
                this.paidAmount = paidAmount
                totalAmount = viewModel.getTotalAmount(rv_sell_item.getItems())
                totalQty = viewModel.getTotalQty(rv_sell_item.getItems())
            }
        })
        if (viewModel.getTotalAmount() == 0L) {
            viewModel.clearCartList()
            rv_sell_item.clear()
        }
    }

    fun isOrder() = rv_sell_item.size != 0
}