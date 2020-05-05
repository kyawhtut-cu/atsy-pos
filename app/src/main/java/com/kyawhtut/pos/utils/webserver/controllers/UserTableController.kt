package com.kyawhtut.pos.utils.webserver.controllers

import com.yanzhenjie.andserver.annotation.*
import com.yanzhenjie.andserver.error.BasicException
import com.yanzhenjie.andserver.util.MediaType

@RestController
@RequestMapping("/api.v1")
class UserTableController : ApiController() {

    @GetMapping(
        path = ["/user"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    override fun get(
        @RequestParam(name = "page", required = false, defaultValue = "0") page: String,
        @RequestParam(name = "limit", required = false, defaultValue = "10") limit: String
    ): Any? {
        return db.userDao().get()
    }

    @GetMapping(
        path = ["/user/{userId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE]
    )
    fun getUserById(@PathVariable(name = "userId") userId: String): Any? {
        db.userDao().get(userId.toInt()).run {
            if (this != null) return this
            else throw BasicException(404, "User not found")
        }
    }

    @DeleteMapping(
        path = ["/user/{userId}"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE, "application/json; charset=utf-8"]
    )
    override fun delete(@PathVariable(name = "userId") id: String): String {
        /*if (db.userDao().(id.toInt()) != 0) throw BasicException(
            422,
            "User can't delete."
        )*/
        if (db.userDao().get(id.toInt()) == null) throw BasicException(
            404,
            "User can't find."
        )
        db.userDao().delete(id.toInt())
        return "Delete successfully."
    }
}