package com.kyawhtut.pos.ui.category.dialog

import com.kyawhtut.pos.data.db.dao.CategoryDao
import com.kyawhtut.pos.data.db.entity.CategoryBuilder

class CategoryAddDialogRepositoryImpl(private val categoryDao: CategoryDao) :
    CategoryAddDialogRepository {
    override fun insertCategory(block: CategoryBuilder.() -> Unit) {
        categoryDao.insert(CategoryBuilder().apply(block).build())
    }
}