package com.kyawhtut.pos.ui.category.dialog

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.google.android.material.card.MaterialCardView
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.fontchooserlib.util.toDisplay
import com.kyawhtut.pos.R
import com.kyawhtut.pos.base.BaseDialogFragment
import com.kyawhtut.pos.utils.*
import kotlinx.android.synthetic.main.dialog_category_add.*
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import me.jfenn.colorpickerdialog.views.picker.ImagePickerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryAddDialog : BaseDialogFragment(
    R.layout.dialog_category_add,
    true
) {

    companion object {
        private const val extraCategoryId = "extra.categoryId"

        fun show(fm: FragmentManager, categoryId: Int = 0) {
            CategoryAddDialog().putArg(
                extraCategoryId to categoryId
            ).show(fm, "CategoryAddDialog")
        }
    }

    private val viewModel: CategoryAddDialogViewModel by viewModel()

    override fun setup(bundle: Bundle) {
        viewModel.categoryId = bundle.getInt(extraCategoryId)

        ed_category_name.setText(viewModel.categoryName.toDisplay(FontChoose.isUnicode()))
        cv_category_color.setCardBackgroundColor(viewModel.categoryColor.toColor())
        cv_category_text_color.setCardBackgroundColor(viewModel.categoryTextColor.toColor())
        cb_active_status.isChecked = viewModel.isActive

        cb_active_status.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isActive = isChecked
        }

        ed_category_name.addTextChangedListener {
            viewModel.categoryName = ed_category_name.mText
            if (ed_category_name.text.toString().isNotEmpty()) edt_category_name.error = ""
        }

        cv_category_color.setOnClickListener {
            showColorPickerDialog(
                cv_category_color,
                tv_category_color.mText.toString(),
                viewModel.categoryColor.toColor()
            )
        }

        cv_category_text_color.setOnClickListener {
            showColorPickerDialog(
                cv_category_text_color,
                tv_category_text_color.mText.toString(),
                viewModel.categoryTextColor.toColor()
            )
        }

        if (viewModel.categoryId != 0) {
            tv_dialog_title.mText = getString(R.string.lbl_category_edit)
            with(viewModel.canDeleteCurrentCategory()) {
                btn_delete.isEnabled = this
                if (this) gp_delete_unavailable.gone() else gp_delete_unavailable.visible()
            }
        } else {
            btn_delete.gone()
            gp_delete_unavailable.gone()
        }

        btn_delete.setOnClickListener {
            viewModel.deleteCurrentCategory()
            dismiss()
        }

        btn_ok.setOnClickListener {
            if (viewModel.categoryName.isEmpty()) edt_category_name.setError("Please enter category name")
                .run { return@setOnClickListener }
            if (viewModel.categoryId == 0) viewModel.insertCategory() else viewModel.updateCategory()
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
            .withListener { pickerView, color ->
                if (view == cv_category_text_color) viewModel.categoryTextColor =
                    color.toHexString() else viewModel.categoryColor = color.toHexString()
                view.setCardBackgroundColor(color)
            }
            .show(childFragmentManager, "colorPicker")
    }
}