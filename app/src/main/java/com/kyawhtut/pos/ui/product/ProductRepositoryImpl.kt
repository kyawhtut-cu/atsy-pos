package com.kyawhtut.pos.ui.product

import android.content.SharedPreferences
import com.kyawhtut.pos.base.BaseRepositoryImpl
import com.kyawhtut.pos.data.db.dao.CategoryDao
import com.kyawhtut.pos.data.db.dao.ProductDao
import com.kyawhtut.pos.data.db.entity.CategoryEntity
import com.kyawhtut.pos.data.db.entity.ProductBuilder
import com.kyawhtut.pos.data.db.entity.ProductEntity
import com.kyawhtut.pos.data.db.entity.UserEntity

class ProductRepositoryImpl(
    sh: SharedPreferences,
    rootUser: UserEntity,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) : BaseRepositoryImpl(sh, rootUser), ProductRepository {

    override fun insertProduct(block: ProductBuilder.() -> Unit) {
        productDao.insert(ProductBuilder().apply(block).build())
    }

    override fun updateProduct(block: ProductBuilder.() -> Unit) {
        productDao.update(ProductBuilder().apply(block).build())
    }

    override fun getProductById(productId: Int): ProductEntity? = productDao.get(productId)

    override fun canDeleteProductById(productId: Int): Boolean =
        productDao.canDeleteProductById(productId) == 0

    override fun deleteItemById(id: Int) {
        productDao.delete(id)
    }

    override fun getCategoryList(): List<CategoryEntity> = categoryDao.get()
}