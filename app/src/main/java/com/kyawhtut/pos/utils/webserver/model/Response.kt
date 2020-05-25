package com.kyawhtut.pos.utils.webserver.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class SuccessResponse private constructor(
    @SerializedName("data")
    val data: Any,
    val status: Int = 200,
    val isSuccess: Boolean = true
) {

    class Builder {
        var data: Any = Object()

        fun build() = SuccessResponse(data)
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class ErrorResponse private constructor(
    val status: Int,
    val message: String,
    val isSuccess: Boolean
) {
    class Builder {
        var statusCode: Int = 400
        var message = ""

        fun build() = ErrorResponse(statusCode, message, true)
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

fun successResponse(block: SuccessResponse.Builder.() -> Unit) =
    SuccessResponse.Builder().apply(block).build()

fun errorResponse(block: ErrorResponse.Builder.() -> Unit) =
    ErrorResponse.Builder().apply(block).build()