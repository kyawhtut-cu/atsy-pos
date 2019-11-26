package com.kyawhtut.pos.ui.category.dialog

import androidx.annotation.ColorRes
import androidx.lifecycle.ViewModel

class CategoryAddDialogViewModel(private val repo: CategoryAddDialogRepository) : ViewModel() {

    @ColorRes
    var categoryColor: Int = 0
    @ColorRes
    var categoryTextColor: Int = 0
    var categoryName = ""
    var userId: Int = 0

    fun insertCategory() {
        repo.insertCategory {
            categoryName = this@CategoryAddDialogViewModel.categoryName
            categoryColor = this@CategoryAddDialogViewModel.categoryColor
            categoryTextColor = this@CategoryAddDialogViewModel.categoryTextColor
            userId = this@CategoryAddDialogViewModel.userId
        }
    }
}