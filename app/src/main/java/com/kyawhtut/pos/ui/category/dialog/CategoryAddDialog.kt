package com.kyawhtut.pos.ui.category.dialog

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import com.google.android.material.card.MaterialCardView
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.db.entity.UserEntity
import com.kyawhtut.pos.ui.base.BaseDialogFragment
import com.kyawhtut.pos.utils.getColorValue
import com.kyawhtut.pos.utils.putArg
import kotlinx.android.synthetic.main.dialog_category_add.*
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog
import me.jfenn.colorpickerdialog.views.picker.ImagePickerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryAddDialog : BaseDialogFragment(R.layout.dialog_category_add, true) {

    companion object {
        private const val extraUserData = "extra.userData"

        fun show(fm: FragmentManager, user: UserEntity) {
            CategoryAddDialog().putArg(
                extraUserData to user
            ).show(fm, "CategoryAddDialog")
        }
    }

    private val viewModel: CategoryAddDialogViewModel by viewModel()

    override fun setup(bundle: Bundle) {
        viewModel.userId = (bundle.get(extraUserData) as UserEntity).id

        ed_category_name.addTextChangedListener {
            viewModel.categoryName = ed_category_name.mText
            if (ed_category_name.text.toString().isNotEmpty()) edt_category_name.error = ""
        }

        cv_category_color.setOnClickListener {
            showColorPickerDialog(cv_category_color, tv_category_color.mText.toString())
        }

        cv_category_text_color.setOnClickListener {
            showColorPickerDialog(cv_category_text_color, tv_category_text_color.mText.toString())
        }

        btn_ok.setOnClickListener {
            if (viewModel.categoryName.isEmpty()) edt_category_name.setError("Please enter category name").run { return@setOnClickListener }
            viewModel.insertCategory()
            dismiss()
        }

        btn_cancel.setOnClickListener {
            dismiss()
        }
    }

    private fun showColorPickerDialog(view: MaterialCardView, title: String) {
        ColorPickerDialog()
            .withColor(context.getColorValue(R.color.colorAccent))
            .withRetainInstance(false)
            .withTitle(title)
            .withCornerRadius(16f)
            .withAlphaEnabled(false)
            .withPicker(ImagePickerView::class.java)
            .withPresets()
            .withListener { pickerView, color ->
                if (view == cv_category_text_color) viewModel.categoryTextColor =
                    color else viewModel.categoryColor = color
                view.setCardBackgroundColor(color)
            }
            .show(childFragmentManager, "colorPicker")
    }
}