package com.kyawhtut.pos.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.kyawhtut.pos.data.db.entity.ProductEntity
import com.kyawhtut.pos.utils.ProductType

class CategoryViewModel(private val repo: CategoryRepository) : ViewModel() {

    var categoryId: Int = 0
        set(value) {
            repo.categoryId = value
            field = value
        }
    private val _categoryData = repo.getCategory()
    private val _productData: LiveData<List<ProductEntity>> = repo.getProductList()

    val dataList = MediatorLiveData<List<ProductEntity>>()

    fun removeSource() {
        dataList.removeSource(_categoryData)
        dataList.removeSource(_productData)
    }

    fun addSource(sourceType: ProductType) {
        removeSource()
        dataList.addSource(
            when (sourceType) {
                is ProductType.Product -> _productData
                else -> _categoryData
            }
        ) {
            dataList.postValue(it)
        }
    }
}