package com.kyawhtut.pos.ui.category.dialog

import androidx.annotation.ColorRes
import androidx.lifecycle.ViewModel
import com.kyawhtut.pos.utils.getCurrentTimeString
import com.kyawhtut.pos.utils.toBoolean
import com.kyawhtut.pos.utils.toInt

class CategoryAddDialogViewModel(private val repo: CategoryAddDialogRepository) : ViewModel() {

    var categoryId: Int = 0
        set(value) {
            field = value
            if (value != 0)
                getCategoryById()
        }
    @ColorRes
    var categoryColor: Int = 0
    @ColorRes
    var categoryTextColor: Int = 0
    var categoryName = ""
    private var userId: Int = repo.getCurrentUser()?.id ?: 0
    var isActive = true
    private var createdDate: String = getCurrentTimeString()

    fun canDeleteCurrentCategory() =
        if (categoryId == 0) true else repo.canDeleteCategoryById(categoryId)

    fun deleteCurrentCategory() = repo.deleteItemById(categoryId)

    private fun getCategoryById() {
        repo.getCategoryById(categoryId)?.let {
            categoryName = it.categoryName
            categoryColor = it.categoryColor
            categoryTextColor = it.categoryTextColor
            userId = it.createdUserId
            isActive = it.categoryAvailable.toBoolean()
            createdDate = it.createdDate
        }
    }

    fun updateCategory() {
        repo.updateCategory {
            id = this@CategoryAddDialogViewModel.categoryId
            categoryName = this@CategoryAddDialogViewModel.categoryName
            categoryColor = this@CategoryAddDialogViewModel.categoryColor
            categoryTextColor = this@CategoryAddDialogViewModel.categoryTextColor
            createdUserId = this@CategoryAddDialogViewModel.userId
            updatedUserId = repo.getCurrentUser()?.id ?: userId
            categoryAvailable = this@CategoryAddDialogViewModel.isActive.toInt()
            createdDate = this@CategoryAddDialogViewModel.createdDate
        }
    }

    fun insertCategory() {
        repo.insertCategory {
            categoryName = this@CategoryAddDialogViewModel.categoryName
            categoryColor = this@CategoryAddDialogViewModel.categoryColor
            categoryTextColor = this@CategoryAddDialogViewModel.categoryTextColor
            createdUserId = this@CategoryAddDialogViewModel.userId
            updatedUserId = this@CategoryAddDialogViewModel.userId
            categoryAvailable = this@CategoryAddDialogViewModel.isActive.toInt()
            createdDate = getCurrentTimeString()
        }
    }
}