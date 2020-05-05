package com.kyawhtut.pos.ui.customer

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toDisplay
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseDialogFragment
import com.kyawhtut.pos.utils.gone
import com.kyawhtut.pos.utils.putArg
import com.kyawhtut.pos.utils.visible
import kotlinx.android.synthetic.main.dialog_category_add.btn_delete
import kotlinx.android.synthetic.main.dialog_category_add.gp_delete_unavailable
import kotlinx.android.synthetic.main.dialog_customer_add.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CustomerDialog : BaseDialogFragment(R.layout.dialog_customer_add) {

    companion object {
        private const val extraCustomerId = "extra.customerID"

        fun show(fm: FragmentManager, id: Int = 0) {
            CustomerDialog().putArg(
                extraCustomerId to id
            ).show(fm, "CustomerDialog")
        }
    }

    private val viewModel: CustomerViewModel by viewModel()

    override fun setup(bundle: Bundle) {
        viewModel.cId = bundle.getInt(extraCustomerId)

        if (viewModel.cId != 0) {
            tv_dialog_title.mText = getString(R.string.lbl_customer_edit)
            with(viewModel.canDeleteCustomer()) {
                btn_delete.isEnabled = this
                if (this) gp_delete_unavailable.gone() else gp_delete_unavailable.visible()
            }
        } else {
            btn_delete.gone()
            gp_delete_unavailable.gone()
        }

        ed_customer_name.setText(viewModel.name.toDisplay(FontChoose.isUnicode()))
        ed_customer_address.setText(viewModel.address.toDisplay(FontChoose.isUnicode()))
        tag_phone.tagList = viewModel.phone
        cb_active_status.isChecked = viewModel.status

        cb_active_status.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.status = isChecked
        }

        ed_customer_name.addTextChangedListener {
            viewModel.name = ed_customer_name.mText
            if (viewModel.name.isNotEmpty()) edt_customer_name.error = ""
        }

        ed_customer_address.addTextChangedListener {
            viewModel.address = ed_customer_address.mText
            if (viewModel.address.isNotEmpty()) edt_customer_address.error = ""
        }

        btn_ok.setOnClickListener {
            if (viewModel.name.isEmpty()) edt_customer_name.setError("Enter customer name").run { return@setOnClickListener }
            if (viewModel.address.isEmpty()) edt_customer_address.setError("Enter customer name").run { return@setOnClickListener }
            viewModel.phone = tag_phone.tagList
            if (viewModel.cId == 0) viewModel.insert()
            else viewModel.update()
            dismiss()
        }

        btn_cancel.setOnClickListener {
            dismiss()
        }

        btn_delete.setOnClickListener {
            viewModel.delete()
            dismiss()
        }
    }
}
