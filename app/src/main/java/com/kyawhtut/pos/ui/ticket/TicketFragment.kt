package com.kyawhtut.pos.ui.ticket

import android.os.Bundle
import android.widget.EditText
import androidx.core.view.isGone
import androidx.lifecycle.observe
import com.kyawhtut.lib.rv.*
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseFragmentViewModel
import com.kyawhtut.pos.data.db.entity.CartWithHeader
import com.kyawhtut.pos.data.db.entity.customer
import com.kyawhtut.pos.data.vo.*
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.dialog_customer_choose.view.*
import kotlinx.android.synthetic.main.dialog_product_code_not_found.view.*
import kotlinx.android.synthetic.main.fragment_ticket.*
import kotlinx.android.synthetic.main.item_ticket_header_layout.view.*
import kotlinx.android.synthetic.main.item_ticket_item_layout.view.*
import kotlinx.android.synthetic.main.item_ticket_item_layout.view.tv_price
import kotlinx.android.synthetic.main.item_ticket_item_layout.view.tv_product_count
import kotlinx.android.synthetic.main.item_ticket_item_layout.view.tv_product_name
import kotlinx.android.synthetic.main.item_ticket_item_layout.view.tv_product_qty
import kotlinx.android.synthetic.main.item_ticket_total_layout.view.*
import kotlinx.android.synthetic.main.item_voucher.view.*
import org.angmarch.views.NiceSpinner
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class TicketFragment : BaseFragmentViewModel<TicketViewModel>(R.layout.fragment_ticket) {

    override val viewModel: TicketViewModel by viewModel()
    private var isClickHome: Boolean = false

    override fun onViewCreated(bundle: Bundle) {
        viewModel.getDraftCartList().observe(this) {
            rv_voucher.update(it.toMutableList())
        }

        viewModel.cartList.observe(this) {
            isClickHome = true
            rv_cart.update(it)
        }

        setHomeState(true, true)
        second.invisible()
        home.setOnClickListener {
            setHomeState(true, true)
            second.invisible()
            home()
        }

        btn_create_new_voucher.setOnClickListener {
            showCustomerSelectDialog()
        }

        fab_print.setOnClickListener {
            viewModel.insertOrder()
        }

        rv_voucher.bind(
            emptyList<CartWithHeader>(),
            R.layout.item_voucher
        ) { item, pos ->
            this.btn_delete.setOnClickListener {
                viewModel.deleteVoucherByTicketID(item.cartHeader.ticketId)
            }
            this.tv_ticket_title.mText =
                String.format("%s(%s)", item.cartHeader.ticketId, item.cartHeader.customerName)
            this.tv_sale_man.mText =
                String.format("%s/%s", item.cartHeader.saleManId, item.cartHeader.saleName)
            this.tv_ticket_count_price.mText =
                String.format("%s / %s", item.totalQty, item.getTotalNetAmount("Ks"))
            this.content_ticket.setOnClickListener {
                isClickHome = true
                home.setItem("Voucher", R.drawable.ic_gift_card_black, false, false)
                second.visible()
                second.setItem(item.cartHeader.ticketId, R.drawable.ic_gift_card_black, true, true)
                viewModel.getDraftCarListByTicketId(item.cartHeader.ticketId)
            }
        }.itemChange {
            if (it == 0) rv_voucher.gone() else rv_voucher.visible()
            if (it == 0 && viewModel._cartList.size == 0) gp_empty.visible() else gp_empty.gone()
            changeFabStatus()
        }
        rv_voucher.addItemDecoration(DividerItemDecoration(context!!, ignoreLastItem = true))

        rv_cart.bind(emptyList<PrintVO>())
            .map(
                R.layout.item_ticket_header_layout,
                { item, idx -> item.type is PrintType.HEADER }
            ) { item, pos ->
                val data = item.data as PrintHeader
                this.tv_customer_name.mText = data.customerName
                this.tv_customer_phone.mText = data.customerPhone
                this.tv_ticket_waiter.mText = String.format("%s/%s", data.waiterID, data.waiterName)
                this.tv_ticket_number.mText = data.ticketID
                this.tv_ticket_date.mText = getCurrentTimeString("dd-MM-yyyy h:m a")
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
                Timber.d("Update -> %s", data)
                this.tv_total_product_qty.text = "${data.totalQty}"
                this.tv_total_price.text = data.getTotalPrice("Ks")
                this.tv_tax_amount_title.mText = getString(R.string.lbl_tax_amount, data.tax)
                this.tv_total_tax_amount.text = data.getTaxAmount("Ks")
                this.tv_total_net_amount.text = data.getTotalNetAmount("Ks")
                this.tv_total_change_amount.text = data.getTotalChangeAmount("Ks")
                this.tv_total_paid_amount.text = data.getTotalPaidAmount("Ks")
                this.tv_total_discount_amount.text = data.getDiscountAmount("Ks")
                this.edt_discount_amount.setText(data.discountAmount.toString())
                this.edt_paid_amount.setText(data.paidAmount.toString())
                this.tv_total_paid_amount.setOnClickListener {
                    it.invisible()
                    this.edt_paid_amount.visible()
                }
                this.tv_total_discount_amount.setOnClickListener {
                    it.invisible()
                    this.edt_discount_amount.visible()
                }
                this.edt_paid_amount.setOnKeyListener { view, keyCode, event ->
                    if (this.edt_paid_amount.text.toString().isNotEmpty() && keyCode == 66) {
                        this.edt_paid_amount.invisible()
                        this.tv_total_paid_amount.visible()
                        data.paidAmount = this.edt_paid_amount.text.toString().toLong()
                        item.data = data
                        rv_cart.update(pos, item)
                        (view as EditText).hideKeyBoard()
                    }
                    false
                }
                this.edt_discount_amount.setOnKeyListener { view, keyCode, event ->
                    if (this.edt_discount_amount.text.toString().isNotEmpty() && keyCode == 66) {
                        this.edt_discount_amount.invisible()
                        this.tv_total_discount_amount.visible()
                        data.discountAmount = this.edt_discount_amount.text.toString().toLong()
                        item.data = data
                        rv_cart.update(pos, item)
                        (view as EditText).hideKeyBoard()
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
                    rv_cart.remove(item)
                    viewModel._cartList.remove(item)
                    updateTotalAmount()
                }
                this.edt_product_qty.setOnKeyListener { view, keyCode, event ->
                    if (this.edt_product_qty.text.toString().isNotEmpty() && keyCode == 66) {
                        this.edt_product_qty.gone()
                        this.tv_product_qty.visible()
                        data.qty = this.edt_product_qty.text.toString().toInt()
                        item.data = data
                        rv_cart.update(pos, item)
                        updateTotalAmount()
                        (view as EditText).hideKeyBoard()
                    }
                    false
                }
                this.tv_price.text = data.getTotalPrice("Ks")
            }.itemChange {
                if (!isClickHome)
                    viewModel.saveCacheCart()
                isClickHome = false
                if (it == 0) rv_cart.gone().run {
                    second.invisible()
                    setHomeState(true, true)
                } else rv_cart.visible()
                if (it == 0 && rv_voucher.size == 0) gp_empty.visible() else gp_empty.gone()
                changeFabStatus()
            }
    }

    private fun changeFabStatus() {
        fab_print.setImageResource(R.drawable.ic_printer_black)
        if (!gp_empty.isGone) fab_print.hide().run { return }
        fab_print.show()
        if (rv_cart.size > 0) return
        fab_print.setImageResource(R.drawable.ic_add_white)
    }

    fun addProduct(productCode: String) {
        with(viewModel.getProductIdByProductCode(productCode)) {
            Timber.d("Product code %s %s", productCode, this)
            if (this > 0) addProduct(this)
            else showNotFoundProductCode(productCode)
        }
    }

    private fun showNotFoundProductCode(productCode: String) {
        context?.showDialog(
            context.getInflateView(R.layout.dialog_product_code_not_found),
            {
                this.tv_message.mText = "ပစ္စည်း ကုဒ်နံပါတ် {$productCode} ကို ရှာဖွေလို့မတွေ့ပါ။"
            },
            false,
            true
        )
    }

    private fun showCustomerSelectDialog(productId: Int = -1) {
        var customerSpinner: NiceSpinner? = null
        val customerList = viewModel.getCustomerList()
        context?.showDialog(
            context.getInflateView(R.layout.dialog_customer_choose),
            {
                this.sp_customer.apply {
                    customerSpinner = this
                    customerList.map {
                        it.customerName
                    }.also {
                        attachDataSource(LinkedList(it))
                    }
                }
            },
            onClickPositive = {
                text = "OK"
                onClick = {
                    viewModel.customerEntity = customerList[customerSpinner?.selectedIndex ?: 0]
                    if (productId != -1) addProduct(productId)
                    it.dismiss()
                }
            }
        )
    }

    fun addProduct(productId: Int) {
        if (!viewModel.isAddedHeader) {
            if (viewModel.customerEntity.id == -1) {
                showCustomerSelectDialog(productId)
                return
            }
            viewModel.isAddedHeader = true
            viewModel.setProduct(0, printVO {
                type = PrintType.HEADER
                data = printHeader {
                    customerName = viewModel.customerEntity.customerName
                    customerPhone = viewModel.customerEntity.customerPhone
                    ticketID = viewModel.generateNewTicketID()
                    waiterID = viewModel.getCurrentUser()?.id ?: 0
                    waiterName = viewModel.getCurrentUser()?.displayName ?: ""
                }
            })
            setHomeState(false, false)
            setSecondState(viewModel.ticketID, true, true)
            rv_cart.add(
                viewModel._cartList.first()
            )
            viewModel.setProduct(1, printVO {
                type = PrintType.FOOTER
            })
            rv_cart.add(
                viewModel._cartList.last()
            )
        }
        with(viewModel.getProductById(productId)) {
            val pos =
                if (viewModel.isAddedTotalVO) viewModel._cartList.size - 2 else viewModel._cartList.size - 1
            viewModel.setProduct(
                pos,
                this
            )
            rv_cart.add(
                viewModel._cartList[pos],
                pos
            )
        }
        if (!viewModel.isAddedTotalVO) {
            viewModel.isAddedTotalVO = true
            with(printVO {
                type = PrintType.TOTAL
                data = printTotal {
                    totalAmount = viewModel.getTotalAmount()
                    tax = viewModel.taxAmount
                    paidAmount = 0
                    totalQty = viewModel.getTotalQty()
                }
            }) {
                val pos = viewModel._cartList.size - 1
                viewModel.setProduct(
                    pos,
                    this
                )
                rv_cart.add(
                    viewModel._cartList[pos],
                    pos
                )
            }
        } else {
            updateTotalAmount()
        }
    }

    private fun setHomeState(isHome: Boolean, isSelected: Boolean) {
        home.setItem("Voucher", R.drawable.ic_gift_card_black, isHome, isSelected)
    }

    private fun setSecondState(title: String, isHome: Boolean, isSelected: Boolean) {
        second.setItem(title, R.drawable.ic_gift_card_black, isHome, isSelected)
        if (isSelected) second.visible() else second.invisible()
    }

    private fun updateTotalAmount() {
        if (!viewModel.isHasItem) {
            clearData()
            return
        }
        val pos = viewModel._cartList.size - 2
        val oldData = viewModel._cartList[pos]
        var taxAmount: Int
        var paidAmount: Long
        var discountAmount: Long
        (oldData.data as PrintTotalVO).apply {
            taxAmount = tax
            paidAmount = this.paidAmount
            discountAmount = this.discountAmount
        }
        viewModel._cartList.removeAt(pos)
        viewModel._cartList.add(viewModel._cartList.size - 1, printVO {
            type = PrintType.TOTAL
            data = printTotal {
                tax = taxAmount
                this.paidAmount = paidAmount
                this.discountAmount = discountAmount
                totalAmount = viewModel.getTotalAmount()
                totalQty = viewModel.getTotalQty()
            }
        })
        rv_cart.update(
            pos,
            viewModel._cartList[pos]
        )
        if (!viewModel.isHasItem) {
            clearData()
        }
    }

    private fun home() {
        isClickHome = true
        clearData()
        rv_voucher.visible()
    }

    private fun clearData() {
        viewModel.customerEntity = customer {
            id = -1
        }
        viewModel.clearCartList()
        rv_cart.clear()
    }
}