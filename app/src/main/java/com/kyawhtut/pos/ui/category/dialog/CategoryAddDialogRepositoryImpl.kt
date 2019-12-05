package com.kyawhtut.pos.ui.category.dialog

import android.content.SharedPreferences
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.CategoryDao
import com.kyawhtut.pos.data.db.entity.CategoryBuilder
import com.kyawhtut.pos.data.db.entity.UserEntity

class CategoryAddDialogRepositoryImpl(
    private val categoryDao: CategoryDao,
    sh: SharedPreferences,
    rootUser: UserEntity
    ) : BaseRepositoryImpl(sh, rootUser),
    CategoryAddDialogRepository {

    override fun insertCategory(block: CategoryBuilder.() -> Unit) {
        categoryDao.insert(CategoryBuilder().apply(block).build())
    }

    override fun updateCategory(block: CategoryBuilder.() -> Unit) {
        categoryDao.update(CategoryBuilder().apply(block).build())
    }

    override fun getCategoryById(categoryId: Int) = categoryDao.get(categoryId)

    override fun canDeleteCategoryById(categoryId: Int): Boolean =
        categoryDao.canDeleteCategoryById(categoryId) == 0

    override fun deleteItemById(id: Int) {
        categoryDao.delete(id)
    }
}