package com.kyawhtut.pos.ui.product

import com.kyawhtut.pos.base.BaseRepository
import com.kyawhtut.pos.data.db.entity.CategoryEntity
import com.kyawhtut.pos.data.db.entity.ProductBuilder
import com.kyawhtut.pos.data.db.entity.ProductEntity

interface ProductRepository : BaseRepository {

    fun insertProduct(block: ProductBuilder.() -> Unit)

    fun updateProduct(block: ProductBuilder.() -> Unit)

    fun getProductById(productId: Int): ProductEntity?

    fun canDeleteProductById(productId: Int): Boolean

    fun getCategoryList(): List<CategoryEntity>
}