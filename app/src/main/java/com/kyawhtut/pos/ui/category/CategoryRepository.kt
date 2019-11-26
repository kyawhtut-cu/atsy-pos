package com.kyawhtut.pos.ui.category

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.data.db.entity.ProductBuilder
import com.kyawhtut.pos.data.db.entity.ProductEntity

interface CategoryRepository {

    fun getCategory(): LiveData<List<ProductEntity>>

    fun getProductList(categoryId: Int): LiveData<List<ProductEntity>>

    fun insertProduct(block: ProductBuilder.() -> Unit)
}