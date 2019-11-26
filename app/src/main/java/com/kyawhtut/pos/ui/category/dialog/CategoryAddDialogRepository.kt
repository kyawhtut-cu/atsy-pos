package com.kyawhtut.pos.ui.category.dialog

import com.kyawhtut.pos.data.db.entity.CategoryBuilder

interface CategoryAddDialogRepository {

    fun insertCategory(block: CategoryBuilder.() -> Unit)
}