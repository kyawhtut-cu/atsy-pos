package com.kyawhtut.pos.ui.product

import com.kyawhtut.pos.base.BaseViewModel
import com.kyawhtut.pos.utils.getCurrentTimeString
import com.kyawhtut.pos.utils.toBoolean
import com.kyawhtut.pos.utils.toInt

class ProductViewModel(private val repo: ProductRepository) : BaseViewModel(repo) {

    var productId: Int = 0
        set(value) {
            field = value
            if (value != 0) loadProduct()
        }
    var code = ""
    var name = ""
    var description = ""
    var price = 0L
    var color = 0
    var textColor = 0
    var count = 0
    var retailPrice = 0L
    var cId = 0
    var status = true
    var remainAmountShow = false
    var createUser = getCurrentUser()?.id ?: 0
    var createDate = getCurrentTimeString()

    private fun loadProduct() {
        with(repo.getProductById(productId)!!) {
            code = productCode
            name = productName
            description = productDescription
            price = productPrice
            color = productColor
            textColor = productTextColor
            count = productCount
            retailPrice = productRetailPrice
            cId = categoryId
            status = productAvailable.toBoolean()
            remainAmountShow = productRemainAmountShow.toBoolean()
            createUser = createdUserId
            createDate = createdDate
        }
    }

    fun getCategoryList() = repo.getCategoryList()

    fun canDeleteProduct() = repo.canDeleteProductById(productId)

    fun insert() {
        repo.insertProduct {
            productCode = code
            productName = name
            productDescription = description
            productPrice = price
            productColor = color
            productTextColor = textColor
            productCount = count
            productRetailPrice = retailPrice
            categoryId = cId
            productAvailable = status.toInt()
            productRemainAmountShow = remainAmountShow
            createdUserId = createUser
            updatedUserId = createUser
            createdDate = createDate
        }
    }

    fun update() {
        repo.updateProduct {
            id = productId
            productCode = code
            productName = name
            productPrice = price
            productColor = color
            productTextColor = textColor
            productCount = count
            productRetailPrice = retailPrice
            categoryId = cId
            productAvailable = status.toInt()
            productRemainAmountShow = remainAmountShow
            createdUserId = createUser
            updatedUserId = getCurrentUser()?.id ?: createUser
            createdDate = createDate
            updatedDate = getCurrentTimeString()
        }
    }

    fun deleteProductById() = repo.deleteItemById(productId)
}