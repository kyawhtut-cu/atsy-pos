package com.kyawhtut.pos.ui.category.dialog

import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.CategoryBuilder
import com.kyawhtut.pos.data.db.entity.CategoryEntity

interface CategoryAddDialogRepository : BaseRepository {

    fun insertCategory(block: CategoryBuilder.() -> Unit)

    fun updateCategory(block: CategoryBuilder.() -> Unit)

    fun getCategoryById(categoryId: Int): CategoryEntity?

    fun canDeleteCategoryById(categoryId: Int): Boolean
}