package com.kyawhtut.pos.ui.category

import androidx.lifecycle.LiveData
import com.kyawhtut.pos.data.db.entity.ProductBuilder
import com.kyawhtut.pos.data.db.entity.ProductEntity

interface CategoryRepository {

    var categoryId: Int

    fun getCategory(): LiveData<List<ProductEntity>>

    fun getProductList(): LiveData<List<ProductEntity>>

    fun insertProduct(block: ProductBuilder.() -> Unit)
}