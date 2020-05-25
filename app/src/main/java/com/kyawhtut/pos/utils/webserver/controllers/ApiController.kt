package com.kyawhtut.pos.utils.webserver.controllers

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kyawhtut.pos.data.db.AppDatabase
import com.kyawhtut.pos.utils.toBoolean
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class ApiController : KoinComponent {

    companion object {
        private const val DEFAULT_PAGE = 0
        private const val DEFAULT_LIMIT = 10
    }

    protected val db: AppDatabase by inject()

    abstract fun get(page: String, limit: String, filter: String): Any?

    open fun delete(id: String): String {
        return "Delete successfully."
    }

    protected fun getSimpleQuery(
        tableName: String,
        page: Int = -1,
        limit: Int = -1,
        filter: String = ""
    ): SimpleSQLiteQuery {
        var query = "select * from ${tableName}_table"

        // todo: adding page limit
        query += when {
            page == -1 && limit != -1 -> " limit $limit offset $DEFAULT_PAGE"
            page != -1 && limit == -1 -> " limit $DEFAULT_LIMIT offset $page"
            page != -1 && limit != -1 -> " limit $limit offset $page"
            else -> ""
        }

        query += getFilter(filter)

        return SimpleSQLiteQuery(query)
    }

    private fun getFilter(filter: String): String {
        return " where %s".format(filter).takeIf { filter.isNotEmpty() } ?: filter
    }
}

@Suppress("UNCHECKED_CAST")
infix fun <T> T.swipe(new: String): T {
    return if (new.isEmpty()) this
    else when (this) {
        is Long -> new.toLong() as T
        is Int -> new.toInt() as T
        is Double -> new.toDouble() as T
        is Float -> new.toFloat() as T
        is Boolean -> new.toInt().toBoolean() as T
        else -> new as T
    }
}

@Suppress("UNCHECKED_CAST")
infix fun <T> T.swipe(new: Long): T {
    return if (new.toString().isEmpty()) this
    else new as T
}

@Suppress("UNCHECKED_CAST")
infix fun <T> T.swipe(new: Int): T {
    return if (new.toString().isEmpty()) this
    else new as T
}

/*
fun <T> Iterable<T>.pagination(page: Int, limit: Int): Iterable<T> {
    val currentPage = page - 1
    var end = (currentPage * limit) + limit
    if(end > this.co)
}*/
