package com.kyawhtut.pos.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import com.kyawhtut.pos.data.db.dao.CategoryDao
import com.kyawhtut.pos.data.db.dao.ProductDao
import com.kyawhtut.pos.data.db.entity.CategoryEntity
import com.kyawhtut.pos.data.db.entity.ProductBuilder
import com.kyawhtut.pos.data.db.entity.ProductEntity

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao
) : CategoryRepository {

    override var categoryId: Int = 0

    override fun getCategory(): LiveData<List<ProductEntity>> = categoryDao.flowable().map {
        CategoryEntity.categoryList2ProductList(it)
    }.toLiveData()

    override fun getProductList(): LiveData<List<ProductEntity>> =
        productDao.flowable()
            .map {
                it.filter {
                    it.categoryId == categoryId
                }
            }.toLiveData()

    override fun insertProduct(block: ProductBuilder.() -> Unit) {
        productDao.insert(ProductBuilder().apply(block).build())
    }
}