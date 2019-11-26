package com.kyawhtut.pos.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.kyawhtut.pos.data.db.entity.ProductEntity
import com.kyawhtut.pos.utils.ProductType
import com.kyawhtut.pos.utils.toColor

class CategoryViewModel(private val repo: CategoryRepository) : ViewModel() {

    var categoryId: Int = 0
    private val _categoryData = repo.getCategory()
    private val _productData: LiveData<List<ProductEntity>>
        get() = repo.getProductList(categoryId)

    val dataList = MediatorLiveData<List<ProductEntity>>()

    fun insertProduct() {
        repo.insertProduct {
            productName = "Product Name"
            productColor = "#ff0000".toColor()
            productTextColor = "#ffffff".toColor()
            categoryId = 2
        }
    }

    private fun removeSoure() {
        dataList.removeSource(_categoryData)
        dataList.removeSource(_productData)
    }

    fun addSource(sourceType: ProductType) {
        removeSoure()
        when (sourceType) {
            is ProductType.Product -> dataList.addSource(_productData) {
                dataList.postValue(it)
            }
            is ProductType.Category -> dataList.addSource(_categoryData) {
                dataList.postValue(it)
            }
        }
    }
}