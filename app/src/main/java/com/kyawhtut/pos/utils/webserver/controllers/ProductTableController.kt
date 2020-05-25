package com.kyawhtut.pos.utils.webserver.controllers

import com.kyawhtut.pos.data.db.entity.product
import com.kyawhtut.pos.utils.toBoolean
import com.yanzhenjie.andserver.annotation.*
import com.yanzhenjie.andserver.error.BasicException
import com.yanzhenjie.andserver.util.MediaType

/**
 * @author kyawhtut
 * @date 05/05/2020
 */
@RestController
@RequestMapping("/api/v1")
class ProductTableController : ApiController() {

    @GetMapping(
        path = ["/product"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    override fun get(
        @RequestParam(name = "page", required = false, defaultValue = "-1") page: String,
        @RequestParam(name = "limit", required = false, defaultValue = "-1") limit: String,
        @RequestParam(name = "filter", required = false, defaultValue = "") filter: String
    ): Any? {
        return db.productDao().get(
            getSimpleQuery(
                "product",
                page.toInt(),
                limit.toInt(),
                filter
            )
        )
    }

    // todo: get by id
    @GetMapping(
        path = ["/product/{productId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    fun getProductById(@PathVariable(name = "productId") productId: String): Any? {
        db.productDao().get(productId.toInt()).run {
            if (this != null) return this
            else throw BasicException(404, "Product not found")
        }
    }

    // todo : update
    @PutMapping(
        path = ["/product/{productId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    fun updateProduct(
        @PathVariable(name = "productId") productId: String,
        @RequestParam(
            name = "productCode",
            required = false
        ) productCode: String,
        @RequestParam(name = "productName", required = false) productName: String,
        @RequestParam(name = "productDescription", required = false) productDescription: String,
        @RequestParam(name = "productPrice", required = false) productPrice: String,
        @RequestParam(name = "productColor", required = false) productColor: String,
        @RequestParam(
            name = "productTextColor",
            required = false
        ) productTextColor: String,
        @RequestParam(name = "productCount", required = false) productCount: String,
        @RequestParam(name = "productRetailPrice", required = false) productRetailPrice: String,
        @RequestParam(name = "categoryId", required = false) categoryId: String,
        @RequestParam(name = "productAvailable", required = false) productAvailable: String,
        @RequestParam(
            name = "showAlertRemainAmount",
            required = false
        ) showAlertRemainAmount: String,
        @RequestParam(name = "updatedUserId", required = true) updatedUserId: String
    ): Any? {
        db.productDao().get(productId.toInt()).also { data ->
            if (data == null) throw BasicException(404, "Product not found")
            db.productDao().update(
                product {
                    this.id = data.id
                    this.productCode = data.productCode swipe productCode
                    this.productName = data.productName swipe productName
                    this.productDescription = data.productDescription swipe productDescription
                    this.productPrice = data.productPrice swipe productPrice
                    this.productColor = data.productColor swipe productColor
                    this.productTextColor = data.productTextColor swipe productTextColor
                    this.productCount = data.productCount swipe productCount
                    this.productRetailPrice = data.productRetailPrice swipe productRetailPrice
                    this.categoryId = data.categoryId swipe categoryId
                    this.productAvailable = data.productAvailable swipe productAvailable
                    this.productRemainAmountShow =
                        (data.productRemainAmountShow swipe showAlertRemainAmount).toBoolean()
                    this.productSellCount = data.productSellCount
                    this.createdUserId = data.createdUserId
                    this.updatedUserId = updatedUserId.toInt()
                    this.createdDate = data.createdDate
                }
            )
            return "Update successfully."
        }
    }

    // todo: save
    @PostMapping(
        path = ["/product"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    fun insertProduct(
        @RequestParam(
            name = "productCode",
            required = true
        ) productCode: String,
        @RequestParam(name = "productName", required = true) productName: String,
        @RequestParam(name = "productDescription", required = true) productDescription: String,
        @RequestParam(name = "productPrice", required = true) productPrice: String,
        @RequestParam(name = "productColor", required = true) productColor: String,
        @RequestParam(
            name = "productTextColor",
            required = true
        ) productTextColor: String,
        @RequestParam(name = "productCount", required = true) productCount: String,
        @RequestParam(name = "productRetailPrice", required = true) productRetailPrice: String,
        @RequestParam(name = "categoryId", required = true) categoryId: String,
        @RequestParam(name = "productAvailable", required = true) productAvailable: String,
        @RequestParam(
            name = "showAlertRemainAmount",
            required = true
        ) showAlertRemainAmount: String,
        @RequestParam(name = "createdUserId", required = true) createdUserId: String
    ): Any? {
        db.productDao().insert(
            product {
                this.productCode = productCode
                this.productName = productName
                this.productDescription = productDescription
                this.productPrice = productPrice.toLong()
                this.productColor = productColor
                this.productTextColor = productTextColor
                this.productCount = productCount.toInt()
                this.productRetailPrice = productRetailPrice.toLong()
                this.categoryId = categoryId.toInt()
                this.productAvailable = productAvailable.toInt()
                this.productRemainAmountShow = showAlertRemainAmount.toBoolean()
                this.createdUserId = createdUserId.toInt()
                this.updatedUserId = createdUserId.toInt()
            }
        )
        return "Insert successfully."
    }

    // todo: delete
    @DeleteMapping(
        path = ["/product/{productId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    override fun delete(@PathVariable(name = "productId") id: String): String {
        if (db.productDao().canDeleteProductById(id.toInt()) != 0) throw BasicException(
            422,
            "Product can't delete."
        )
        if (db.productDao().get(id.toInt()) == null) throw BasicException(
            404,
            "Product can't find."
        )
        db.productDao().delete(id.toInt())
        return super.delete(id)
    }
}
