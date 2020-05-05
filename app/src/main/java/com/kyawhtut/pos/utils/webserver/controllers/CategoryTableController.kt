package com.kyawhtut.pos.utils.webserver.controllers

import com.kyawhtut.pos.data.db.entity.category
import com.kyawhtut.pos.utils.getCurrentTimeString
import com.yanzhenjie.andserver.annotation.*
import com.yanzhenjie.andserver.error.BasicException
import com.yanzhenjie.andserver.util.MediaType

@RestController
@RequestMapping("/api.v1")
class CategoryTableController : ApiController() {

    @GetMapping(
        path = ["/category"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    override fun get(
        @RequestParam(name = "page", required = false, defaultValue = "0") page: String,
        @RequestParam(name = "limit", required = false, defaultValue = "10") limit: String
    ): Any? {
        db.categoryDao().get().run {
            return when {
                page == "0" -> this
                page.toInt() < this.size / limit.toInt() -> this.subList(
                    limit.toInt() + page.toInt(),
                    limit.toInt()
                )
                else -> throw BasicException(500, "Page not found.")
            }
        }
    }

    @GetMapping(
        path = ["/category/{categoryId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    fun getUserById(@PathVariable(name = "categoryId") userId: String): Any? {
        db.categoryDao().get(userId.toInt()).run {
            if (this != null) return this
            else throw BasicException(404, "Category not found")
        }
    }

    @PutMapping(
        path = ["/category/{categoryId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    fun updateCategory(
        @PathVariable(name = "categoryId") categoryId: String,
        @RequestParam(name = "categoryName", required = false) categoryName: String,
        @RequestParam(name = "categoryColor", required = false) categoryColor: String,
        @RequestParam(
            name = "categoryTextColor",
            required = false
        ) categoryTextColor: String,
        @RequestParam(
            name = "categorySellCount",
            required = false
        ) categorySellCount: String,
        @RequestParam(name = "updatedUserId", required = true) updatedUserId: String
    ): Any? {
        db.categoryDao().get(categoryId.toInt()).also { data ->
            if (data == null) throw BasicException(404, "Category not found")
            db.categoryDao().update(
                category {
                    this.id = data.id
                    this.categoryName = data.categoryName swipe categoryName
                    this.categoryColor = data.categoryColor swipe categoryColor
                    this.categoryTextColor = data.categoryTextColor swipe categoryTextColor
                    this.categorySellCount = data.categorySellCount swipe categorySellCount
                    this.updatedUserId = updatedUserId.toInt()
                    this.createdDate = getCurrentTimeString()
                }
            )
            return "Update successfully."
        }
    }

    @PostMapping(
        path = ["/category"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    fun insertCategory(
        @RequestParam(name = "categoryName") categoryName: String,
        @RequestParam(name = "categoryColor") categoryColor: String,
        @RequestParam(name = "categoryTextColor") categoryTextColor: String,
        @RequestParam(name = "categorySellCount") categorySellCount: String,
        @RequestParam(name = "createdUserId") createdUserId: String,
        @RequestParam(name = "updatedUserId") updatedUserId: String
    ): Any? {
        db.categoryDao().insert(
            category {
                this.categoryName = categoryName
                this.categoryColor = categoryColor.toInt()
                this.categoryTextColor = categoryTextColor.toInt()
                this.categorySellCount = categorySellCount.toInt()
                this.createdUserId = createdUserId.toInt()
                this.updatedUserId = updatedUserId.toInt()
                this.createdDate = getCurrentTimeString()
            }
        )
        return "Insert successfully."
    }

    @DeleteMapping(
        path = ["/category/{categoryId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    override fun delete(@PathVariable(name = "categoryId") id: String): String {
        if (db.categoryDao().canDeleteCategoryById(id.toInt()) != 0) throw BasicException(
            422,
            "Category can't delete."
        )
        if (db.categoryDao().get(id.toInt()) == null) throw BasicException(
            404,
            "Category can't find."
        )
        db.categoryDao().delete(id.toInt())
        return "Delete successfully."
    }
}