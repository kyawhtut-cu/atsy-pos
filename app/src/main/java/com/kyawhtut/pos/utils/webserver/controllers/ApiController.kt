package com.kyawhtut.pos.utils.webserver.controllers

import com.kyawhtut.pos.data.db.AppDatabase
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class ApiController : KoinComponent {

    protected val db: AppDatabase by inject()

    abstract fun get(page: String, limit: String): Any?

    abstract fun delete(id: String): String


    /*@GetMapping(
        path = ["/get/{tableName}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    fun getTableData(@PathVariable(name = "tableName") tableName: String): Any? {
        return when (String.format("%s_table", tableName)) {
            "user_table" -> db.categoryDao().get()
            "role_table" -> db.roleDao().get()
            "category_table" -> db.categoryDao().get()
            "product_table" -> db.productDao().get()
            "customer_table" -> db.customerDao().get()
            "sell_table" -> db.sellDao().get()
            "ticket_table" -> db.ticketDao().get()
            "cart_header_table" -> db.cartDao().getCartHeader()
            "cart" -> db.cartDao().getCartList()
            else -> throw BasicException(404, "Table not found.")
        }
    }*/
}

@Suppress("UNCHECKED_CAST")
infix fun <T> T.swipe(new: String): T {
    return if (new.isEmpty()) this
    else new as T
}

/*
fun <T> Iterable<T>.pagination(page: Int, limit: Int): Iterable<T> {
    val currentPage = page - 1
    var end = (currentPage * limit) + limit
    if(end > this.co)
}*/
