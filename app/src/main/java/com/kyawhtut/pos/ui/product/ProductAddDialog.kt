package com.kyawhtut.pos.ui.product

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.card.MaterialCardView
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toDisplay
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseDialogFragment
import com.kyawhtut.pos.data.db.entity.CategoryEntity
import com.kyawhtut.pos.ui.scanner.ScannerActivity
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.dialog_product_add.*
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import me.jfenn.colorpickerdialog.views.picker.ImagePickerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ProductAddDialog : BaseDialogFragment(R.layout.dialog_product_add, true) {

    companion object {
        private const val extraProductId = "extra.productId"
        private const val extraScannerRequestCode = 0x01

        fun show(fm: FragmentManager, productId: Int = 0) {
            ProductAddDialog().putArg(
                extraProductId to productId
            ).show(fm, "ProductAddDialog")
        }
    }

    private val viewModel: ProductViewModel by viewModel()
    private val categoryList = mutableListOf<CategoryEntity>()

    override fun setup(bundle: Bundle) {
        viewModel.color = context.getColorValue(R.color.colorAccent)
        viewModel.textColor = context.getColorValue(R.color.colorWhite)
        viewModel.productId = bundle.getInt(extraProductId)

        viewModel.getCategoryList().also {
            if (it.isEmpty()) dismiss().run {
                context.longToast("Please add new category before add new product.")
                return
            }
            categoryList.clear()
            categoryList.addAll(it)
            viewModel.cId = it.first().id
            categoryList.map {
                it.categoryName.toDisplay(FontChoose.isUnicode())
            }.run {
                sp_category.attachDataSource(LinkedList(this))
            }
        }

        ed_product_name.setText(viewModel.name.toDisplay(FontChoose.isUnicode()))
        ed_product_description.setText(viewModel.description.toDisplay(FontChoose.isUnicode()))
        ed_product_retail_price.setText("${viewModel.retailPrice}")
        ed_product_price.setText("${viewModel.price}")
        ed_product_count.setText("${viewModel.count}")
        cv_product_color.setCardBackgroundColor(viewModel.color)
        cv_product_text_color.setCardBackgroundColor(viewModel.textColor)
        cb_active_status.isChecked = viewModel.status
        sw_notification.isChecked = viewModel.remainAmountShow

        sw_notification.text =
            String.format(sw_notification.text.toString(), viewModel.limitAmount)

        if (viewModel.productId != 0) {
            tv_dialog_title.mText = getString(R.string.lbl_product_edit)
            sp_category.selectedIndex = categoryList.indexOfFirst { it.id == viewModel.cId }
        }

        cb_active_status.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.status = isChecked
        }

        edt_product_code.setEndIconOnClickListener {
            askPermission(Manifest.permission.CAMERA) {
                startActivityForResult<ScannerActivity>(extraScannerRequestCode)
            }.onDeclined {
                if (it.hasDenied()) {
                    context.showDialog(
                        message = "Please accept camera permission",
                        isCancelable = false,
                        onClickPositive = {
                            text = "Ok"
                            onClick = { _ ->
                                it.askAgain()
                            }
                        }
                    )
                }
                if (it.hasForeverDenied()) {
                    context.showDialog(
                        message = "Please open camera permission in application setting.",
                        onClickPositive = {
                            text = "Ok"
                            onClick = {
                                context.openPermission()
                            }
                        },
                        onClickNegative = {
                            text = "Cancel"
                            onClick = {
                                it.dismiss()
                            }
                        }
                    )
                }
            }
        }

        sw_notification.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.remainAmountShow = isChecked
        }

        sp_category.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            viewModel.cId = categoryList[position].id
        }

        ed_product_name.addTextChangedListener {
            viewModel.name = ed_product_name.mText
            if (ed_product_name.text.toString().isNotEmpty()) edt_product_name.error = ""
        }
        ed_product_description.addTextChangedListener {
            viewModel.description = ed_product_description.mText
        }

        ed_product_retail_price.addTextChangedListener {
            if (ed_product_retail_price.text.toString().isNotEmpty()) {
                viewModel.retailPrice = ed_product_retail_price.text.toString().toLong()
                edt_product_retail_price.error = ""
            }
        }

        ed_product_code.addTextChangedListener {
            viewModel.code = ed_product_code.mText
        }

        ed_product_price.addTextChangedListener {
            if (ed_product_price.text.toString().isNotEmpty()) {
                viewModel.price = ed_product_price.text.toString().toLong()
            }
        }

        ed_product_count.addTextChangedListener {
            if (ed_product_count.text.toString().isNotEmpty()) {
                viewModel.count = ed_product_count.text.toString().toInt()
            }
        }

        cv_product_color.setOnClickListener {
            showColorPickerDialog(
                cv_product_color,
                tv_product_color.mText.toString(),
                viewModel.color
            )
        }

        cv_product_text_color.setOnClickListener {
            showColorPickerDialog(
                cv_product_text_color,
                tv_product_text_color.mText.toString(),
                viewModel.textColor
            )
        }

        if (viewModel.productId != 0) {
            with(viewModel.canDeleteProduct()) {
                btn_delete.isEnabled = this
                if (this) gp_delete_unavailable.gone() else gp_delete_unavailable.visible()
            }
        } else {
            btn_delete.gone()
            gp_delete_unavailable.gone()
        }

        btn_delete.setOnClickListener {
            viewModel.deleteProductById()
            dismiss()
        }

        btn_ok.setOnClickListener {
            if (viewModel.name.isEmpty()) edt_product_name.setError("Please enter category name").run { return@setOnClickListener }
            if (viewModel.retailPrice == 0L) edt_product_retail_price.setError("Please enter retail price").run { return@setOnClickListener }
            if (viewModel.productId == 0) viewModel.insert() else viewModel.update()
            dismiss()
        }

        btn_cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun showColorPickerDialog(
        view: MaterialCardView,
        title: String,
        default: Int = context.getColorValue(R.color.colorAccent)
    ) {
        ColorPickerDialog()
            .withColor(default)
            .withRetainInstance(false)
            .withTitle(title)
            .withCornerRadius(16f)
            .withAlphaEnabled(false)
            .withPicker(ImagePickerView::class.java)
            .withPresets()
            .withListener { _, color ->
                if (view == cv_product_text_color) viewModel.textColor =
                    color else viewModel.color = color
                view.setCardBackgroundColor(color)
            }
            .show(childFragmentManager, "colorPicker")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == extraScannerRequestCode && resultCode == RESULT_OK) {
            ed_product_code.setText(data?.getStringExtra(ScannerActivity.extraProductCode) ?: "")
        }
    }
}